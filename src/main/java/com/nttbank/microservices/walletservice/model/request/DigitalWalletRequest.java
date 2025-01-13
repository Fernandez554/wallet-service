package com.nttbank.microservices.walletservice.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DigitalWalletRequest(

    @NotBlank(message = "Document ID is required")
    String documentId,

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Phone number must be valid")
    String phoneNumber,

    @NotBlank(message = "IMEI is required")
    @Size(min = 15, max = 17, message = "IMEI should be between 15 and 17 characters")
    String imei,

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email,

    @NotBlank(message = "PIN is required")
    @Size(min = 4, max = 6, message = "PIN should be between 4 and 6 digits")
    String pin

) {}