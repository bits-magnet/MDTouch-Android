package com.example.hsc.mdtouch;

public class Message {

    private String text;
    private String sender;
    private String createdAt;

    public Message(){

    }

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String s) {
        this.sender = s;
    }


}
