package com.nttbank.microservices.walletservice.dao.impl;

import com.nttbank.microservices.walletservice.dao.WalletTransactionDAO;
import com.nttbank.microservices.walletservice.dao.repository.IWalletTransactionRepo;
import com.nttbank.microservices.walletservice.model.entity.WalletTransaction;
import io.reactivex.rxjava3.core.Maybe;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.adapter.rxjava.RxJava3Adapter;

/**
 * WalletTransactionDaoImpl class provides the implementation of the WalletTransactionDAO
 * interface.
 **/
@Service
public class WalletTransactionDaoImpl implements WalletTransactionDAO {

  @Autowired
  private IWalletTransactionRepo transactionRepo;

  @Override
  public Maybe<WalletTransaction> updateTransaction(WalletTransaction walletTransaction) {
    return RxJava3Adapter.monoToMaybe(
        this.transactionRepo.findById(walletTransaction.getId())
            .flatMap(transaction -> {
              return this.transactionRepo.save(WalletTransaction.builder()
                  .type(walletTransaction.getType())
                  .amount(walletTransaction.getAmount())
                  .status(walletTransaction.getStatus())
                  .description(walletTransaction.getDescription())
                  .updatedAt(LocalDateTime.now())
                  .build());
            })
    );
  }

  @Override
  public Maybe<WalletTransaction> saveTransaction(WalletTransaction walletTransaction) {
    return RxJava3Adapter.monoToMaybe(this.transactionRepo.save(walletTransaction));

  }

}
