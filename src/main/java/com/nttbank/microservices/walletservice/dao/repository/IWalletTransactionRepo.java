package com.nttbank.microservices.walletservice.dao.repository;

import com.nttbank.microservices.walletservice.model.entity.WalletTransaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * IWalletTransactionRepo interface provides the methods to interact with the WalletTransaction
 * collection.
 **/
public interface IWalletTransactionRepo extends ReactiveMongoRepository<WalletTransaction, String> {

}
