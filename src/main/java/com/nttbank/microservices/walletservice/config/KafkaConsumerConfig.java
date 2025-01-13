package com.nttbank.microservices.walletservice.config;


import com.nttbank.microservices.commonlibrary.event.CreateWalletEvent;
import com.nttbank.microservices.commonlibrary.event.GenericEvent;
import com.nttbank.microservices.walletservice.model.domain.StatusType;
import com.nttbank.microservices.walletservice.dao.repository.IDigitalWalletRepo;
import com.nttbank.microservices.walletservice.model.entity.DigitalWallet;
import java.util.HashMap;
import java.util.Map;
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


@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

  private final IDigitalWalletRepo digitalWalletRepo;

  @Value("${kafka.nttbank.server:127.0.0.1}")
  private String kafkaServer;
  @Value("${kafka.nttbank.port:9092}")
  private String kafkaPort;
  @Value("${kafka.nttbank.topic:nttbank}")
  private String topicName = "wallet-service-management-v1";

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
        JsonDeserializer.class.getName());

    kafkaProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "com.nttbank.microservices.*");

    return new DefaultKafkaConsumerFactory<>(kafkaProperties);
  }


  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, GenericEvent<? extends GenericEvent>> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, GenericEvent<? extends GenericEvent>> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }

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
              .build())
          .subscribe(savedWallet -> log.info("Wallet updated: {}", savedWallet));
    }


  }

}
