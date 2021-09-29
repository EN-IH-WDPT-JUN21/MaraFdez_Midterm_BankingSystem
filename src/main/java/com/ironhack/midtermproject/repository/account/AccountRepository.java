package com.ironhack.midtermproject.repository.account;

import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.dao.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByPrimaryOwnerId(Long id);
    List<Account> findByPrimaryOwner(AccountHolder accountHolder);
    List<Account> findBySecondaryOwner(AccountHolder accountHolder);


}
