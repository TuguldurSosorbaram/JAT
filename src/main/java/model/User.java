package model;

public class User {
    private String username;
    private String password;  // For simplicity, not encrypted yet

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
