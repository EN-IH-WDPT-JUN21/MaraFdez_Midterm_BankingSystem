package com.ironhack.midtermproject.controller.impl.account;

import com.ironhack.midtermproject.controller.dto.CheckingDTO;
import com.ironhack.midtermproject.controller.interfaces.account.IStudentCheckingController;
import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.dao.account.StudentChecking;
import com.ironhack.midtermproject.repository.account.StudentCheckingRepository;
import com.ironhack.midtermproject.service.interfaces.account.IAccountService;
import com.ironhack.midtermproject.service.interfaces.account.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentCheckingController implements IStudentCheckingController{

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Autowired
    private ICheckingService checkingService;

    @Autowired
    private IAccountService accountService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentChecking> getAllStudentCheckingAccounts() {
        return studentCheckingRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getStudentCheckingAccount(@PathVariable ("id") Long id) {
        return accountService.getAccount(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        studentCheckingRepository.deleteById(id);
    }

}
