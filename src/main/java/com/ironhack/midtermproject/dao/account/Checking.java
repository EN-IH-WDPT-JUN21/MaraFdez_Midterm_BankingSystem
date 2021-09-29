package com.ironhack.midtermproject.dao.account;

import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.utils.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter

@Entity
@PrimaryKeyJoinColumn(name = "id")
@DynamicUpdate
@Table(name = "checking_account")
public class Checking extends Account{
    // When creating a new Checking account, if the primaryOwner is more than 24, a regular Checking Account should be created

    @NotNull(message = "A secret key is required")
    @NotBlank(message = "A secret key is required")
    @Column(name = "secret_key")
    private String secretKey;

    // Constructor

    // minimumBalance and monthlyMaintenanceFee are final properties
    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        setSecretKey(secretKey);
    }

}
