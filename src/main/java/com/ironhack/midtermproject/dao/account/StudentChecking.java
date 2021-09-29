package com.ironhack.midtermproject.dao.account;

import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@PrimaryKeyJoinColumn(name = "id")
@DynamicUpdate
@Table(name = "student_checking_account")
public class StudentChecking extends Account{
    // StudentChecking Accounts are for account holders under 24 years old.

    @NotNull(message = "A secret key is required")
    @NotBlank(message = "A secret key is required")
    @Column(name = "secret_key")
    private String secretKey;


    // Constructor

    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        setSecretKey(secretKey);
    }

}
