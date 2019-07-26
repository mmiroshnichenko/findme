package com.findme.controller;

import com.findme.exception.BadRequestException;

public class BaseController {
    protected Long parseLongArgument(String id) throws BadRequestException {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Error: incorrect argument format");
        }
    }
}
