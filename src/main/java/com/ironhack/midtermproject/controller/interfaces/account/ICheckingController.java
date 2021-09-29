package com.ironhack.midtermproject.controller.interfaces.account;

import com.ironhack.midtermproject.controller.dto.CheckingDTO;
import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.dao.account.Checking;

import java.util.List;

public interface ICheckingController {

    List<Checking> getAllCheckingAccounts();
    Account getCheckingAccount(Long id);
    Account createNewAccount(CheckingDTO checkingDTO);
    void delete(Long id);

}
