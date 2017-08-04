package com.ny.toutiao.service;

import com.ny.toutiao.dao.UserDAO;
import com.ny.toutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ny on 2017/8/1.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    public User getUser(int id){
        return userDAO.selectaById(id);
    }
}
