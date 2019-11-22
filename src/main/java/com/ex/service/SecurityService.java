package com.ex.service;

/**
 * Service for Security.
 *
 * @author Zdornov Maxim
 * @version 1.0
 */

public interface SecurityService {
    String findLoggedInUsername();
    void autoLogin(String login, String password);
}
