package com.ironhack.midtermproject.service.interfaces.account;

import com.ironhack.midtermproject.controller.dto.CCardDTO;
import com.ironhack.midtermproject.dao.account.CreditCard;

public interface ICreditCardService {

    CreditCard getCreditCardAccount(Long id);
    CreditCard createNewAccount(CCardDTO creditCardDTO);
}
