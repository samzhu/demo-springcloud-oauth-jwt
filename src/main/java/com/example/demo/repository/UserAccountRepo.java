package com.example.demo.repository;

import com.example.demo.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepo extends JpaRepository<UserAccount, String> {

    UserAccount findByAccount(String account);
}
