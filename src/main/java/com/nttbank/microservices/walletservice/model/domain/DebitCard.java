package com.nttbank.microservices.walletservice.model.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DebitCard {
  private String debitCardNumber;
  private String expirationDate;
}
