package com.ironhack.midtermproject.controller.interfaces.account;

import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.dao.account.StudentChecking;

import java.util.List;
import java.util.Optional;

public interface IStudentCheckingController {

    List<StudentChecking> getAllStudentCheckingAccounts();
    Account getStudentCheckingAccount(Long id);
    void delete( Long id);

}
