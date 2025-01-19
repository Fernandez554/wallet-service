package com.nttbank.microservices.walletservice.model.domain;

/**
 * Enum representing the type of transaction that can be performed in the system.
 */
public enum TransactionType {
  SEND_MONEY,
  RECEIVE_MONEY,
  DEPOSIT_DEBIT_CARD,
  WITHDRAW_DEBIT_CARD
}
