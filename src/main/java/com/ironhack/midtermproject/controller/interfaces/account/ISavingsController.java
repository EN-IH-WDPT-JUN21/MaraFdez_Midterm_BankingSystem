package com.ironhack.midtermproject.controller.interfaces.account;

import com.ironhack.midtermproject.controller.dto.SavingsDTO;
import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.dao.account.Savings;

import java.util.List;

public interface ISavingsController {

    List<Savings> getAllSavingsAccounts();
    Savings getSavingsAccount(Long id);
    Account createNewAccount(SavingsDTO savingsDTO);
    void delete(Long id);

}
