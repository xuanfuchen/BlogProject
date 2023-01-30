package com.BlogProject.dao;

import com.BlogProject.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

//this class can manipulate user's table in database using JpaRepository's method
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
}
