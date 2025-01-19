package com.nttbank.microservices.walletservice.model.domain;

import lombok.Builder;
import lombok.Data;

/**
 * DebitCard class represents a debit card entity in the system.
 */
@Builder
@Data
public class DebitCard {

  private String debitCardNumber;
}
