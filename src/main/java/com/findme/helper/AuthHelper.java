package com.findme.helper;

import com.findme.exception.BadRequestException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class AuthHelper {
    public void checkAuthentication(HttpSession session) throws BadRequestException {
        if (session.getAttribute("USER") == null) {
            throw new BadRequestException("Error: user is not authenticated");
        }
    }
}
