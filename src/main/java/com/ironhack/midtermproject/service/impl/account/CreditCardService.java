package com.ironhack.midtermproject.service.impl.account;

import com.ironhack.midtermproject.controller.dto.CCardDTO;
import com.ironhack.midtermproject.dao.account.Checking;
import com.ironhack.midtermproject.dao.account.CreditCard;
import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.repository.user.AccountHolderRepository;
import com.ironhack.midtermproject.repository.user.CreditCardRepository;
import com.ironhack.midtermproject.service.interfaces.account.ICreditCardService;
import com.ironhack.midtermproject.utils.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class CreditCardService implements ICreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    // When a credit card account is accessed, it has to be determined if it has been one month or more since either the account
    // was created or since interest was added to the account and, if so, it has to be added the appropriate interest to the balance.
    public CreditCard getCreditCardAccount(Long id) {
        Optional<CreditCard> existsCreditCard = creditCardRepository.findById(id);

        LocalDate lastInterestApplicationDate = existsCreditCard.get().getLastInterestApplicationDate();
        LocalDate expectedDate;
        // As a year only has 12 months, it will have a different procedure if they are months of the same year or of different ones
        if(lastInterestApplicationDate.getMonthValue()+1 > 12) {
            expectedDate = LocalDate.of(lastInterestApplicationDate.getYear()+1, 01, lastInterestApplicationDate.getDayOfMonth());
        } else {
            expectedDate = LocalDate.of(lastInterestApplicationDate.getYear(),
                    lastInterestApplicationDate.getMonthValue()+1, lastInterestApplicationDate.getDayOfMonth());
        }


        if (existsCreditCard.isPresent()) {

            // If a month has passed since the lastInterestApplicationDate
            if (expectedDate.compareTo(LocalDate.now()) <= 0) {
                CreditCard creditCardAccount = existsCreditCard.get();

                // How many months have passed since the last time the interest rate was applied
                int months;

                // As a year only has 12 months, it will have a different procedure if they are months of the same year or of different ones
                if(LocalDate.now().getYear() == lastInterestApplicationDate.getYear()) {
                    months = LocalDate.now().getMonthValue() - lastInterestApplicationDate.getMonthValue();
                } else {

                    int i = 0;

                    while(LocalDate.now().compareTo(lastInterestApplicationDate) > 0){

                        if(lastInterestApplicationDate.getMonthValue()+1 > 12) {
                            lastInterestApplicationDate = LocalDate.of(lastInterestApplicationDate.getYear()+1,
                                    1, lastInterestApplicationDate.getDayOfMonth());
                        } else {
                            lastInterestApplicationDate = LocalDate.of(lastInterestApplicationDate.getYear(),
                                    lastInterestApplicationDate.getMonthValue()+1, lastInterestApplicationDate.getDayOfMonth());
                        }
                        i++;
                    }

                    months = i;
                }

                // The current balance amount is multiplied by the interest rate and by the number of months
                creditCardAccount.setBalance(new Money(creditCardAccount.getBalance()
                        .increaseAmount(creditCardAccount.getBalance().getAmount()
                                .multiply(creditCardAccount.getInterestRate())
                                .multiply(new BigDecimal(months)))));

                // The moment this action is performed becomes the last time the interest rate was applied
                creditCardAccount.setLastInterestApplicationDate(LocalDate.now());

                creditCardRepository.save(creditCardAccount);
                return creditCardAccount;

            } else {
                return existsCreditCard.get();
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit Card Account not found");
        }
    }

    public CreditCard createNewAccount(@NotNull CCardDTO creditCardDTO) {
        Money balance = creditCardDTO.getBalance();
        AccountHolder accountHolder = accountHolderRepository.findById(creditCardDTO.getPrimaryOwner()).get();
        AccountHolder secondaryOwner;
        Money creditLimit = creditCardDTO.getCreditLimit();
        BigDecimal interestRate = creditCardDTO.getInterestRate();

        // secondaryOwner is an optional property so it could be null
        if (creditCardDTO.getSecondaryOwner() != null) {
            secondaryOwner = accountHolderRepository.findById(creditCardDTO.getSecondaryOwner()).get();
        } else {
            secondaryOwner = null;
        }

        // creditLimit and interestRate could be present, otherwise they will be set by default
        if(creditLimit != null && interestRate != null) {
            CreditCard creditCard = new CreditCard(balance, accountHolder, secondaryOwner, creditLimit, interestRate);
            creditCardRepository.save(creditCard);
            return creditCard;

        } else if(creditLimit != null) {
            CreditCard creditCard = new CreditCard(balance, accountHolder, secondaryOwner, creditLimit);
            creditCardRepository.save(creditCard);
            return creditCard;

        } else if(interestRate != null) {
            CreditCard creditCard = new CreditCard(balance, accountHolder, secondaryOwner, interestRate);
            creditCardRepository.save(creditCard);
            return creditCard;

        } else {
            CreditCard creditCard = new CreditCard(balance, accountHolder, secondaryOwner);
            creditCardRepository.save(creditCard);
            return creditCard;
        }

    }
}
