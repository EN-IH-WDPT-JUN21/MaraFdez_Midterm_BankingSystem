package com.ironhack.midtermproject.controller.dto;

import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class BalanceDTO {

    private Money balance;

    // Class constructor specifying amount and currency
    public BalanceDTO(BigDecimal amount, Currency currency) {
        setBalance(new Money(amount, currency, RoundingMode.HALF_EVEN));
    }
}
