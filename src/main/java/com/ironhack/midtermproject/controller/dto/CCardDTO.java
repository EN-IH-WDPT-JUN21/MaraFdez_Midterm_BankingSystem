package com.ironhack.midtermproject.controller.dto;

import com.ironhack.midtermproject.utils.Constants;
import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CCardDTO {

    @NotNull(message = "A specific balance and its currency are required")
    private Money balance;

    @NotNull(message = "An account holder id is required")
    private Long primaryOwner;

    private Long secondaryOwner;

    private Money creditLimit;

    @DecimalMin(value = "0.1", message = "The interest rate cannot be lower than 0.1")
    @Positive(message = "Interest rate must be a positive number")
    private BigDecimal interestRate;

    // Constructor

    // interestRate set by default
    public CCardDTO(Money balance, Long primaryOwner, Long secondaryOwner, Money creditLimit) {
        setBalance(balance);
        setPrimaryOwner(primaryOwner);
        setSecondaryOwner(secondaryOwner);
        setFirstCreditLimit(creditLimit);
        setInterestRate(Constants.CCARD_DEFAULT_INTEREST_RATE);
    }

    // creditLimit set by default
    public CCardDTO(Money balance, Long primaryOwner, Long secondaryOwner, BigDecimal interestRate) {
        setBalance(balance);
        setPrimaryOwner(primaryOwner);
        setSecondaryOwner(secondaryOwner);
        setCreditLimit(new Money(Constants.CCARD_DEFAULT_CREDIT_LIMIT));
        setInterestRate(interestRate);
    }

    // interestRate and creditLimit set by default
    public CCardDTO(Money balance, Long primaryOwner, Long secondaryOwner) {
        setBalance(balance);
        setPrimaryOwner(primaryOwner);
        setSecondaryOwner(secondaryOwner);
        setCreditLimit(new Money(Constants.CCARD_DEFAULT_CREDIT_LIMIT));
        setInterestRate(Constants.CCARD_DEFAULT_INTEREST_RATE);
    }

    // Setters

    public void setFirstCreditLimit(Money creditLimit) {
        if(creditLimit.getAmount().compareTo(new BigDecimal(100)) >= 0 || creditLimit.getAmount().compareTo(new BigDecimal(100000)) <= 0) {
            this.creditLimit = creditLimit;
        } else {
            System.out.println("The credit limit cannot be lower than 100 or greater than 100000");
        }
    }

}
