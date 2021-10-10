package model;

public class ServiceUser {

    private Long id;

    private String login;

    private String password;

    public ServiceUser() {}

    public ServiceUser(Long i, String login, String password) {
        this.id = i;
        this.login = login;
        this.password = password;
    }

    public ServiceUser(String login, String password) {
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
