package com.webchat.dao;

import com.webchat.models.User;

public interface UserDao {

    boolean loggin(User user);

    boolean registrate(User user) throws Exception;
}
