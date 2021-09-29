package com.ironhack.midtermproject.controller.interfaces;

import com.ironhack.midtermproject.controller.dto.TransactionDTO;
import com.ironhack.midtermproject.dao.Transaction;

import java.util.List;
import java.util.Optional;

public interface ITransactionController {

    List<Transaction> getAllTransactions();
    Optional<Transaction> getTransaction(Long id);
    Transaction createTransaction(TransactionDTO transactionDTO);
}
