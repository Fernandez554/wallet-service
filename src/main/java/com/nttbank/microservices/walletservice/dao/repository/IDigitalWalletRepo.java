package com.nttbank.microservices.walletservice.dao.repository;

import com.nttbank.microservices.walletservice.model.entity.DigitalWallet;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface IDigitalWalletRepo extends ReactiveMongoRepository<DigitalWallet, String> {

  Mono<DigitalWallet> findByDocumentId(String documentId);

  Mono<DigitalWallet> findByPhoneNumber(String phoneNumber);
}
