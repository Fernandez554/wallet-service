package com.nttbank.microservices.walletservice.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nttbank.microservices.walletservice.model.domain.TransactionType;
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
@Document(collection = "wallet_transactions")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class WalletTransactions {
  @EqualsAndHashCode.Include
  @Id
  private String id;
  private String documentId;
  private String walletId;
  private TransactionType type;
  private BigDecimal amount;
  private LocalDateTime createdAt;
  private String description;
}
