package com.ironhack.midtermproject.service.impl.user;

import com.ironhack.midtermproject.controller.dto.TPartyTransactionDTO;
import com.ironhack.midtermproject.controller.dto.ThirdPartyDTO;
import com.ironhack.midtermproject.dao.account.Checking;
import com.ironhack.midtermproject.dao.account.Savings;
import com.ironhack.midtermproject.dao.account.StudentChecking;
import com.ironhack.midtermproject.dao.users.ThirdParty;
import com.ironhack.midtermproject.enums.TransactionType;
import com.ironhack.midtermproject.repository.TransactionRepository;
import com.ironhack.midtermproject.repository.account.CheckingRepository;
import com.ironhack.midtermproject.repository.account.SavingsRepository;
import com.ironhack.midtermproject.repository.account.StudentCheckingRepository;
import com.ironhack.midtermproject.repository.user.ThirdPartyRepository;
import com.ironhack.midtermproject.service.interfaces.user.IThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
public class ThirdPartyService implements IThirdPartyService {

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CheckingRepository checkingRepository;

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    public ThirdParty addThirdParty(@NotNull ThirdPartyDTO thirdPartyDTO) {
        ThirdParty thirdParty = new ThirdParty(thirdPartyDTO.getName(), thirdPartyDTO.getHashedKey());
        thirdPartyRepository.save(thirdParty);
        return thirdParty;
    }

    public TPartyTransactionDTO makeATransaction(@NotNull TPartyTransactionDTO transactionDTO) {
        Optional<Checking> existsCheckingAccount = checkingRepository.findById(transactionDTO.getAccountId());
        Optional<Savings> existsSavingAccount = savingsRepository.findById(transactionDTO.getAccountId());
        Optional<StudentChecking> existsStudentChecking = studentCheckingRepository.findById(transactionDTO.getAccountId());


        // The account must exist in the database
        if (existsCheckingAccount.isPresent()) {
            Checking checkingAccount = existsCheckingAccount.get();

            // The secretKey provided must match the account's secretKey
            if (checkingAccount.getSecretKey().equals(transactionDTO.getSecretKey())) {

                // The transaction type must be SEND in order to send money
                if(transactionDTO.getTransactionType() == TransactionType.SEND) {

                    checkingAccount.getBalance().increaseAmount(transactionDTO.getAmount());
                    checkingRepository.save(checkingAccount);

                    return transactionDTO;

                } else if(transactionDTO.getTransactionType() == TransactionType.RECEIVE) {

                    checkingAccount.getBalance().decreaseAmount(transactionDTO.getAmount());
                    checkingRepository.save(checkingAccount);

                    return transactionDTO;
                }

            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect secret key");
            }


        } if (existsSavingAccount.isPresent()) {
            Savings savingsAccount = existsSavingAccount.get();

            // The secretKey provided must match the account's secretKey
            if (savingsAccount.getSecretKey().equals(transactionDTO.getSecretKey())) {

                // The transaction type must be SEND in order to send money
                if(transactionDTO.getTransactionType() == TransactionType.SEND) {

                    savingsAccount.getBalance().increaseAmount(transactionDTO.getAmount());
                    savingsRepository.save(savingsAccount);

                    return transactionDTO;

                } else if(transactionDTO.getTransactionType() == TransactionType.RECEIVE) {

                    savingsAccount.getBalance().decreaseAmount(transactionDTO.getAmount());
                    savingsRepository.save(savingsAccount);

                    return transactionDTO;
                }

            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect secret key");
            }


        } if (existsStudentChecking.isPresent()) {
            StudentChecking studentChecking = existsStudentChecking.get();

            // The secretKey provided must match the account's secretKey
            if (studentChecking.getSecretKey().equals(transactionDTO.getSecretKey())) {

                // The transaction type must be SEND in order to send money
                if(transactionDTO.getTransactionType() == TransactionType.SEND) {

                    studentChecking.getBalance().increaseAmount(transactionDTO.getAmount());
                    studentCheckingRepository.save(studentChecking);

                    return transactionDTO;

                } else if(transactionDTO.getTransactionType() == TransactionType.RECEIVE) {

                    studentChecking.getBalance().decreaseAmount(transactionDTO.getAmount());
                    studentCheckingRepository.save(studentChecking);

                    return transactionDTO;
                }

            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect secret key");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Relax and try again");
    }

}
