package com.nttbank.microservices.walletservice.business;

import com.nttbank.microservices.walletservice.model.entity.DigitalWallet;
import com.nttbank.microservices.walletservice.model.entity.WalletTransaction;
import io.reactivex.rxjava3.core.Maybe;
import java.math.BigDecimal;

/**
 * WalletService interface provides the methods to interact with the wallet service.
 **/
public interface WalletService {

  /**
   * Find a digital wallet by document id.
   **/
  Maybe<DigitalWallet> findByDocumentId(String documentId);

  /**
   * Transfer money from one wallet to another.
   **/
  Maybe<WalletTransaction> transfer(String documentId, String phoneNumber, BigDecimal amount);

  /**
   * Link a debit card to a digital wallet.
   **/
  Maybe<DigitalWallet> linkDebitCard(String documentId, String debitCardNumber);

  /**
   * Unlink a debit card from a digital wallet.
   **/
  Maybe<WalletTransaction> withdrawToDebitCard(String documentId, BigDecimal amount);

  /**
   * Deposit money from a debit card to a digital wallet.
   **/
  Maybe<WalletTransaction> depositFromDebitCard(String documentId, BigDecimal amount);

}
