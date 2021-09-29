package com.ironhack.midtermproject.dao.account;

import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.utils.Constants;
import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.bcel.Const;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


@Entity
@PrimaryKeyJoinColumn(name = "id")
@DynamicUpdate
@Table(name = "credit_card_account")
public class CreditCard extends Account{

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency"))
    })
    private Money creditLimit;

    @DecimalMin(value = "0.1", message = "The interest rate cannot be lower than 0.1")
    @Positive(message = "Interest rate must be a positive number")
    @Column(name = "interest_rate", columnDefinition = "DECIMAL(4,4)")
    private BigDecimal interestRate;

    @Column(name = "last_interest_application_date")
    private LocalDate lastInterestApplicationDate = getCreationDate();


    // Constructor

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setFirstCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }

    // interestRate set by default
    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit) {
        super(balance, primaryOwner, secondaryOwner);
        setFirstCreditLimit(creditLimit);
        setInterestRate(Constants.CCARD_DEFAULT_INTEREST_RATE);
    }

    // creditLimit set by default
    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setCreditLimit(new Money(Constants.CCARD_DEFAULT_CREDIT_LIMIT));
        setInterestRate(interestRate);
    }

    // interestRate and creditLimit set by default
    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
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
