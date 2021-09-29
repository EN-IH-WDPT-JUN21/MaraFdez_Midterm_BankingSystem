package com.ironhack.midtermproject.repository.account;

import com.ironhack.midtermproject.dao.account.StudentChecking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCheckingRepository extends JpaRepository<StudentChecking, Long> {

}
