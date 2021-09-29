package com.ironhack.midtermproject.dao.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.midtermproject.dao.users.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A name must be provided")
    @NotBlank(message = "A name must be provided")
    private String name;

    @NotNull(message = "A username must be provided")
    @NotBlank(message = "A username must be provided")
    private String username;

    @NotNull(message = "A password must be provided")
    @NotBlank(message = "A password must be provided")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Role> role;


    //Constructor

    public User(String name, String username, String password) {
        setName(name);
        setUsername(username);
        setPassword(password);
    }

}
