package com.ironhack.midtermproject.service.interfaces.account;

import com.ironhack.midtermproject.controller.dto.CheckingDTO;
import com.ironhack.midtermproject.dao.account.Account;

public interface ICheckingService {

    Account createNewAccount(CheckingDTO checkingDTO);

}
