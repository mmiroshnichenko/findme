package com.findme.validator.relationship;

import com.findme.exception.BadRequestException;
import com.findme.models.RelationshipStatus;

public class CanceledRelationshipValidator extends BaseRelationshipValidator {

    @Override
    public void check(RelationshipParams params) throws BadRequestException {
        if (params.getNextStatus().equals(RelationshipStatus.CANCELED)) {
            if (!params.getCurrentStatus().equals(RelationshipStatus.REQUESTED)) {
                throw new BadRequestException("Error: incorrect new CANCELED status for relationship");
            }
        }

        checkNext(params);
    }
}
