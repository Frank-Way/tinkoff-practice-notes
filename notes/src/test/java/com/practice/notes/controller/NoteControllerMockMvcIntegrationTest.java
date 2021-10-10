package com.practice.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.notes.model.Note;
import com.practice.notes.repository.NoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
public class NoteControllerMockMvcIntegrationTest {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private NoteRepository repository;
	@Autowired
	private MockMvc mockMvc;

	@AfterEach
	public void resetDb() {
		repository.deleteAll();
	}

	@Test
	public void givenNoteWhenAdd() throws Exception {
		Note note = createTestNote("name", "one",LocalDateTime.parse("2021-07-09T10:15:30"), LocalDateTime.parse("2020-07-09T10:15:30"), 1L,true);
		mockMvc.perform(
				post("/users/1/notes")
						.content(objectMapper.writeValueAsString(note))
						.contentType(MediaType.APPLICATION_JSON)
		)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.name").value("o"))
				.andExpect(jsonPath("$.body").value("1"))
				.andExpect(jsonPath("$.s").value("2021-07-09T10:15:30"))
				.andExpect(jsonPath("$.s1").value("2021-07-09T10:15:30"))
				.andExpect(jsonPath("$.userId").value("1"))
				.andExpect(jsonPath("$.status").value("true"));
	}

	@Test
	public void giveNoteWhenUpdate() throws Exception {
		long id = createTestNote("one", "a",LocalDateTime.parse("2021-07-09T10:15:30"), LocalDateTime.parse("2020-07-09T10:15:30"), 1L,true).getId();
		mockMvc.perform(
				put("/users/1/notes/1", id)
						.content(objectMapper.writeValueAsString(new Note(1l, "one", "a",LocalDateTime.parse("2021-07-09T10:15:30"), LocalDateTime.parse("2020-07-09T10:15:30"), 1L,true)))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.name").value("o"))
				.andExpect(jsonPath("$.body").value("1"))
				.andExpect(jsonPath("$.s").value("2021-07-09T10:15:30"))
				.andExpect(jsonPath("$.s1").value("2021-07-09T10:15:30"))
				.andExpect(jsonPath("$.userId").value("1"))
				.andExpect(jsonPath("$.status").value("true"));
	}

	@Test
	public void givenNoteWhenDeleteNote() throws Exception {
		Note note = createTestNote("name", "one",LocalDateTime.parse("2021-07-09T10:15:30"), LocalDateTime.parse("2020-07-09T10:15:30"), 1L,true);
		mockMvc.perform(
				delete("/users/1/notes/1", note.getId()))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(note)));
	}

	private Note createTestNote(String name, String body, LocalDateTime s, LocalDateTime s1, long userId, boolean status) {
		Note note = new Note(name,body,s,s1,userId,status);
		return repository.save(note);
	}
}
