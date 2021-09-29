package com.ironhack.midtermproject.controller.impl.account;

import com.ironhack.midtermproject.controller.dto.BalanceDTO;
import com.ironhack.midtermproject.controller.dto.StatusDTO;
import com.ironhack.midtermproject.controller.interfaces.account.IAccountController;
import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.repository.account.AccountRepository;
import com.ironhack.midtermproject.service.interfaces.account.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AccountController implements IAccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IAccountService accountService;

    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAllAccounts() {
         return accountRepository.findAll();
    }

    @GetMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccount(@PathVariable ("id") Long id) {
        return accountService.getAccount(id);
    }

    @GetMapping("/myAccount")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAllAccountHolderAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        return accountService.getAllAccountHolderAccounts(userDetails.getUsername());
    }

    @PatchMapping("/accounts/balance/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateBalance(@PathVariable ("id") Long id, @RequestBody @Valid BalanceDTO balanceDTO){
        accountService.updateAccountBalance(id, balanceDTO);
    }

    @PatchMapping("/accounts/status/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void changeStatus(@PathVariable ("id") Long id, @RequestBody StatusDTO statusDTO){
        accountService.changeStatus(id, statusDTO);
    }

    @DeleteMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable("id") Long id){
        accountRepository.deleteById(id);
    }
}
