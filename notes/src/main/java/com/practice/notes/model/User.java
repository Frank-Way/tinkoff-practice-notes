package com.practice.notes.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "usersIdSeq", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersIdSeq")
    private Long id;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    public  User() {}

    public User(Long i, String login, String password) {
        this.id = i;
        this.login = login;
        this.password = password;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Getters and Setters (Omitted for brevity)

    public void setId(long value) { this.id = value; }

    public Long getId() { return this.id; }

    public void setLogin(String value) { this.login = value; }

    public String getLogin() { return this.login; }

    public void setPassword(String value) { this.password = value; }

    public String getPassword() { return this.password; }
}