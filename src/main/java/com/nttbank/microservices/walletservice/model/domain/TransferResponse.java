package com.nttbank.microservices.walletservice.model.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferResponse {

  private String status;
  private String message;

}
