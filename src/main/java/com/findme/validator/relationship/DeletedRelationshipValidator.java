package com.findme.validator.relationship;

import com.findme.exception.BadRequestException;
import com.findme.models.RelationshipStatus;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DeletedRelationshipValidator extends BaseRelationshipValidator {
    private final Integer minDaysAsFriends = 3;

    @Override
    public void check(RelationshipParams params) throws BadRequestException {
        if (params.getNextStatus().equals(RelationshipStatus.DELETED)) {
            if (!params.getCurrentStatus().equals(RelationshipStatus.CONFIRMED)) {
                throw new BadRequestException("Error: incorrect new DELETED status for relationship");
            }

            if (TimeUnit.DAYS.convert((new Date().getTime() - params.getDateModify().getTime()), TimeUnit.MILLISECONDS) < minDaysAsFriends) {
                throw new BadRequestException("Error: you cannot delete your friend wait " + minDaysAsFriends + " days");
            }
        }

        checkNext(params);
    }
}
