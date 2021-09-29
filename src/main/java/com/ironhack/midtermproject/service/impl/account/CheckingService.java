package com.ironhack.midtermproject.service.impl.account;

import com.ironhack.midtermproject.controller.dto.CheckingDTO;
import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.dao.account.Checking;
import com.ironhack.midtermproject.utils.Money;
import com.ironhack.midtermproject.dao.account.StudentChecking;
import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.repository.user.AccountHolderRepository;
import com.ironhack.midtermproject.repository.account.CheckingRepository;
import com.ironhack.midtermproject.repository.account.StudentCheckingRepository;
import com.ironhack.midtermproject.service.interfaces.account.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class CheckingService implements ICheckingService {

    @Autowired
    private CheckingRepository checkingRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    public Account createNewAccount(@NotNull CheckingDTO checkingDTO) {

        // When creating a new Checking account, if the primaryOwner is less than 24, a StudentChecking account should be created
        if(LocalDate.now().getYear() -
                accountHolderRepository.findById(checkingDTO.getPrimaryOwner()).get().getBirthDate().getYear() < 24) {

            Money balance = checkingDTO.getBalance();
            AccountHolder accountHolder = accountHolderRepository.findById(checkingDTO.getPrimaryOwner()).get();
            AccountHolder secondaryOwner;
            String secretKey = checkingDTO.getSecretKey();

            // secondaryOwner is an optional property so it could be null
            if(checkingDTO.getSecondaryOwner() != null) {
                secondaryOwner = accountHolderRepository.findById(checkingDTO.getSecondaryOwner()).get();
            } else {
                secondaryOwner = null;
            }

            StudentChecking studentChecking = new StudentChecking(balance, accountHolder, secondaryOwner, secretKey);
            studentCheckingRepository.save(studentChecking);
            return studentChecking;

        } else {

            Money balance = checkingDTO.getBalance();
            AccountHolder accountHolder = accountHolderRepository.findById(checkingDTO.getPrimaryOwner()).get();
            AccountHolder secondaryOwner;
            String secretKey = checkingDTO.getSecretKey();

            // secondaryOwner is an optional property so it could be null
            if(checkingDTO.getSecondaryOwner() != null) {
                secondaryOwner = accountHolderRepository.findById(checkingDTO.getSecondaryOwner()).get();
            } else {
                secondaryOwner = null;
            }

            Checking checking = new Checking(balance, accountHolder, secondaryOwner, secretKey);
            checkingRepository.save(checking);
            return checking;
        }

    }
}
