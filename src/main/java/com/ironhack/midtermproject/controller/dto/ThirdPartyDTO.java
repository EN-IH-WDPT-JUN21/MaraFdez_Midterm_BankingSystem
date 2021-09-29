package com.ironhack.midtermproject.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ThirdPartyDTO {

    @NotNull(message = "A name is required")
    private String name;

    private String hashedKey;

}
