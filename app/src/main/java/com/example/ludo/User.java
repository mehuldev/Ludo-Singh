package com.example.ludo;

import java.io.Serializable;

public class User implements Serializable {
    public String name, username, email, uid;
    public User(){

    }
    public User(String name, String username, String email, String uid){
        this.name = name;
        this.username = username;
        this.email = email;
        this.uid = uid;
    }
}
