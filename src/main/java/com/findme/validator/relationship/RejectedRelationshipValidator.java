package com.findme.validator.relationship;

import com.findme.exception.BadRequestException;
import com.findme.models.RelationshipStatus;

public class RejectedRelationshipValidator extends BaseRelationshipValidator {

    @Override
    public void check(RelationshipParams params) throws BadRequestException {
        if (params.getNextStatus().equals(RelationshipStatus.REJECTED)) {
            if (!params.getCurrentStatus().equals(RelationshipStatus.REQUESTED)) {
                throw new BadRequestException("Error: incorrect new REJECTED status for relationship");
            }
        }

        checkNext(params);
    }
}
