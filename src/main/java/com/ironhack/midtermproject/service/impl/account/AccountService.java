package com.ironhack.midtermproject.service.impl.account;

import com.ironhack.midtermproject.controller.dto.BalanceDTO;
import com.ironhack.midtermproject.controller.dto.StatusDTO;
import com.ironhack.midtermproject.dao.account.*;
import com.ironhack.midtermproject.repository.account.AccountRepository;
import com.ironhack.midtermproject.repository.account.CheckingRepository;
import com.ironhack.midtermproject.repository.account.SavingsRepository;
import com.ironhack.midtermproject.repository.account.StudentCheckingRepository;
import com.ironhack.midtermproject.repository.user.AccountHolderRepository;
import com.ironhack.midtermproject.repository.user.CreditCardRepository;
import com.ironhack.midtermproject.service.interfaces.account.IAccountService;
import com.ironhack.midtermproject.service.interfaces.account.ICreditCardService;
import com.ironhack.midtermproject.utils.Constants;
import com.ironhack.midtermproject.utils.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CheckingRepository checkingRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private SavingsService savingsService;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    public Account getAccount(Long id) {
        Optional<Account> existsAccount = accountRepository.findById(id);

        if(existsAccount.isPresent()){
            return existsAccount.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
    }

    // This way the accountHolder will be able to check his/her own accounts
    public List<Account> getAllAccountHolderAccounts(String username) {
        // The accountHolder must be able to verify the accounts on which he is the primary owner,
        // but also on which he is a secondary owner
        List<Account> accounts = accountRepository.findByPrimaryOwner(accountHolderRepository.findByUsername(username).get());
        List<Account> accounts2 = accountRepository.findBySecondaryOwner(accountHolderRepository.findByUsername(username).get());

        accounts.addAll(accounts2);

        if(accounts.size() != 0) {

            int i = 0;
            for(Account account : accounts) {
                Optional<Savings> existsSavings = savingsRepository.findById(account.getId());

                // In the case of Savings Account, it must be detected if the corresponding interest was added,
                // for that getSavingsAccount() method from savingsService is called
                if (existsSavings.isPresent()) {
                    Savings savings = savingsService.getSavingsAccount(existsSavings.get().getId());
                    accounts.set(i, savings);
                }
                i++;
            }

            int j = 0;
            for(Account account : accounts) {
                Optional<CreditCard> existsCreditCard = creditCardRepository.findById(account.getId());

                // In the case of Credit Card Account, it must be detected if the corresponding interest was added,
                // for that getCreditCardAccount() method from CreditCardService is called
                if (existsCreditCard.isPresent()) {
                    CreditCard creditCard = creditCardService.getCreditCardAccount(existsCreditCard.get().getId());
                    accounts.set(j, creditCard);
                }
                j++;
            }

            return accounts;

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No account found ");
        }

    }

    public void updateAccountBalance(Long id, @NotNull BalanceDTO balance) {
        Optional<Checking> existsChecking = checkingRepository.findById(id);
        Optional<Savings> existsSavings = savingsRepository.findById(id);
        Optional<Account> existsAccount = accountRepository.findById(id);

        // If the id corresponds to a CHECKING ACCOUNT
        if (existsChecking.isPresent()) {

            Checking checking = existsChecking.get();
            checking.setBalance(balance.getBalance());

            // If the account drops below the minimumBalance, the penaltyFee should be deducted from the balance automatically
            if (checking.getBalance().getAmount()
                    .compareTo(Constants.CHECKING_ACC_MIN_BALANCE) == -1) {
                checking.setBalance(new Money(checking.getBalance().decreaseAmount(Constants.PENALTY_FEE)));
            }

            checkingRepository.save(checking);

        // If the id corresponds to a SAVINGS ACCOUNT
        } else if (existsSavings.isPresent()) {

            Savings savings = existsSavings.get();
            savings.setBalance(balance.getBalance());

            // If the account drops below the minimumBalance, the penaltyFee should be deducted from the balance automatically
            if (savings.getBalance().getAmount()
                    .compareTo(Constants.SAVINGS_ACC_DEFAULT_MIN_BALANCE) == -1) {
                savings.setBalance(new Money(savings.getBalance().decreaseAmount(Constants.SAVINGS_ACC_DEFAULT_MIN_BALANCE)));
            }

            savingsRepository.save(savings);

        // If the id corresponds to any other type of ACCOUNT
        } else if (existsAccount.isPresent()) {

            Account account = existsAccount.get();
            account.setBalance(balance.getBalance());

            accountRepository.save(account);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
    }

    public void changeStatus(Long id, @NotNull StatusDTO statusDTO) {
        Optional<Account> existsAccount = accountRepository.findById(id);

        if(existsAccount.isPresent()) {
            existsAccount.get().setStatus(statusDTO.getStatus());
            accountRepository.save(existsAccount.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
    }

}

