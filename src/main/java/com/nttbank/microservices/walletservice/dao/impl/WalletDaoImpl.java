package com.nttbank.microservices.walletservice.dao.impl;

import com.nttbank.microservices.walletservice.dao.WalletDao;
import com.nttbank.microservices.walletservice.dao.repository.IDigitalWalletRepo;
import com.nttbank.microservices.walletservice.dao.repository.IWalletTransactionRepo;
import com.nttbank.microservices.walletservice.model.entity.DigitalWallet;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.adapter.rxjava.RxJava3Adapter;

/**
 * WalletDaoImpl class provides the implementation of the WalletDao interface.
 **/
@Service
public class WalletDaoImpl implements WalletDao {

  @Autowired
  private IDigitalWalletRepo repo;

  @Autowired
  private IWalletTransactionRepo transactionRepo;

  @Override
  public Maybe<DigitalWallet> findByDocumentId(String documentId) {
    return RxJava3Adapter.monoToMaybe(this.repo.findByDocumentId(documentId));
  }

  @Override
  public Maybe<DigitalWallet> findByPhoneNumber(String phoneNumber) {
    return RxJava3Adapter.monoToMaybe(this.repo.findByPhoneNumber(phoneNumber));
  }

  @Override
  public Maybe<DigitalWallet> update(DigitalWallet digitalWallet) {
    return RxJava3Adapter.monoToMaybe(this.repo.save(digitalWallet));
  }

}
