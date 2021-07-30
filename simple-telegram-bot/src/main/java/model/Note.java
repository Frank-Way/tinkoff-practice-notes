package model;

import java.util.Date;

public class Note {
    private Long id;

    private ServiceUser user;

    private String name;

    private String body;

    private Date createdAt;

    private Date updatedAt;

    private boolean status;

    public Note () {}

    public Note(Long id, String name, String body, Date s, Date s1, long userId, boolean status) {
        this.id = id;
        this.name = name;
        this.body = body;
        this.createdAt = s;
        this.updatedAt = s1;
        this.status = status;
    }

    public Note(String name, String body, Date s, Date s1, long userId, boolean status) {
        this.name = name;
        this.body = body;
        this.createdAt = s;
        this.updatedAt = s1;
        this.status = status;
    }

    // Getters and Setters (Omitted for brevity)

    public void setId(long value) {
        this.id = value;
    }

    public Long getId() { return this.id; }

    public void setUser(ServiceUser value) {
        this.user = value;
    }

    public ServiceUser getUser() {
        return this.user;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        if (this.name != null) return this.name;
        else return "Без заголовка";
    }

    public void setBody(String value) {
        this.body = value;
    }

    public String getBody() {
        return this.body;
    }

    public void setCreatedAt(Date value) {
        this.createdAt = value;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setUpdatedAt(Date value) {
        this.updatedAt = value;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setStatus(boolean value) {
        this.status = value;
    }

    public boolean getStatus() {
        return this.status;
    }
}