package com.example.sqllite_basics;

public class user_model {
    String id;
    String username;
    String password;

    public user_model(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }
}
