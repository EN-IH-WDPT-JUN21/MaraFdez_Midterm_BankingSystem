package com.ironhack.midtermproject.controller.dto;

import com.ironhack.midtermproject.utils.Constants;
import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class SavingsDTO {

    @NotNull(message = "A specific balance and its currency are required")
    private Money balance;

    @NotNull(message = "An account holder id is required")
    private Long primaryOwner;

    private Long secondaryOwner;

    private Money minimumBalance;

    @NotNull(message = "A secret key is required")
    @NotBlank(message = "A secret key is required")
    private String secretKey;

    @DecimalMax(value = "0.5", message = "The interest rate cannot be greater than 0.5")
    @Positive(message = "Interest rate must be a positive number")
    private BigDecimal interestRate;


    // Constructor

    // interestRate by default
    public SavingsDTO(Money balance, Long primaryOwner, Long secondaryOwner, Money minimumBalance, String secretKey) {
        setBalance(balance);
        setPrimaryOwner(primaryOwner);
        setSecondaryOwner(secondaryOwner);
        setFirstMinimumBalance(minimumBalance);
        setSecretKey(secretKey);
        setInterestRate(Constants.SAVINGS_ACC_DEFAULT_INTEREST_RATE);
    }

    // minimumBalance by default
    public SavingsDTO(Money balance, Long primaryOwner, Long secondaryOwner, String secretKey, BigDecimal interestRate) {
        setBalance(balance);
        setPrimaryOwner(primaryOwner);
        setSecondaryOwner(secondaryOwner);
        setMinimumBalance(new Money(Constants.SAVINGS_ACC_DEFAULT_MIN_BALANCE));
        setSecretKey(secretKey);
        setInterestRate(interestRate);
    }

    // interestRate and minimumBalance by default
    public SavingsDTO(Money balance, Long primaryOwner, Long secondaryOwner, String secretKey) {
        setBalance(balance);
        setPrimaryOwner(primaryOwner);
        setSecondaryOwner(secondaryOwner);
        setMinimumBalance(new Money(Constants.SAVINGS_ACC_DEFAULT_MIN_BALANCE));
        setSecretKey(secretKey);
        setInterestRate(Constants.SAVINGS_ACC_DEFAULT_INTEREST_RATE);
    }

    // Setters

    // Setters

    public void setFirstMinimumBalance(Money minimumBalance) {
        if(minimumBalance.getAmount().compareTo(new BigDecimal(100)) >= 0 || minimumBalance.getAmount().compareTo(new BigDecimal(1000)) <= 0) {
            this.minimumBalance = minimumBalance;
        } else {
            System.out.println("The minimum balance cannot be lower than 100");
        }
    }

}
