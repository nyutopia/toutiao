package com.ny.toutiao.model;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by ny on 2017/8/5.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users= new ThreadLocal<>();
    public User getUser(){
        return users.get();
    }
    public void setUser(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();
    }
}
