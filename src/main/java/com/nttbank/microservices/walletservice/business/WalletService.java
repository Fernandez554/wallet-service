package com.nttbank.microservices.walletservice.business;

import com.nttbank.microservices.walletservice.model.domain.TransferResponse;
import com.nttbank.microservices.walletservice.model.entity.DigitalWallet;
import io.reactivex.rxjava3.core.Maybe;
import java.math.BigDecimal;

public interface WalletService {

  Maybe<DigitalWallet> findByDocumentId(String documentId);
  Maybe<TransferResponse> transfer(String username, String phoneNumber, BigDecimal amount);
}
