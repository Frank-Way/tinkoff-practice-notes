package com.practice.notes.repository;

import com.practice.notes.IntegrationTestBase;
import com.practice.notes.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@Transactional
class UserRepositoryTest extends IntegrationTestBase {

    private static final Long ID = 1L;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByIdAndLogin(){
        Optional<User> user = userRepository.findById(ID);
        assertTrue(user.isEmpty());
        user.ifPresent(entity -> {
            assertEquals("log", entity.getLogin());
        });
    }

    @Test
    void findById(){
        Optional<User> user = userRepository.findById(ID);
        assertTrue(user.isPresent());
    }

//    @Test
//    void testSave(){
//        User user = User.builder()
//                .login("l")
//                .password("pas")
//                .build();
//        userRepository.save(user);
//        assertNotNull(user.getId());
//    }
}