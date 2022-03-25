package com.ldu.jwt.repository;

import com.ldu.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
    // findby 규칙 -> username 문법
    // select * from user where username =1?
    public User findByUsername(String username);
}
