package de.oetting.bgp.security;

public class LoginRequest {

    private String login;
    private String password;

    public LoginRequest(String email, String password) {
        this.login = email;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
