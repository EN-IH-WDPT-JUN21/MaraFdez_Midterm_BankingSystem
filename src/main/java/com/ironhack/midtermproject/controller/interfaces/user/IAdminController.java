package com.ironhack.midtermproject.controller.interfaces.user;

import com.ironhack.midtermproject.dao.users.Admin;
import com.ironhack.midtermproject.dao.users.User;

import java.util.List;

public interface IAdminController {

    List<Admin> getAllAdmins();
    User getAdmin(Long id);

}
