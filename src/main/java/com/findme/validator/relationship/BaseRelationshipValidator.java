package com.findme.validator.relationship;

import com.findme.exception.BadRequestException;

import java.util.List;

public abstract class BaseRelationshipValidator {
    private BaseRelationshipValidator next;

    public BaseRelationshipValidator linkWith(BaseRelationshipValidator next) {
        this.next = next;

        return next;
    }

    public abstract void check(RelationshipParams params) throws BadRequestException;

    protected void checkNext(RelationshipParams params) throws BadRequestException {
        if (next != null) {
            next.check(params);
        }
    }
}
