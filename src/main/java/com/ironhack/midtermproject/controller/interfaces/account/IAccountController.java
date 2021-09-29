package com.ironhack.midtermproject.controller.interfaces.account;

import com.ironhack.midtermproject.controller.dto.BalanceDTO;
import com.ironhack.midtermproject.controller.dto.StatusDTO;
import com.ironhack.midtermproject.dao.account.Account;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IAccountController {

    List<Account> getAllAccounts();
    Account getAccount(Long id);
    List<Account> getAllAccountHolderAccounts(UserDetails userDetails);
    void updateBalance(Long id, BalanceDTO balanceDTO);
    void changeStatus(Long id, StatusDTO statusDTO);
    void deleteAccount(Long id);

}
