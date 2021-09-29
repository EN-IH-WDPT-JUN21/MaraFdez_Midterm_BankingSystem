package com.ironhack.midtermproject.dao.account;

import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency"))
    })
    private Money balance;

    @ManyToOne
    @JoinColumn(name = "account_holder_id")
    private AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    private AccountHolder secondaryOwner;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "creation_date")
    private LocalDate creationDate = LocalDate.now();

    @Enumerated(value = EnumType.STRING)
    private Status status = Status.ACTIVE;


    // Constructor

    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        setBalance(balance);
        setPrimaryOwner(primaryOwner);
        setSecondaryOwner(secondaryOwner);
    }

    // Getters

    // Makes secondaryOwner optional
    public Optional<AccountHolder> getSecondaryOwner() {
        return Optional.ofNullable(secondaryOwner);
    }

}
