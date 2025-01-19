package com.nttbank.microservices.walletservice.business.impl;

import com.nttbank.microservices.commonlibrary.event.TransferDebitCardEvent;
import com.nttbank.microservices.commonlibrary.event.WalletTransferEvent;
import com.nttbank.microservices.walletservice.business.WalletService;
import com.nttbank.microservices.walletservice.dao.WalletDao;
import com.nttbank.microservices.walletservice.dao.WalletTransactionDAO;
import com.nttbank.microservices.walletservice.model.domain.DebitCard;
import com.nttbank.microservices.walletservice.model.domain.TransactionType;
import com.nttbank.microservices.walletservice.model.entity.DigitalWallet;
import com.nttbank.microservices.walletservice.model.entity.WalletTransaction;
import com.nttbank.microservices.walletservice.util.KafkaAccountUtil;
import com.nttbank.microservices.walletservice.util.KafkaDebitCardUtil;
import io.reactivex.rxjava3.core.Maybe;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WalletServiceImpl class provides the implementation of the WalletService interface.
 **/
@Slf4j
@Service
public class WalletServiceImpl implements WalletService {

  @Autowired
  private WalletDao walletDao;

  @Autowired
  private WalletTransactionDAO walletTransactionDAO;

  @Autowired
  private KafkaAccountUtil kafkaAccountUtil;

  @Autowired
  private KafkaDebitCardUtil kafkaDebitCardUtil;

  @Override
  public Maybe<DigitalWallet> findByDocumentId(String documentId) {
    return this.walletDao.findByDocumentId(documentId);
  }

  @Override
  public Maybe<WalletTransaction> transfer(String username, String phoneNumber, BigDecimal amount) {
    return walletDao.findByPhoneNumber(phoneNumber)
        .flatMap(receiver -> {
          if (receiver.getAccountId() == null) {
            return Maybe.just(WalletTransaction.builder()
                .status("error")
                .description("The receiver does not have an active account")
                .build());
          }
          return walletDao.findByDocumentId(username)
              .flatMap(sender -> {
                return walletTransactionDAO.saveTransaction(
                    WalletTransaction.builder()
                        .type(TransactionType.SEND_MONEY.name())
                        .status("pending")
                        .walletId(sender.getId())
                        .description("Processing the transfer")
                        .amount(amount.setScale(2, RoundingMode.HALF_UP))
                        .build()
                ).flatMap(transaction -> {
                  kafkaAccountUtil.sendMessage(WalletTransferEvent.builder()
                      .transactionId(transaction.getId())
                      .senderAccountId(sender.getAccountId())
                      .senderPhoneNumber(sender.getPhoneNumber())
                      .receiverAccountId(receiver.getAccountId())
                      .receiverPhoneNumber(receiver.getPhoneNumber())
                      .amount(amount)
                      .build());
                  return Maybe.just(transaction);
                });
              });
        });
  }

  @Override
  public Maybe<DigitalWallet> linkDebitCard(String documentId, String debitCardNumber) {
    return this.walletDao.findByDocumentId(documentId)
        .flatMap(wallet -> {
          wallet.setLinkedDebitCard(DebitCard.builder()
              .debitCardNumber(debitCardNumber)
              .build());
          return this.walletDao.update(wallet);
        });
  }

  @Override
  public Maybe<WalletTransaction> withdrawToDebitCard(String documentId, BigDecimal amount) {
    return this.walletDao.findByDocumentId(documentId)
        .flatMap(wallet -> {
          if (wallet.getLinkedDebitCard() == null) {
            return Maybe.just(WalletTransaction.builder()
                .status("error")
                .description("You need to link a debit card first in order to do this operation")
                .build());
          }
          return walletTransactionDAO.saveTransaction(
              WalletTransaction.builder()
                  .type(TransactionType.WITHDRAW_DEBIT_CARD.name())
                  .status("pending")
                  .walletId(wallet.getId())
                  .description("Processing the debit card withdrawal")
                  .amount(amount.setScale(2, RoundingMode.HALF_UP))
                  .build()
          ).flatMap(transaction -> {
            kafkaDebitCardUtil.sendMessage(TransferDebitCardEvent.builder()
                .transactionId(transaction.getId())
                .type(TransactionType.WITHDRAW_DEBIT_CARD.name())
                .accountId(wallet.getAccountId())
                .debitCardNumber(wallet.getLinkedDebitCard().getDebitCardNumber())
                .amount(amount)
                .build());
            return Maybe.just(transaction);
          });
        });
  }

  @Override
  public Maybe<WalletTransaction> depositFromDebitCard(String documentId, BigDecimal amount) {
    return this.walletDao.findByDocumentId(documentId)
        .flatMap(wallet -> {
          if (wallet.getLinkedDebitCard() == null) {
            return Maybe.just(WalletTransaction.builder()
                .status("error")
                .description("You need to link a debit card first in order to do this operation")
                .build());
          }
          return walletTransactionDAO.saveTransaction(
              WalletTransaction.builder()
                  .type(TransactionType.DEPOSIT_DEBIT_CARD.name())
                  .status("pending")
                  .walletId(wallet.getId())
                  .description("Processing the debit card deposit")
                  .amount(amount.setScale(2, RoundingMode.HALF_UP))
                  .build()
          ).flatMap(transaction -> {
            kafkaDebitCardUtil.sendMessage(TransferDebitCardEvent.builder()
                .transactionId(transaction.getId())
                .type(TransactionType.DEPOSIT_DEBIT_CARD.name())
                .accountId(wallet.getAccountId())
                .debitCardNumber(wallet.getLinkedDebitCard().getDebitCardNumber())
                .amount(amount)
                .build());
            return Maybe.just(transaction);
          });
        });
  }
}
