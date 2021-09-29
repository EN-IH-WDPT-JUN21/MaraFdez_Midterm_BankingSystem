package com.ironhack.midtermproject.controller.dto;

import com.ironhack.midtermproject.utils.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class AccountHolderDTO {

    @NotNull(message = "A name must be provided")
    @NotBlank(message = "A name must be provided")
    private String name;

    @NotNull(message = "A username must be provided")
    @NotBlank(message = "A username must be provided")
    private String username;

    @NotNull(message = "A password must be provided")
    @NotBlank(message = "A password must be provided")
    private String password;

    @NotNull(message = "A date of birth is required")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull(message = "A primary address must be provided")
    private Address primaryAddress;

    private Address mailingAddress;

    // Constructor

    // mailing address is null
    public AccountHolderDTO(String name, String username, String password, LocalDate birthDate, Address primaryAddress) {
        setName(name);
        setUsername(username);
        setPassword(password);
        setBirthDate(birthDate);
        setPrimaryAddress(primaryAddress);
    }
}
