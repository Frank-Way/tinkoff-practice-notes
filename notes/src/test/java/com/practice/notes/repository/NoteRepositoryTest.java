package com.practice.notes.repository;

import com.practice.notes.IntegrationTestBase;
import com.practice.notes.model.Note;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;


class NoteRepositoryTest extends IntegrationTestBase {

    private static final Long ID = 1L;
    //private static final Pageable pageable = 3;

    @Autowired
    private NoteRepository noteRepository;

    @Test
    void testFindById(){
        Optional<Note> notes = noteRepository.findById(ID);
        assertTrue(notes.isPresent());
    }

//    @Test
//    void testFindPageableByUserId(){
//        Page<Note> notes = noteRepository.findPageableByUserId(ID, Pageable.unpaged());
//        MatcherAssert.assertThat(notes, IsCollectionWithSize.hasSize(1));
//    }

//    @Test
//    void testFindNoteByIdUser(){
//        List<Note> notes = noteRepository.findNoteByIdUser(ID);
//        assertThat(notes, hasSize(1));
//    }
}