package com.nttbank.microservices.walletservice.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * WalletTransaction class represents a wallet transaction entity in the system.
 */
@Data
@Document(collection = "wallet_transactions")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class WalletTransaction {

  @EqualsAndHashCode.Include
  @Id
  private String id;
  private String walletId;
  private String type;
  private String description;
  private BigDecimal amount;
  private String status;
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
  @Builder.Default
  private LocalDateTime updatedAt = LocalDateTime.now();

}
