package com.ironhack.midtermproject.dao.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@AllArgsConstructor
@Getter
@Setter

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@DynamicUpdate
public class Admin extends User{
    // Admins can create new accounts.
    // Admins should be able to access the balance for any account and to modify it.

    // Constructor

    public Admin(String name, String username, String password) {
        super(name, username, password);
    }
}
