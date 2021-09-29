package com.ironhack.midtermproject.controller.interfaces.user;

import com.ironhack.midtermproject.dao.users.User;

import java.util.List;

public interface IUserController {

    List<User> getAllUsers();
    User getUser(Long id);
    void delete(Long id);
}
