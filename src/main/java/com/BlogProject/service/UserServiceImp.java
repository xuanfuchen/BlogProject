package com.BlogProject.service;

import com.BlogProject.dao.UserRepository;
import com.BlogProject.po.User;
import com.BlogProject.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password){
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.MD5Upper(password));
        return user;
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }
}
