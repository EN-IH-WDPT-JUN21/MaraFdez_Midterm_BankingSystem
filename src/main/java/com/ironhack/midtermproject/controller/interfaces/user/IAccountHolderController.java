package com.ironhack.midtermproject.controller.interfaces.user;

import com.ironhack.midtermproject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.dao.users.User;

import java.util.List;

public interface IAccountHolderController {

    List<AccountHolder> getAllAccountHolders();
    User getAccountHolder(Long id);
    AccountHolder addAccountHolder(AccountHolderDTO accountHolderDTO);
    void delete(Long id);

}
