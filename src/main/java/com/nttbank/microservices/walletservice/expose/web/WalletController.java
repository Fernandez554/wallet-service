package com.nttbank.microservices.walletservice.expose.web;

import com.nttbank.microservices.walletservice.business.WalletService;
import com.nttbank.microservices.walletservice.model.domain.TransferResponse;
import com.nttbank.microservices.walletservice.model.entity.DigitalWallet;
import io.reactivex.rxjava3.core.Maybe;
import jakarta.ws.rs.QueryParam;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/wallet")
public class WalletController {

  @Autowired
  private WalletService service;

  @GetMapping(value = "/", produces = {"application/json"})
  public Maybe<DigitalWallet> userWalletDetails(@RequestHeader("X-Username") String username) {
    return this.service.findByDocumentId(username);
  }

  @GetMapping(value = "/transfer", produces = {"application/json"})
  public Maybe<TransferResponse> transfer(@RequestHeader("X-Username") String username,
      @QueryParam("phoneNumber") String phoneNumber, @QueryParam("amount") BigDecimal amount) {
    return this.service.transfer(username, phoneNumber, amount);
  }


}
