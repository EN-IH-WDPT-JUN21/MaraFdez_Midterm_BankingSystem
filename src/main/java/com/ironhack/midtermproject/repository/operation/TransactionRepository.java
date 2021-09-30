package com.ironhack.midtermproject.repository.operation;

import com.ironhack.midtermproject.dao.operation.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByProvenanceAccountId(Long id);

    // Get the sum of the amounts of each transaction made by an account holder in a 24h period
    @Query(value = "SELECT SUM(transaction_amount) FROM Transaction " +
                        "WHERE (transaction_date >= NOW() - INTERVAL 1 DAY) " +
                        "AND provenance_account_id = :id", nativeQuery = true)
    Optional<BigDecimal> findByTransactionDateLessThan24h(@Param("id") Long id);

    // Get the highest daily total transactions amount
    // as a result of the sum of each transaction made by customers in any other 24h period
    @Query(value = "SELECT SUM(transaction_amount) " +
            "FROM transaction WHERE provenance_account_id = :id " +
            "AND transaction_date < DATE_SUB(now(), INTERVAL 24 HOUR) " +
            "GROUP BY transaction_date ORDER BY 1 DESC LIMIT 1", nativeQuery = true)
    Optional<BigDecimal> findMaxTransactions24hPeriod(@Param("id") Long id);

}
