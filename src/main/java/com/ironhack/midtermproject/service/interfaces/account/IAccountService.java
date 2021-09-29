package com.ironhack.midtermproject.service.interfaces.account;

import com.ironhack.midtermproject.controller.dto.BalanceDTO;
import com.ironhack.midtermproject.controller.dto.StatusDTO;
import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.enums.Status;

import java.util.List;

public interface IAccountService {

    Account getAccount(Long id);
    List<Account> getAllAccountHolderAccounts(String username);
    void updateAccountBalance(Long id, BalanceDTO balance);
    void changeStatus(Long id, StatusDTO statusDTO);

}
