package com.findme.helper;

import com.findme.exception.ForbiddenException;
import com.findme.models.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class AuthHelper {
    public void checkAuthentication(HttpSession session) throws ForbiddenException {
        if (session.getAttribute("USER") == null) {
            throw new ForbiddenException("Error: user is not authenticated");
        }
    }

    public User getAuthUser(HttpSession session) throws ForbiddenException {
        checkAuthentication(session);

        return (User) session.getAttribute("USER");
    }
 }
