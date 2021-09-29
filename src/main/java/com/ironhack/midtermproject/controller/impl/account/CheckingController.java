package com.ironhack.midtermproject.controller.impl.account;

import com.ironhack.midtermproject.controller.dto.CheckingDTO;
import com.ironhack.midtermproject.controller.interfaces.account.ICheckingController;
import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.dao.account.Checking;
import com.ironhack.midtermproject.repository.account.CheckingRepository;
import com.ironhack.midtermproject.service.interfaces.account.IAccountService;
import com.ironhack.midtermproject.service.interfaces.account.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/checking")
public class CheckingController implements ICheckingController {

    @Autowired
    private CheckingRepository checkingRepository;

    @Autowired
    private ICheckingService checkingService;

    @Autowired
    private IAccountService accountService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Checking> getAllCheckingAccounts() {
        return checkingRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getCheckingAccount(@PathVariable ("id") Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createNewAccount(@RequestBody @Valid CheckingDTO checkingDTO) {
        return checkingService.createNewAccount(checkingDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        checkingRepository.deleteById(id);
    }

}
