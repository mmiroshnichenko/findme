package com.findme.helper;

import com.findme.exception.BadRequestException;

public class ArgumentHelper {
    public Long parseLongArgument(String id) throws BadRequestException {
        try {
            long paramId = Long.parseLong(id);
            if (paramId <= 0) {
                throw new BadRequestException("Error: incorrect id: " + id);
            }

            return paramId;
        } catch (NumberFormatException e) {
            throw new BadRequestException("Error: incorrect argument format");
        }
    }
}
