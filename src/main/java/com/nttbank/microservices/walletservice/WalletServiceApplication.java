package com.nttbank.microservices.walletservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** WalletServiceApplication class is the entry point for the Wallet Service application. */
@SpringBootApplication
public class WalletServiceApplication {

  /** Main method to run the Wallet Service application. */
  public static void main(String[] args) {
    SpringApplication.run(WalletServiceApplication.class, args);
  }

}
