package com.BlogProject.service;

import com.BlogProject.po.User;

public interface UserService {

    User checkUser(String username, String password);

    User getUser(String username);
}
