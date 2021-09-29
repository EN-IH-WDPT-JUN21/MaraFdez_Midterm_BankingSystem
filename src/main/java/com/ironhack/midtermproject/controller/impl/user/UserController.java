package com.ironhack.midtermproject.controller.impl.user;

import com.ironhack.midtermproject.controller.interfaces.user.IUserController;
import com.ironhack.midtermproject.dao.account.Account;
import com.ironhack.midtermproject.dao.users.User;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.service.interfaces.user.IAccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController implements IUserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IAccountHolderService accountHolderService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable("id") Long id) {
        return accountHolderService.getUser(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        userRepository.deleteById(id);
    }

}
