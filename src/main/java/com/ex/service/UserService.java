package com.ex.service;
import com.ex.entity.User;

/**
 * interface UserService
 *
 * @author Zdornov Maxim
 * @version 1.0
 */

public interface UserService {
    void save(User user);
    User findByUsername(String username);
    void update(User user);
}
