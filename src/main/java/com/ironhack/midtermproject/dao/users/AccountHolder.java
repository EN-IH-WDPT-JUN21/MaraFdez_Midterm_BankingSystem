package com.ironhack.midtermproject.dao.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.utils.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@DynamicUpdate
@Table(name = "account_holder")
public class AccountHolder extends User{
    // AccountHolders are able to access their own accounts and only their accounts
    // when passing the correct credentials using Basic Auth.

    // AccountHolders should be able to access their own account balance


    @NotNull(message = "A date of birth is required")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "date_of_birth")
    private LocalDate birthDate;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="street", column=@Column(name="current_street")),
            @AttributeOverride(name="buildingNumber", column=@Column(name="current_number")),
            @AttributeOverride(name="city", column=@Column(name="current_city")),
            @AttributeOverride(name="country", column=@Column(name="current_country")),
            @AttributeOverride(name="zipCode", column=@Column(name="current_zip"))
    })
    private Address primaryAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="street", column=@Column(name="mailing_street")),
            @AttributeOverride(name="buildingNumber", column=@Column(name="mailing_number")),
            @AttributeOverride(name="city", column=@Column(name="mailing_city")),
            @AttributeOverride(name="country", column=@Column(name="mailing_country")),
            @AttributeOverride(name="zipCode", column=@Column(name="mailing_zip"))
    })
    private Address mailingAddress;

    @JsonIgnore
    @OneToMany(mappedBy = "primaryOwner")
    private Set<Account> accountsAsPrimaryOwner;


    @JsonIgnore
    @OneToMany(mappedBy = "secondaryOwner")
    private Set<Account> accountsAsSecondaryOwner;


    // Constructor

    public AccountHolder(String name, String username, String password, LocalDate birthDate, Address primaryAddress, Address mailingAddress) {
        super(name, username, password);
        setBirthDate(birthDate);
        setPrimaryAddress(primaryAddress);
        setMailingAddress(mailingAddress);
    }

    // Getter

    // Makes mailingAddress optional
    public Optional<Address> getMailingAddress() {
        return Optional.ofNullable(mailingAddress);
    }
}
