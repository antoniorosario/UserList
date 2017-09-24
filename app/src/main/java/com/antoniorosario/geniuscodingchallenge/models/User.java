package com.antoniorosario.geniuscodingchallenge.models;


import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private int avatarImageResID;
    private String bio;

    public User() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvatarImageResID() {
        return avatarImageResID;
    }

    public void setAvatarImageResID(int avatarImageResID) {
        this.avatarImageResID = avatarImageResID;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
