package com.ironhack.midtermproject.service.impl.user;

import com.ironhack.midtermproject.dao.users.User;
import com.ironhack.midtermproject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermproject.controller.dto.BalanceDTO;
import com.ironhack.midtermproject.controller.dto.TransactionDTO;
import com.ironhack.midtermproject.dao.operation.Transaction;
import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.repository.operation.TransactionRepository;
import com.ironhack.midtermproject.repository.account.AccountRepository;
import com.ironhack.midtermproject.repository.user.AccountHolderRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.service.interfaces.account.IAccountService;
import com.ironhack.midtermproject.service.interfaces.user.IAccountHolderService;
import com.ironhack.midtermproject.utils.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountHolderService implements IAccountHolderService {

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IAccountService accountService;

    public User getUser(Long id) {
        Optional<User> existsUser = userRepository.findById(id);

        if(existsUser.isPresent()){
            return existsUser.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public AccountHolder addAccountHolder(@NotNull AccountHolderDTO accountHolderDTO) {
        if(accountHolderDTO != null) {
            AccountHolder accountHolder = new AccountHolder(
                    accountHolderDTO.getName(), accountHolderDTO.getUsername(), accountHolderDTO.getPassword(),
                    accountHolderDTO.getBirthDate(), accountHolderDTO.getPrimaryAddress(), accountHolderDTO.getMailingAddress());
            accountHolderRepository.save(accountHolder);
            return accountHolder;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid parameters. Please, check them and try again.");
        }
    }

    public Transaction makeATransaction(@NotNull TransactionDTO transactionDTO) {

        Optional<Account> existsProvenanceAccount = accountRepository.findById(transactionDTO.getProvenanceAccountId());
        Optional<Account> existsDestinationAccount = accountRepository.findById(transactionDTO.getDestinationAccountId());

        // Both accounts must exist in the database
        if (existsProvenanceAccount.isPresent() && existsDestinationAccount.isPresent()) {
            Account provenanceAccount = existsProvenanceAccount.get();
            Account destinationAccount = existsDestinationAccount.get();

            // The name provided must match the primary or secondary owner name
            if (destinationAccount.getPrimaryOwner().getName().equals(transactionDTO.getDestinationAccountHolderName()) ||
                    destinationAccount.getSecondaryOwner().get().getName().equals(transactionDTO.getDestinationAccountHolderName())) {

                // The provenanceAccount must not be frozen in order to make a transaction
                if(provenanceAccount.getStatus() == Status.ACTIVE) {

                    // Before the transaction, patterns indicating FRAUD are checked, and if so, the account status is frozen
                    List<Transaction> transactions =
                            transactionRepository.findByProvenanceAccountId(transactionDTO.getProvenanceAccountId());

                    ///// More than 2 transactions occurring on a single account within 1 second period
                    if(transactions.size() >= 2) {
                        Transaction lastTransaction = transactions.get(transactions.size() - 1);
                        int secondsBetweenTransactions = transactionDTO.getTransactionDate().getSecond()
                                - lastTransaction.getTransactionDate().getSecond();

                        if(secondsBetweenTransactions <= 1){
                            provenanceAccount.setStatus(Status.FROZEN);
                            accountRepository.save(provenanceAccount);
                            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Potential fraud detection. Please, contact an Admin");
                        }
                    }

                    ///// Transactions made in 24h total to more than 150% of the customer's highest daily total transactions in any other 24h period
                    Optional<BigDecimal> last24hTransactions = transactionRepository.findByTransactionDateLessThan24h(provenanceAccount.getId());
                    Optional<BigDecimal> highestDailyTransactions = transactionRepository.findMaxTransactions24hPeriod(provenanceAccount.getId());

                    // last24hTransaction could be empty if no other transaction took place today
                    if(last24hTransactions.isEmpty() && highestDailyTransactions.isPresent()) {
                        if (transactionDTO.getTransaction().getAmount()
                                .compareTo(highestDailyTransactions.get().multiply(new BigDecimal(1.5))) > 0) {
                            provenanceAccount.setStatus(Status.FROZEN);
                            accountRepository.save(provenanceAccount);
                            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Potential fraud detection. Please, contact an Admin");
                        }
                    } else if(last24hTransactions.isPresent() && highestDailyTransactions.isPresent()) {
                        if (last24hTransactions.get().add(transactionDTO.getTransaction().getAmount())
                                .compareTo(highestDailyTransactions.get().multiply(new BigDecimal(1.5))) > 0) {
                            provenanceAccount.setStatus(Status.FROZEN);
                            accountRepository.save(provenanceAccount);
                            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Potential fraud detection. Please, contact an Admin");
                        }
                    }

                    // The transaction should only be processed if the account has sufficient funds
                    if (provenanceAccount.getBalance().getAmount().compareTo(transactionDTO.getTransaction().getAmount()) > 0) {

                        // The updateAccountBalance method is used, in this manner we make sure that if the amount that remains
                        // in the provenanceAccount is less than its minimumBalance, the deduction of the penaltyFee is activated.
                        BalanceDTO balanceDTO = new BalanceDTO(new Money(provenanceAccount.getBalance().decreaseAmount(transactionDTO.getTransaction())));
                        accountService.updateAccountBalance(provenanceAccount.getId(), balanceDTO);

                        // As the destinationAccount will always going to add money, its balance can never be below the minimum,
                        // thus the updateAccountBalance method is not necessary here
                        destinationAccount.getBalance().increaseAmount(transactionDTO.getTransaction());
                        accountRepository.save(destinationAccount);

                        Transaction transaction = new Transaction(transactionDTO.getProvenanceAccountId(),
                                transactionDTO.getDestinationAccountId(), transactionDTO.getDestinationAccountHolderName(),
                                transactionDTO.getTransaction());
                        transactionRepository.save(transaction);
                        return transaction;

                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The provenance account has insufficient funds");
                    }

                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The provenance account is frozen. Please contact an Admin");
                }

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The account holder name is incorrect");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
    }

}
