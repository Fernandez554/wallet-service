package com.nttbank.microservices.walletservice.dao;

import com.nttbank.microservices.walletservice.model.entity.WalletTransaction;
import io.reactivex.rxjava3.core.Maybe;

/**
 * WalletTransactionDAO interface provides the methods to interact with the WalletTransaction
 * collection.
 **/
public interface WalletTransactionDAO {

  /**
   * updateTransaction method updates the WalletTransaction object.
   *
   * @param walletTransaction - WalletTransaction object.
   * @return WalletTransaction object.
   **/
  Maybe<WalletTransaction> updateTransaction(WalletTransaction walletTransaction);

  /**
   * saveTransaction method saves the WalletTransaction object.
   *
   * @param walletTransaction - WalletTransaction object.
   * @return WalletTransaction object.
   **/
  Maybe<WalletTransaction> saveTransaction(WalletTransaction walletTransaction);

}
