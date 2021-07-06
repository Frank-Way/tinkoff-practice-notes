package com.practice.notes.controller;

import com.practice.notes.exception.ResourceNotFoundException;
import com.practice.notes.model.Note;
import com.practice.notes.repository.NoteRepository;
import com.practice.notes.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
public class NoteController {

    private final UserRepository userRepository;
    private final NoteRepository noteRepository;
    public NoteController(UserRepository userRepository, NoteRepository noteRepository) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    @GetMapping("/users/{userId}/notes")
    public List<Note> getNotesByUserId(@PathVariable Long userId) {
        return noteRepository.findByUserId(userId);
    }

    @GetMapping("/users/{userId}/notes/{noteId}")
    public Note getNoteById(@PathVariable Long userId, @PathVariable Long noteId) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        return noteRepository.findById(noteId).orElseThrow(
                () -> new ResourceNotFoundException("Note not found with id " + noteId));
    }

    @PostMapping("/users/{userId}/notes")
    public Note addNote(@PathVariable Long userId,
                            @Valid @RequestBody Note note) {
        return userRepository.findById(userId)
                .map(user -> {
                    note.setUser(user);
                    return noteRepository.save(note);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @PutMapping("/users/{userId}/notes/{noteId}")
    public Note updateNote(@PathVariable Long userId,
                               @PathVariable Long noteId,
                               @Valid @RequestBody Note noteRequest) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }

        return noteRepository.findById(noteId)
                .map(note -> {
                    note.setName(noteRequest.getName());
                    note.setBody(noteRequest.getBody());
                    //note.setUpdatedAt(noteRequest.getUpdatedAt());
                    note.setStatus(noteRequest.getStatus());
                    return noteRepository.save(note);
                }).orElseThrow(() -> new ResourceNotFoundException("Note not found with id " + noteId));
    }

    @DeleteMapping("/users/{userId}/notes/{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable Long userId,
                                          @PathVariable Long noteId) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }

        return noteRepository.findById(noteId)
                .map(note -> {
                    noteRepository.delete(note);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Note not found with id " + noteId));

    }
}