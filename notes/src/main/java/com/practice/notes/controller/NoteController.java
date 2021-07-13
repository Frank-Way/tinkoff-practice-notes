package com.practice.notes.controller;

import com.practice.notes.exception.*;
import com.practice.notes.model.*;
import com.practice.notes.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get notes", description = "Get a list of all user's notes by user's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notes found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Note.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content) })
    @GetMapping("/users/{userId}/notes")
    public List<Note> getNotesByUserId(
            @Parameter(description = "Id of the user whose notes are to be searched") @PathVariable Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        return noteRepository.findByUserId(userId);
    }

    @Operation(summary = "Get a note", description = "Get a note by user's and its ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notes found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Note.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request provided",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Invalid reference",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User/note was not found",
                    content = @Content) })
    @GetMapping("/users/{userId}/notes/{noteId}")
    public Note getNoteById(
            @Parameter(description = "Id of the user whose note is to be searched") @PathVariable Long userId,
            @Parameter(description = "Id of the note to be searched") @PathVariable Long noteId) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        Note note = noteRepository.findById(noteId).orElseThrow(
                () -> new ResourceNotFoundException("Note not found with id " + noteId));
        if (note.getUser().getId() == userId)
            return note;
        else
            throw new WrongReferenceException("Note with id " + noteId + "is not related to user with id " + userId);
    }

    @Operation(summary = "Add a note", description = "Add a note by user's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Note.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content) })
    @PostMapping("/users/{userId}/notes")
    public Note addNote(
            @Parameter(description = "Id of the user whose note is to be added") @PathVariable Long userId,
            @Valid @RequestBody Note note) {
        return userRepository.findById(userId)
                .map(user -> {
                    note.setUser(user);
                    return noteRepository.save(note);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @Operation(summary = "Update a note", description = "Update a note by user's and its ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Note.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id provided",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Invalid reference",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User/note was not found",
                    content = @Content) })
    @PutMapping("/users/{userId}/notes/{noteId}")
    public Note updateNote(
            @Parameter(description = "Id of the user whose note is to be updated") @PathVariable Long userId,
            @Parameter(description = "Id of the note to be updated") @PathVariable Long noteId,
            @Valid @RequestBody Note noteRequest) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        Note note = noteRepository.findById(noteId).orElseThrow(
                () -> new ResourceNotFoundException("Note not found with id " + noteId));
        if (note.getUser().getId() == userId) {
            note.setName(noteRequest.getName());
            note.setBody(noteRequest.getBody());
            note.setStatus(noteRequest.getStatus());
            return noteRepository.save(note);
        }
        else
            throw new WrongReferenceException("Note with id " + noteId + "is not related to user with id " + userId);
    }

    @Operation(summary = "Delete a note", description = "Delete a note by user's and its ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Note.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id provided",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Invalid reference",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User/note was not found",
                    content = @Content) })
    @DeleteMapping("/users/{userId}/notes/{noteId}")
    public ResponseEntity<?> deleteNote(
            @Parameter(description = "Id of the user whose note is to be deleted") @PathVariable Long userId,
            @Parameter(description = "Id of the note to be deleted") @PathVariable Long noteId) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        Note note = noteRepository.findById(noteId).orElseThrow(
                () -> new ResourceNotFoundException("Note not found with id " + noteId));
        if (note.getUser().getId() == userId) {
            noteRepository.delete(note);
            return ResponseEntity.ok().build();
        }
        else
            throw new WrongReferenceException("Note with id " + noteId + "is not related to user with id " + userId);
    }
}