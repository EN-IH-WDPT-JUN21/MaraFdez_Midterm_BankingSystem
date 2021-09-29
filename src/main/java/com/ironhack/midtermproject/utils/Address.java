package com.ironhack.midtermproject.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Embeddable
public class Address{

    @NotNull(message = "A street must be provided")
    @NotBlank(message = "A street must be provided")
    private String street;

    @NotNull(message = "A number must be provided")
    @Positive(message = "This number cannot be negative")
    private int buildingNumber;

    @NotNull(message = "A city must be provided")
    @NotBlank(message = "A city must be provided")
    private String city;

    @NotNull(message = "A country must be provided")
    @NotBlank(message = "A country must be provided")
    private String country;

    @NotNull(message = "A zip code must be provided")
    private Integer zipCode;

}
