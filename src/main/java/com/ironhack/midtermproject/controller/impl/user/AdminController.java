package com.ironhack.midtermproject.controller.impl.user;

import com.ironhack.midtermproject.controller.interfaces.user.IAdminController;
import com.ironhack.midtermproject.dao.users.Admin;
import com.ironhack.midtermproject.dao.users.User;
import com.ironhack.midtermproject.repository.user.AdminRepository;
import com.ironhack.midtermproject.service.interfaces.user.IAccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController implements IAdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private IAccountHolderService accountHolderService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getAdmin(@PathVariable("id") Long id) {
        return accountHolderService.getUser(id);
    }

}


