package com.nttbank.microservices.walletservice.business.impl;

import com.nttbank.microservices.walletservice.business.WalletTransactionService;
import com.nttbank.microservices.walletservice.dao.WalletTransactionDAO;
import com.nttbank.microservices.walletservice.model.entity.WalletTransaction;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WalletTransactionServiceImpl class provides the implementation of the WalletTransactionService
 * interface.
 **/
@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

  @Autowired
  private WalletTransactionDAO transactionDao;

  @Override
  public Maybe<WalletTransaction> updateTransaction(WalletTransaction walletTransaction) {
    return this.transactionDao.updateTransaction(walletTransaction);
  }

  @Override
  public Maybe<WalletTransaction> saveTransaction(WalletTransaction walletTransaction) {
    return this.transactionDao.saveTransaction(walletTransaction);
  }

}
