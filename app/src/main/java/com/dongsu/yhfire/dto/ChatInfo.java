package com.dongsu.yhfire.dto;

public class ChatInfo {
    public String key;
    public String content;
    public String name;

    public ChatInfo(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public ChatInfo(){

    }

}
