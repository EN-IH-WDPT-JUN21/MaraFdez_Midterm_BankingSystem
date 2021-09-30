package com.ironhack.midtermproject.controller.impl.operation;

import com.ironhack.midtermproject.controller.dto.TransactionDTO;
import com.ironhack.midtermproject.controller.interfaces.ITransactionController;
import com.ironhack.midtermproject.dao.operation.Transaction;
import com.ironhack.midtermproject.repository.operation.TransactionRepository;
import com.ironhack.midtermproject.service.impl.user.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class TransactionController implements ITransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountHolderService accountHolderService;

    @GetMapping("/transaction")
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @GetMapping("/transaction/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Transaction> getTransaction(@PathVariable("id") Long id) {
        return transactionRepository.findById(id);
    }

    @PostMapping("/newTransaction")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Transaction createTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        return accountHolderService.makeATransaction(transactionDTO);
    }

}
