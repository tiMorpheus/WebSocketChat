package com.webchat.dao;

import com.webchat.models.User;

public interface UserDao {

    boolean logIn(User user);

    boolean registration(User user) throws Exception;
}
