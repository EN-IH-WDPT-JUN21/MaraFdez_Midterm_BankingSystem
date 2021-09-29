package com.ironhack.midtermproject.controller.impl.user;

import com.ironhack.midtermproject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermproject.controller.dto.TransactionDTO;
import com.ironhack.midtermproject.controller.interfaces.user.IAccountHolderController;
import com.ironhack.midtermproject.dao.Transaction;
import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.dao.users.User;
import com.ironhack.midtermproject.repository.user.AccountHolderRepository;
import com.ironhack.midtermproject.service.impl.user.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/accountHolder")
public class AccountHolderController implements IAccountHolderController {

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private AccountHolderService accountHolderService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountHolder> getAllAccountHolders() {
        return accountHolderRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getAccountHolder(@PathVariable("id") Long id) {
        return accountHolderService.getUser(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder addAccountHolder(@RequestBody @Valid AccountHolderDTO accountHolderDTO) {
        return accountHolderService.addAccountHolder(accountHolderDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        accountHolderRepository.deleteById(id);
    }

}
