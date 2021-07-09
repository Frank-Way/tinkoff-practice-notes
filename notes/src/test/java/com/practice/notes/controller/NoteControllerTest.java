package com.practice.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.notes.model.Note;
import com.practice.notes.model.User;
import com.practice.notes.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(NoteController.class)
class NoteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private NoteRepository repository;


    @Test
    public void getNoteById() throws Exception {
        Note note = new Note(1l, "one", "11111", LocalDateTime.parse("2021-07-09T10:15:30"), LocalDateTime.parse("2020-07-09T10:15:30"), 1L,true);
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(note));
        mockMvc.perform(
                get("/users/1/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("o"))
                .andExpect(jsonPath("$.body").value("11111"))
                .andExpect(jsonPath("$.s").value("2021-07-09T10:15:30"))
                .andExpect(jsonPath("$.s1").value("2021-07-09T10:15:30"))
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.status").value("true"));
    }

    @Test
    void givenNoteWhenAdd() throws Exception {
        Note note = new Note(1l, "one", "a", LocalDateTime.parse("2021-07-09T10:15:30"), LocalDateTime.parse("2020-07-09T10:15:30"), 1L,true);
        Mockito.when(repository.save(Mockito.any())).thenReturn(note);
        mockMvc.perform(
                post("/users/1/notes")
                        .content(objectMapper.writeValueAsString(note))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(note)));
    }

    @Test
    void updateNote() throws Exception {
        Note note = new Note(1l, "one", "a",LocalDateTime.parse("2021-07-09T10:15:30"), LocalDateTime.parse("2020-07-09T10:15:30"), 1L,true);
        Mockito.when(repository.save(Mockito.any())).thenReturn(note);
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(note));
        mockMvc.perform(
                put("/users/1/notes/1")
                        .content(objectMapper.writeValueAsString(new Note(1l, "one", "a",LocalDateTime.parse("2021-07-09T10:15:30"), LocalDateTime.parse("2020-07-09T10:15:30"), 1L,true)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("o"))
                .andExpect(jsonPath("$.body").value("1"))
                .andExpect(jsonPath("$.s").value("2021-07-09T10:15:30"))
                .andExpect(jsonPath("$.s1").value("2021-07-09T10:15:30"))
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.status").value("true"));
    }

    @Test
    void deleteNote() throws Exception {
        Note note = new Note(1l, "one", "a",LocalDateTime.parse("2021-07-09T10:15:30"), LocalDateTime.parse("2020-07-09T10:15:30"), 1L,true);
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(note));
        mockMvc.perform(
                delete("/users/1/notes/1"))
                .andExpect(status().isOk());
    }
}