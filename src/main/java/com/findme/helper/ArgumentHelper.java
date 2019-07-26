package com.findme.helper;

import com.findme.exception.BadRequestException;

public class ArgumentHelper {
    public Long parseLongArgument(String id) throws BadRequestException {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Error: incorrect argument format");
        }
    }
}
