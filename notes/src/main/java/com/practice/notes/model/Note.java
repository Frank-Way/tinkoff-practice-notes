package com.practice.notes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt", "hibernateLazyInitializer", "handler"},
        allowGetters = true
)
@Table(name = "notes")
public class Note {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "notesIdSeq", sequenceName = "notes_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notesIdSeq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonIgnore
    private User user;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "body", nullable = false)
    private String body;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "status", nullable = false)
    private boolean status;

    public Note(long id, String name, String body, LocalDateTime s, LocalDateTime s1, long userId, boolean status) {
        this.id = id;
        this.name = name;
        this.body = body;
        this.createdAt = s;
        this.updatedAt = s1;
        this.status = status;
    }

    public Note(String name, String body, LocalDateTime s, LocalDateTime s1, long userId, boolean status) {
        this.name = name;
        this.body = body;
        this.createdAt = s;
        this.updatedAt = s1;
        this.status = status;
    }


    // Getters and Setters (Omitted for brevity)

    public void setUser(User value) {
        this.user = value;
    }

    public User getUser() {
        return this.user;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    public void setBody(String value) {
        this.body = value;
    }

    public String getBody() {
        return this.body;
    }

    public void setCreatedAt(LocalDateTime value) {
        this.createdAt = value;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setUpdatedAt(LocalDateTime value) {
        this.updatedAt = value;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setStatus(boolean value) {
        this.status = value;
    }

    public boolean getStatus() {
        return this.status;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long value) {
        this.id = value;
    }
}
