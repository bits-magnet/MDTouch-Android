package com.example.hsc.mdtouch;

public class User {

    private String name;
    private String password;
    private String chatWith;

    public User(){

    }

    public User(String name, String password) {
        this.password = password;
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}