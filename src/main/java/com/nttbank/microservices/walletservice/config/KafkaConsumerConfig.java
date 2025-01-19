package com.nttbank.microservices.walletservice.config;

import com.nttbank.microservices.commonlibrary.event.CreateWalletEvent;
import com.nttbank.microservices.commonlibrary.event.DebitCardTransactionEvent;
import com.nttbank.microservices.commonlibrary.event.GenericEvent;
import com.nttbank.microservices.commonlibrary.event.WalletTransactionEvent;
import com.nttbank.microservices.walletservice.business.WalletTransactionService;
import com.nttbank.microservices.walletservice.dao.repository.IDigitalWalletRepo;
import com.nttbank.microservices.walletservice.dao.repository.IWalletTransactionRepo;
import com.nttbank.microservices.walletservice.model.domain.TransactionType;
import com.nttbank.microservices.walletservice.model.entity.DigitalWallet;
import com.nttbank.microservices.walletservice.model.entity.WalletTransaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

/**
 * Kafka consumer configuration.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

  private final WalletTransactionService transferService;

  private final IDigitalWalletRepo digitalWalletRepo;
  private final IWalletTransactionRepo walletTransactionRepo;

  @Value("${kafka.nttbank.server:127.0.0.1}")
  private String kafkaServer;
  @Value("${kafka.nttbank.port:9092}")
  private String kafkaPort;
  @Value("${kafka.nttbank.topic.consumer:nttbank}")
  private String topicName;

  /**
   * Kafka consumer factory.
   */
  @Bean
  public ConsumerFactory<String, GenericEvent<? extends GenericEvent>> consumerFactory() {
    Map<String, Object> kafkaProperties = new HashMap<>();
    kafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer + ":" + kafkaPort);
    kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, topicName);

    kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        ErrorHandlingDeserializer.class);
    kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        ErrorHandlingDeserializer.class);

    kafkaProperties.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, JsonDeserializer.class);
    kafkaProperties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS,
        JsonDeserializer.class);

    kafkaProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "com.nttbank.microservices.*");

    return new DefaultKafkaConsumerFactory<>(kafkaProperties);
  }

  /**
   * Kafka listener container factory.
   */
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, GenericEvent<? extends GenericEvent>>
      kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, GenericEvent<? extends GenericEvent>> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }

  /**
   * Listen to the topic and perform the necessary actions.
   */
  @KafkaListener(topics = "wallet-service-management-v1")
  public void listenTopic(GenericEvent<? extends GenericEvent> obj) {

    if (obj instanceof CreateWalletEvent walletEvent) {
      digitalWalletRepo.save(DigitalWallet.builder()
              .documentId(walletEvent.getDocumentId())
              .accountId(walletEvent.getAccountId())
              .email(walletEvent.getEmail())
              .imei(walletEvent.getImei())
              .phoneNumber(walletEvent.getPhoneNumber())
              .status(walletEvent.getStatus())
              .balance(BigDecimal.ZERO)
              .build())
          .subscribe(savedWallet -> log.info("Wallet created successfully for "
              + walletEvent.getDocumentId()));
    }

    if (obj instanceof WalletTransactionEvent transactionEvent) {
      if ("error".equals(transactionEvent.getStatus())) {
        walletTransactionRepo.findById(transactionEvent.getTransactionId())
            .flatMap(transaction -> {
              transaction.setDescription(transaction.getDescription());
              transaction.setStatus("TRANSACTION_FAILED");
              transaction.setUpdatedAt(LocalDateTime.now());
              return walletTransactionRepo.save(transaction);
            }).subscribe();
      }
      //Sender actions
      digitalWalletRepo.findByPhoneNumber(transactionEvent.getSenderPhoneNumber())
          .flatMap(senderWallet -> {
            senderWallet.setBalance(transactionEvent.getSenderBalanceUpdated());
            return digitalWalletRepo.save(senderWallet)
                .flatMap(e -> {
                  return walletTransactionRepo.findById(transactionEvent.getTransactionId())
                      .flatMap(transaction -> {
                        transaction.setStatus(transactionEvent.getStatus());
                        transaction.setDescription("money sent");
                        transaction.setUpdatedAt(LocalDateTime.now());
                        return walletTransactionRepo.save(transaction);
                      });
                });
          }).subscribe();

      //Reciever actions
      digitalWalletRepo.findByPhoneNumber(transactionEvent.getReceiverPhoneNumber())
          .flatMap(receiverWallet -> {
            BigDecimal newBalance = Optional.ofNullable(receiverWallet.getBalance())
                .orElse(BigDecimal.ZERO).add(transactionEvent.getAmount())
                .setScale(2, RoundingMode.HALF_UP);
            receiverWallet.setBalance(newBalance);
            return digitalWalletRepo.save(receiverWallet)
                .flatMap(e -> walletTransactionRepo.save(WalletTransaction.builder()
                    .amount(transactionEvent.getAmount())
                    .type(TransactionType.RECEIVE_MONEY.name())
                    .walletId(receiverWallet.getId())
                    .status(transactionEvent.getStatus())
                    .build()));
          }).subscribe();

    }

    if (obj instanceof DebitCardTransactionEvent debitEvent) {
      if ("error".equals(debitEvent.getStatus())) {
        walletTransactionRepo.findById(debitEvent.getTransId())
            .flatMap(transaction -> {
              transaction.setDescription(debitEvent.getDescription());
              transaction.setStatus("TRANSACTION_FAILED");
              transaction.setUpdatedAt(LocalDateTime.now());
              return walletTransactionRepo.save(transaction);
            }).subscribe();
      }

      // Sender actions
      walletTransactionRepo.findById(debitEvent.getTransId())
          .flatMap(transaction ->
              digitalWalletRepo.findById(transaction.getWalletId())
                  .flatMap(wallet -> {
                    BigDecimal newBalance = ("WITHDRAW_DEBIT_CARD".equals(debitEvent.getType()))
                        ? debitEvent.getBalanceUpdated()
                        : Optional.ofNullable(wallet.getBalance()).orElse(BigDecimal.ZERO)
                            .add(debitEvent.getAmount()).setScale(2, RoundingMode.HALF_UP);
                    wallet.setBalance(newBalance);
                    transaction.setId(debitEvent.getTransId());
                    transaction.setDescription(debitEvent.getDescription());
                    transaction.setStatus(debitEvent.getStatus());
                    transaction.setUpdatedAt(LocalDateTime.now());
                    return digitalWalletRepo.save(wallet)
                        .then(walletTransactionRepo.save(transaction));
                  })
          ).subscribe();
    }
  }
}
