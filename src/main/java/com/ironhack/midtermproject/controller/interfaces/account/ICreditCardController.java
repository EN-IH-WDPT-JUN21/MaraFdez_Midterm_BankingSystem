package com.ironhack.midtermproject.controller.interfaces.account;

import com.ironhack.midtermproject.controller.dto.CCardDTO;
import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.dao.account.CreditCard;

import java.util.List;

public interface ICreditCardController {

    List<CreditCard> getAllCreditCardAccount();
    CreditCard getCreditCardAccount(Long id);
    Account createNewAccount(CCardDTO creditCardDTO);
    void delete(Long id);

}
