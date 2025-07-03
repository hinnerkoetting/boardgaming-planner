package de.oetting.wwp.security;

public class LoginResponse {

    private String login;
    private String token;
    private long id;

    public LoginResponse(String login, String token, long id) {
        this.login = login;
        this.token = token;
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
