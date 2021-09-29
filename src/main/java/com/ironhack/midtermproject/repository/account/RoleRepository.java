package com.ironhack.midtermproject.repository.account;

import com.ironhack.midtermproject.dao.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
