package com.practice.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.notes.exception.ResourceNotFoundException;
import com.practice.notes.model.User;
import com.practice.notes.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
public class UserControllerMockMvcUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserRepository repository;

    @Test
    void givenUserWhenAdd() throws Exception {
        User user = new User(2l, "login", "password");
        Mockito.when(repository.save(Mockito.any())).thenReturn(user);
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void givenIdWhenGetExistingUser() throws Exception {
        User user = new User(2l, "login", "password");
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(user));
        mockMvc.perform(
                get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.login").value("login"))
                .andExpect(jsonPath("$.password").value("password"));
    }

    @Test
    public void givenIdWhenGetNotExistingUser() throws Exception {
        Mockito.when(repository.findById(Mockito.any())).
                thenReturn(Optional.empty());
        mockMvc.perform(
                get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ResourceNotFoundException.class));
    }

    @Test
    public void giveUserWhenUpdate() throws Exception {
        User user = new User(2l, "login", "password");
        Mockito.when(repository.save(Mockito.any())).thenReturn(user);
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(user));
        mockMvc.perform(
                put("/users/2")
                        .content(objectMapper.writeValueAsString(new User(2l,"login","password")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.login").value("login"))
                .andExpect(jsonPath("$.password").value("password"));
    }

    @Test
    public void givenUserWhenDeleteUser() throws Exception {
        User user = new User(1l,"log", "pass");
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(user));
        mockMvc.perform(
                delete("/users/1"))
                .andExpect(status().isOk());
    }

}