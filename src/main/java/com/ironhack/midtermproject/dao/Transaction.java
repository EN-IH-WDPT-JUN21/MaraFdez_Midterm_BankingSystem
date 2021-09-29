package com.ironhack.midtermproject.dao;

import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "provenance_account_id")
    private Long provenanceAccountId;

    @NotNull
    @Column(name = "destination_account_id")
    private Long destinationAccountId;

    @NotNull(message = "A name must be provided")
    @NotBlank(message = "A name must be provided")
    @Column(name = "destination_account_holder_name")
    private String destinationAccountHolderName;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "transaction_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "transaction_currency"))
    })
    private Money transaction;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;


    //Constructor

    public Transaction(Long provenanceAccountId, Long destinationAccountId, String destinationAccountHolderName,
                       Money transaction) {
        setProvenanceAccountId(provenanceAccountId);
        setDestinationAccountId(destinationAccountId);
        setDestinationAccountHolderName(destinationAccountHolderName);
        setTransaction(transaction);
        setTransactionDate(LocalDateTime.now());
    }

}
