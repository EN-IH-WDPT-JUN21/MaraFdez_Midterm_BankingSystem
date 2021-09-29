package com.ironhack.midtermproject.controller.dto;

import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter

public class TransactionDTO {

    @NotNull(message = "An account id is required")
    private Long provenanceAccountId;

    @NotNull(message = "An account id is required")
    private Long destinationAccountId;

    @NotNull(message = "A name must be provided")
    @NotBlank(message = "A name must be provided")
    private String destinationAccountHolderName;

    private Money transaction;

    private LocalDateTime transactionDate;

    public TransactionDTO(Long provenanceAccountId, Long destinationAccountId, String destinationAccountHolderName,
                          Money transaction, LocalDateTime transactionDate) {
        setProvenanceAccountId(provenanceAccountId);
        setDestinationAccountId(destinationAccountId);
        setDestinationAccountHolderName(destinationAccountHolderName);
        setTransaction(transaction);
        setTransactionDate(transactionDate);
    }

    public TransactionDTO(Long provenanceAccountId, Long destinationAccountId, String destinationAccountHolderName, Money transaction) {
        setProvenanceAccountId(provenanceAccountId);
        setDestinationAccountId(destinationAccountId);
        setDestinationAccountHolderName(destinationAccountHolderName);
        setTransaction(transaction);
        setTransactionDate(LocalDateTime.now());
    }

}
