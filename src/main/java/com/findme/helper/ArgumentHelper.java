package com.findme.helper;

import com.findme.exception.BadRequestException;
import com.findme.models.RelationshipStatus;
import org.springframework.stereotype.Service;

@Service
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

    public RelationshipStatus parseRelationshipStatus(String status) throws BadRequestException {
        try {
            return RelationshipStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Error: incorrect relationship status");
        }
    }
}
