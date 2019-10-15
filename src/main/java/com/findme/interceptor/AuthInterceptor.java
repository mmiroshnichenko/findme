package com.findme.interceptor;

import com.findme.exception.ForbiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("USER") == null) {
            throw new ForbiddenException("Error: user is not authenticated");
        }
        return super.preHandle(request, response, handler);
    }
}
