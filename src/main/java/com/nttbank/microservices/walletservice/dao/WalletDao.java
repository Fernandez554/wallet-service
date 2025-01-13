package com.nttbank.microservices.walletservice.dao;

import com.nttbank.microservices.walletservice.model.entity.DigitalWallet;
import io.reactivex.rxjava3.core.Maybe;

public interface WalletDao {

  Maybe<DigitalWallet> findByDocumentId(String documentId);

  Maybe<DigitalWallet> findByPhoneNumber(String phoneNumber);

}
