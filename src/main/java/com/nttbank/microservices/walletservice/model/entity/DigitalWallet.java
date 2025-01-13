package com.nttbank.microservices.walletservice.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nttbank.microservices.walletservice.model.domain.DebitCard;
import com.nttbank.microservices.walletservice.model.domain.StatusType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Data
@Document(collection = "wallets")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class DigitalWallet {

  @EqualsAndHashCode.Include
  @Id
  private String id;
  private String accountId;
  private String documentId;
  private String phoneNumber;
  private String imei;
  private String email;
  private BigDecimal balance;
  private DebitCard linkedDebitCard;
  private String pin;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String status;
}
