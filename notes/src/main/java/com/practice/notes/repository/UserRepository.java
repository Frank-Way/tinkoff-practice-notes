package com.practice.notes.repository;

import com.practice.notes.model.User;
//import org.apache.tomcat.jni.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.jta.UserTransactionAdapter;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByLogin(String login);
}