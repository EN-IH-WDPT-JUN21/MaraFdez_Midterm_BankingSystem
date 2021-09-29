package com.ironhack.midtermproject.repository.user;

import com.ironhack.midtermproject.dao.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
