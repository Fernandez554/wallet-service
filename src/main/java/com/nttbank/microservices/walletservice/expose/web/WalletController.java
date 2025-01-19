package com.nttbank.microservices.walletservice.expose.web;

import com.nttbank.microservices.walletservice.business.WalletService;
import com.nttbank.microservices.walletservice.model.entity.DigitalWallet;
import com.nttbank.microservices.walletservice.model.entity.WalletTransaction;
import io.reactivex.rxjava3.core.Maybe;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.QueryParam;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for the Wallet service.
 *
 * <p>Provides endpoints for the Wallet service to interact with the DigitalWallet and
 * WalletTransaction collections.
 * </p>
 */
@RestController
@Slf4j
@RequestMapping("/wallet")
@Tag(name = "Yanki wallet Api", description = "The Wallet API")
public class WalletController {

  @Autowired
  private WalletService service;

  /**
   * userWalletDetails method retrieves the DigitalWallet object by documentId.
   *
   * @param documentId - documentId of the DigitalWallet object.
   * @return DigitalWallet object.
   **/
  @Operation(summary = "Get user wallet details",
      description = "Get the wallet details of the user.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User wallet details retrieved"),
      @ApiResponse(responseCode = "404", description = "User wallet details not found")
  })
  @GetMapping(value = "/", produces = {"application/json"})
  public Maybe<DigitalWallet> userWalletDetails(@RequestHeader("X-Username") String documentId) {
    return this.service.findByDocumentId(documentId);
  }

  /**
   * transfer method transfers money from one wallet to another.
   *
   * @param documentId  - documentId of the sender.
   * @param phoneNumber - phoneNumber of the receiver.
   * @param amount      - amount to transfer.
   * @return WalletTransaction object.
   **/
  @Operation(summary = "Transfer money",
      description = "Transfer money from one wallet to another.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Money transferred successfully"),
      @ApiResponse(responseCode = "404", description = "Money transfer failed")
  })
  @GetMapping(value = "/transfer", produces = {"application/json"})
  public Maybe<WalletTransaction> transfer(@RequestHeader("X-Username") String documentId,
      @QueryParam("phoneNumber") String phoneNumber, @QueryParam("amount") BigDecimal amount) {
    return this.service.transfer(documentId, phoneNumber, amount);
  }

  /**
   * linkDebitCard method links a debit card to the wallet.
   *
   * @param documentId      - documentId of the DigitalWallet object.
   * @param debitCardNumber - debit card number.
   * @return DigitalWallet object.
   **/
  @Operation(summary = "Link debit card",
      description = "Link a debit card to the wallet.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Debit card linked successfully"),
      @ApiResponse(responseCode = "404", description = "Debit card link failed")
  })
  @PostMapping(value = "/debitcard", produces = {"application/json"})
  public Maybe<DigitalWallet> linkDebitCard(@RequestHeader("X-Username") String documentId,
      @QueryParam("debitCardNumber") String debitCardNumber) {
    return this.service.linkDebitCard(documentId, debitCardNumber);
  }

  /**
   * withdrawToDebitCard method withdraws money from the wallet to the debit card.
   *
   * @param documentId - documentId of the DigitalWallet object.
   * @param amount     - amount to withdraw.
   * @return WalletTransaction object.
   **/
  @Operation(summary = "Withdraw to debit card",
      description = "Withdraw money from the wallet to the debit card.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Money withdraw successfully"),
      @ApiResponse(responseCode = "404", description = "Money withdrawal failed")
  })
  @PostMapping(value = "/debitcard/withdraw", produces = {"application/json"})
  public Maybe<WalletTransaction> withdrawToDebitCard(
      @RequestHeader("X-Username") String documentId,
      @QueryParam("amount") BigDecimal amount) {
    return this.service.withdrawToDebitCard(documentId, amount);
  }

  /**
   * depositFromDebitCard method deposits money from the debit card to the wallet.
   *
   * @param documentId - documentId of the DigitalWallet object.
   * @param amount     - amount to deposit.
   * @return WalletTransaction object.
   **/
  @Operation(summary = "Deposit from debit card",
      description = "Deposit money from the debit card to the wallet.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Money deposited successfully"),
      @ApiResponse(responseCode = "404", description = "Money deposit failed")
  })
  @PostMapping(value = "/debitcard/deposit", produces = {"application/json"})
  public Maybe<WalletTransaction> depositFromDebitCard(
      @RequestHeader("X-Username") String documentId,
      @QueryParam("amount") BigDecimal amount) {
    return this.service.depositFromDebitCard(documentId, amount);
  }


}
