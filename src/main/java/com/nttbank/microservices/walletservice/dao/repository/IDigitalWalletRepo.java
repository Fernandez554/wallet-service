package com.nttbank.microservices.walletservice.dao.repository;

import com.nttbank.microservices.walletservice.model.entity.DigitalWallet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * IDigitalWalletRepo interface provides the methods to interact with the DigitalWallet collection.
 **/
public interface IDigitalWalletRepo extends ReactiveMongoRepository<DigitalWallet, String> {

  /**
   * findByDocumentId method retrieves the DigitalWallet object by documentId.
   *
   * @param documentId - documentId of the DigitalWallet object.
   * @return DigitalWallet object.
   **/
  Mono<DigitalWallet> findByDocumentId(String documentId);

  /**
   * findByPhoneNumber method retrieves the DigitalWallet object by phoneNumber.
   *
   * @param phoneNumber - phoneNumber of the DigitalWallet object.
   * @return DigitalWallet object.
   **/
  Mono<DigitalWallet> findByPhoneNumber(String phoneNumber);
}
