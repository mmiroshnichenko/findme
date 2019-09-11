package com.findme.validator.relationship;

import com.findme.exception.BadRequestException;
import com.findme.models.RelationshipStatus;

public class RequestedRelationshipValidator extends BaseRelationshipValidator {
    private final Integer maxRequestCount = 10;

    @Override
    public void check(RelationshipParams params) throws BadRequestException {
        if (params.getNextStatus().equals(RelationshipStatus.REQUESTED)) {
            if (!params.getCurrentStatus().equals(RelationshipStatus.NEW)) {
                throw new BadRequestException("Error: incorrect new REQUESTED status for relationship");
            }

            if (params.getRequestCount() >= maxRequestCount) {
                throw new BadRequestException("Error: max request to friends cannot be great that " + maxRequestCount);
            }
        }

        checkNext(params);
    }
}
