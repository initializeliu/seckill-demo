package com.xxx.seckill.config;

import com.xxx.seckill.pojo.User;

/**
 * 从线程中获取用户信息
 */
public class UserContext {

    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user){
        userHolder.set(user);
    }

    public static User getUser(){
        return userHolder.get();
    }
}
