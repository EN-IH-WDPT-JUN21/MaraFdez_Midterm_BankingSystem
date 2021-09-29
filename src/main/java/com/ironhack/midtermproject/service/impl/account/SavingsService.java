package com.ironhack.midtermproject.service.impl.account;

import com.ironhack.midtermproject.controller.dto.SavingsDTO;
import com.ironhack.midtermproject.dao.account.CreditCard;
import com.ironhack.midtermproject.dao.account.Savings;
import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.repository.account.SavingsRepository;
import com.ironhack.midtermproject.repository.user.AccountHolderRepository;
import com.ironhack.midtermproject.service.interfaces.account.ISavingsService;
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
public class SavingsService implements ISavingsService {

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    // When a savings account is accessed, it has to be determined if it has been one year or more since either the account
    // was created or since interest was added to the account and, if so, it has to be added the appropriate interest to the balance.
    public Savings getSavingsAccount(Long id) {
        Optional<Savings> existsSavings = savingsRepository.findById(id);

        LocalDate lastInterestApplicationDate = existsSavings.get().getLastInterestApplicationDate();
        LocalDate expectedDate = LocalDate.of(lastInterestApplicationDate.getYear() + 1,
                lastInterestApplicationDate.getMonth(), lastInterestApplicationDate.getDayOfMonth());

        if (existsSavings.isPresent()) {

            // If a year has passed since the lastInterestApplicationDate
            if (expectedDate.compareTo(LocalDate.now()) <= 0) {
                Savings savingsAccount = existsSavings.get();

                // How many years have passed since the last time the interest rate was applied
                int years = LocalDate.now().getYear() - lastInterestApplicationDate.getYear();

                // The current balance amount is multiplied by the interest rate and by the number of years
                savingsAccount.setBalance(new Money(savingsAccount.getBalance()
                        .increaseAmount(savingsAccount.getBalance().getAmount()
                                .multiply(savingsAccount.getInterestRate())
                                .multiply(new BigDecimal(years)))));

                // The moment this action is performed becomes the last time the interest rate was applied
                savingsAccount.setLastInterestApplicationDate(LocalDate.now());

                savingsRepository.save(savingsAccount);
                return savingsAccount;

            } else {
                return existsSavings.get();
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Savings Account not found");
        }
    }

    public Savings createNewAccount(@NotNull SavingsDTO savingsDTO){
        Money balance = savingsDTO.getBalance();
        AccountHolder accountHolder = accountHolderRepository.findById(savingsDTO.getPrimaryOwner()).get();
        AccountHolder secondaryOwner;
        Money minimumBalance = savingsDTO.getMinimumBalance();
        String secretKey = savingsDTO.getSecretKey();
        BigDecimal interestRate = savingsDTO.getInterestRate();

        // secondaryOwner is an optional property so it could be null
        if (savingsDTO.getSecondaryOwner() != null) {
            secondaryOwner = accountHolderRepository.findById(savingsDTO.getSecondaryOwner()).get();
        } else {
            secondaryOwner = null;
        }

        // minimumBalance and interestRate could be present, otherwise they will be set by default
        if(minimumBalance != null && interestRate != null) {
            Savings savings = new Savings(balance, accountHolder, secondaryOwner, minimumBalance, secretKey, interestRate);
            savingsRepository.save(savings);
            return savings;

        } else if(minimumBalance != null) {
            Savings savings = new Savings(balance, accountHolder, secondaryOwner, minimumBalance, secretKey);
            savingsRepository.save(savings);
            return savings;

        } else if(interestRate != null) {
            Savings savings = new Savings(balance, accountHolder, secondaryOwner, secretKey, interestRate);
            savingsRepository.save(savings);
            return savings;

        } else {
            Savings savings = new Savings(balance, accountHolder, secondaryOwner,secretKey);
            savingsRepository.save(savings);
            return savings;
        }
    }

}
