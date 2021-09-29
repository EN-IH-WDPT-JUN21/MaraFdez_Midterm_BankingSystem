package com.ironhack.midtermproject.service.interfaces.account;

import com.ironhack.midtermproject.controller.dto.CCardDTO;
import com.ironhack.midtermproject.controller.dto.SavingsDTO;
import com.ironhack.midtermproject.dao.account.CreditCard;
import com.ironhack.midtermproject.dao.account.Savings;

import javax.validation.constraints.NotNull;

public interface ISavingsService {

    Savings getSavingsAccount(Long id);
    Savings createNewAccount(SavingsDTO savingsDTO);
}
