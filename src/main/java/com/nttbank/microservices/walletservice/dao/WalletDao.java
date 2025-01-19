package com.nttbank.microservices.walletservice.dao;

import com.nttbank.microservices.walletservice.model.entity.DigitalWallet;
import io.reactivex.rxjava3.core.Maybe;

/**
 * WalletDao interface provides the methods to interact with the DigitalWallet collection.
 **/
public interface WalletDao {

  /**
   * findByDocumentId method retrieves the DigitalWallet object by documentId.
   *
   * @param documentId - documentId of the DigitalWallet object.
   * @return DigitalWallet object.
   **/
  Maybe<DigitalWallet> findByDocumentId(String documentId);

  /**
   * findByPhoneNumber method retrieves the DigitalWallet object by phoneNumber.
   *
   * @param phoneNumber - phoneNumber of the DigitalWallet object.
   * @return DigitalWallet object.
   **/
  Maybe<DigitalWallet> findByPhoneNumber(String phoneNumber);

  /**
   * update method updates the DigitalWallet object.
   *
   * @param digitalWallet - DigitalWallet object.
   * @return DigitalWallet object.
   **/
  Maybe<DigitalWallet> update(DigitalWallet digitalWallet);
}
