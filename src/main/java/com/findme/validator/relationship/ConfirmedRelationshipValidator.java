package com.findme.validator.relationship;

import com.findme.exception.BadRequestException;
import com.findme.models.RelationshipStatus;

public class ConfirmedRelationshipValidator extends BaseRelationshipValidator {
    private final Integer maxCountFriends = 100;

    @Override
    public void check(RelationshipParams params) throws BadRequestException {
        if (params.getNextStatus().equals(RelationshipStatus.CONFIRMED)) {
            if (!params.getCurrentStatus().equals(RelationshipStatus.REQUESTED)) {
                throw new BadRequestException("Error: incorrect new CONFIRMED status for relationship");
            }

            if (params.getFriendsCount() >= maxCountFriends) {
                throw new BadRequestException("Error: max friends count cannot be great that " + maxCountFriends);
            }
        }

        checkNext(params);
    }
}
