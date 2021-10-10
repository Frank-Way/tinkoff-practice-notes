package com.practice.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.notes.exception.*;
import com.practice.notes.model.*;
import com.practice.notes.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
public class UserControllerMockMvcIntegrationTest {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserRepository repository;
	@Autowired
	private MockMvc mockMvc;

	@AfterEach
	public void resetDb() {
		repository.deleteAll();
	}

	@Test
	public void givenUserWhenAdd() throws Exception {
		User user = new User(2L,"l","pas");
		mockMvc.perform(
				post("/users")
						.content(objectMapper.writeValueAsString(user))
						.contentType(MediaType.APPLICATION_JSON)
		)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.login").value("l"))
				.andExpect(jsonPath("$.password").value("pas"));
	}

	@Test
	public void givenIdWhenGetExistingUser() throws Exception {
		long id = createTestUser("log","pas").getId();
		mockMvc.perform(
				get("/users/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.login").value("log"))
				.andExpect(jsonPath("$.password").value("pas"));
	}

	@Test
	public void givenIdWhenGetNotExistingUser() throws Exception {
		mockMvc.perform(
				get("/users/1"))
				.andExpect(status().isNotFound())
				.andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ResourceNotFoundException.class));
	}

	@Test
	public void giveUserWhenUpdate() throws Exception {
		long id = createTestUser("log","pas").getId();
		mockMvc.perform(
				put("/users/{id}", id)
						.content(objectMapper.writeValueAsString(new User("log","pas")))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.login").value("log"))
				.andExpect(jsonPath("$.password").value("pas"));
	}

	@Test
	public void givenUserWhenDeleteUser() throws Exception {
		User user = createTestUser("log","pas");
		mockMvc.perform(
				delete("/users/{id}", user.getId()))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(user)));
	}

	private User createTestUser(String log,String password) {
		User user = new User(log,password);
		return repository.save(user);
	}
}
