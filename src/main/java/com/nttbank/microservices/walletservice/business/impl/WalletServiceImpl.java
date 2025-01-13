package com.nttbank.microservices.walletservice.business.impl;

import com.nttbank.microservices.commonlibrary.event.WalletTransferEvent;
import com.nttbank.microservices.walletservice.business.WalletService;
import com.nttbank.microservices.walletservice.dao.WalletDao;
import com.nttbank.microservices.walletservice.model.domain.TransferResponse;
import com.nttbank.microservices.walletservice.model.entity.DigitalWallet;
import com.nttbank.microservices.walletservice.util.KafkaUtil;
import io.reactivex.rxjava3.core.Maybe;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {

  @Autowired
  private WalletDao dao;

  @Autowired
  private KafkaUtil kafkaUtil;

  @Override
  public Maybe<DigitalWallet> findByDocumentId(String documentId) {
    return this.dao.findByDocumentId(documentId);
  }

  @Override
  public Maybe<TransferResponse> transfer(String username, String phoneNumber, BigDecimal amount) {
    return dao.findByPhoneNumber(phoneNumber)
        .flatMap(receiver -> {
          if (receiver.getAccountId() == null) {
            // Handle the case where the receiver does not have an active account
            return Maybe.just(TransferResponse.builder()
                .status("error")
                .message("the receiver does not have an active account")
                .build());
          }
          // Retrieve the sender's wallet
          return dao.findByDocumentId(username)
              .flatMap(sender -> {
                kafkaUtil.sendMessage(WalletTransferEvent.builder()
                    .senderAccountId(sender.getAccountId())
                    .receiverAccountId(receiver.getAccountId())
                    .amount(amount)
                    .build());
                TransferResponse response = TransferResponse.builder()
                    .status("PENDING")
                    .message("Processing the transfer")
                    .build();
                return Maybe.just(response);
              });
        });
  }
}
