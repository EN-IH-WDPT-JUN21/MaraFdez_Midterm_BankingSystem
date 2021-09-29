package com.ironhack.midtermproject.controller.impl.account;

import com.ironhack.midtermproject.controller.dto.SavingsDTO;
import com.ironhack.midtermproject.controller.interfaces.account.ISavingsController;
import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.dao.account.Savings;
import com.ironhack.midtermproject.repository.account.SavingsRepository;
import com.ironhack.midtermproject.service.interfaces.account.ISavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/savings")
public class SavingsController implements ISavingsController {

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private ISavingsService savingsService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Savings> getAllSavingsAccounts() {
        return savingsRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Savings getSavingsAccount(@PathVariable ("id") Long id) {
        return savingsService.getSavingsAccount(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createNewAccount(@RequestBody @Valid SavingsDTO savingsDTO) {
        return savingsService.createNewAccount(savingsDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        savingsRepository.deleteById(id);
    }

}
