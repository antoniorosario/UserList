package com.antoniorosario.geniuscodingchallenge.data;

import android.content.Context;

import com.antoniorosario.geniuscodingchallenge.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDataSource {
    private static UserDataSource userDataSource;

    private List<User> users;

    private UserDataSource(Context context) {
        users = new ArrayList<>();
    }

    public static UserDataSource get(Context context) {
        if (userDataSource == null) {
            userDataSource = new UserDataSource(context);
        }

        return userDataSource;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUser(UUID id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public void deleteUser(User user) {
        users.remove(user);
    }

}
