package com.ironhack.midtermproject.controller.dto;

import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CheckingDTO {

    @NotNull(message = "A specific balance and its currency are required")
    private Money balance;

    @NotNull(message = "An account holder id is required")
    private Long primaryOwner;

    private Long secondaryOwner;

    @NotNull(message = "A secret key is required")
    @NotBlank(message = "A secret key is required")
    private String secretKey;

}
