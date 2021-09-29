package com.ironhack.midtermproject.dao.account;

import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.utils.Constants;
import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@PrimaryKeyJoinColumn(name = "id")
@DynamicUpdate
@Table(name = "savings_account")
public class Savings extends Account{

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private Money minimumBalance;

    @NotNull(message = "A secret key is required")
    @NotBlank(message = "A secret key is required")
    @Column(name = "secret_key")
    private String secretKey;

    @DecimalMax(value = "0.5", message = "The interest rate cannot be greater than 0.5")
    @Positive(message = "Interest rate must be a positive number")
    @Column(name = "interest_rate", columnDefinition = "DECIMAL(4,4)")
    private BigDecimal interestRate;

    @Column(name = "last_interest_application_date")
    private LocalDate lastInterestApplicationDate = getCreationDate();


    // Constructor

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   Money minimumBalance, String secretKey, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setFirstMinimumBalance(minimumBalance);
        setSecretKey(secretKey);
        setInterestRate(interestRate);
    }

    // interestRate by default
    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   Money minimumBalance, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        setFirstMinimumBalance(minimumBalance);
        setSecretKey(secretKey);
        setInterestRate(Constants.SAVINGS_ACC_DEFAULT_INTEREST_RATE);
    }

    // minimumBalance by default
    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   String secretKey, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setMinimumBalance(new Money(Constants.SAVINGS_ACC_DEFAULT_MIN_BALANCE));
        setSecretKey(secretKey);
        setInterestRate(interestRate);
    }

    // interestRate and minimumBalance by default
    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        setMinimumBalance(new Money(Constants.SAVINGS_ACC_DEFAULT_MIN_BALANCE));
        setSecretKey(secretKey);
        setInterestRate(Constants.SAVINGS_ACC_DEFAULT_INTEREST_RATE);
    }

    // Setters

    public void setFirstMinimumBalance(Money minimumBalance) {
        if(minimumBalance.getAmount().compareTo(new BigDecimal(100)) >= 0 || minimumBalance.getAmount().compareTo(new BigDecimal(1000)) <= 0) {
            this.minimumBalance = minimumBalance;
        } else {
            System.out.println("The minimum balance cannot be lower than 100");
        }
    }

}
