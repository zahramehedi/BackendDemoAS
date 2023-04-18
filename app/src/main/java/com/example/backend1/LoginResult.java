package com.example.backend1;
//whenever we successfully login the server will send the name and email of
//the logged in user and we will store that data in this class

import com.google.gson.annotations.SerializedName;

public class LoginResult {

    private String name;

    private String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
