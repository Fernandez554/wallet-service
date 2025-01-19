package com.nttbank.microservices.walletservice.business;

import com.nttbank.microservices.walletservice.model.entity.WalletTransaction;
import io.reactivex.rxjava3.core.Maybe;

/**
 * WalletTransactionService interface provides the methods to interact with the wallet transaction
 * service.
 **/
public interface WalletTransactionService {

  /**
   * Update a wallet transaction.
   **/
  Maybe<WalletTransaction> updateTransaction(WalletTransaction walletTransaction);

  /**
   * Save a wallet transaction.
   **/
  Maybe<WalletTransaction> saveTransaction(WalletTransaction walletTransaction);

}
