package com.ironhack.midtermproject.controller.impl.account;

import com.ironhack.midtermproject.controller.dto.CCardDTO;
import com.ironhack.midtermproject.controller.dto.CheckingDTO;
import com.ironhack.midtermproject.controller.interfaces.account.ICreditCardController;
import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.dao.account.CreditCard;
import com.ironhack.midtermproject.repository.user.CreditCardRepository;
import com.ironhack.midtermproject.service.interfaces.account.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/credit")
public class CreditCardController implements ICreditCardController {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private ICreditCardService creditCardService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCard> getAllCreditCardAccount() {
        return creditCardRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreditCard getCreditCardAccount(@PathVariable("id") Long id) {
        return creditCardService.getCreditCardAccount(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createNewAccount(@RequestBody @Valid CCardDTO creditCardDTO) {
        return creditCardService.createNewAccount(creditCardDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        creditCardRepository.deleteById(id);
    }
}
