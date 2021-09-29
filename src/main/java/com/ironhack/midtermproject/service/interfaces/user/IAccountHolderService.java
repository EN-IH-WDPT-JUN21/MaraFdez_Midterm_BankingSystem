package com.ironhack.midtermproject.service.interfaces.user;

import com.ironhack.midtermproject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermproject.controller.dto.TransactionDTO;
import com.ironhack.midtermproject.dao.Transaction;
import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.dao.users.User;

public interface IAccountHolderService {

    User getUser(Long id);
    AccountHolder addAccountHolder(AccountHolderDTO accountHolderDTO);
    Transaction makeATransaction(TransactionDTO transactionDTO);

}
