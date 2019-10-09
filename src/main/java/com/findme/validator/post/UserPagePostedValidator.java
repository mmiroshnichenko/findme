package com.findme.validator.post;

import com.findme.exception.BadRequestException;
import com.findme.models.RelationshipStatus;

public class UserPagePostedValidator extends BasePostValidator {

    @Override
    public void check(PostParams params) throws BadRequestException {
        if (!params.getPost().getUserPosted().equals(params.getPost().getUserPagePosted()) &&
                (params.getRelationship() == null || !params.getRelationship().getRelationshipStatus().equals(RelationshipStatus.CONFIRMED))) {
            throw new BadRequestException("Error: you can make post only in own page and friends page");
        }

        checkNext(params);
    }
}
