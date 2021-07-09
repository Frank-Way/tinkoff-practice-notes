package com.practice.notes.controller;

import com.practice.notes.exception.ResourceNotFoundException;
import com.practice.notes.model.User;
import com.practice.notes.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;

@RestController
public class UserController {

    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(summary = "Get a user", description = "Get a user by his login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid login provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content) })

    @GetMapping(path = "/users/", params = { "login" })
    public User getUserByLogin(@Parameter(description = "Login of user to be searched") @RequestParam String login) {
        return userRepository.findByLogin(login).orElseThrow(
                () -> new ResourceNotFoundException("User not found with login '" + login + "'"));
    }

    @Operation(summary = "Get users", description = "Get a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request provided",
                    content = @Content)})
    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Operation(summary = "Get a user", description = "Get a user by his Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content) })
    @GetMapping("/users/{userId}")
    public User getUserById(@Parameter(description = "Id of user to be searched") @PathVariable Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id " + userId));
    }



    @Operation(summary = "Add a user", description = "Add a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request supplied",
                    content = @Content)})
    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @Operation(summary = "Update a user", description = "Update a user by his Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content) })
    @PutMapping("/users/{userId}")
    public User updateUser(@Parameter(description = "Id of user to be updated") @PathVariable Long userId,
                                   @Valid @RequestBody User userRequest) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setLogin(userRequest.getLogin());
                    user.setPassword(userRequest.getPassword());
                    return userRepository.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @Operation(summary = "Delete a user", description = "Delete a user by his Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content) })
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "Id of user to be deleted") @PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }
}
