package com.findme.validator.relationship;

import com.findme.exception.BadRequestException;

import java.util.List;

public abstract class BaseRelationshipValidator {
    private BaseRelationshipValidator next;
    private List<String> errors;

    public void linkWith(BaseRelationshipValidator next) {
        this.next = next;
    }

    public abstract void check(RelationshipParams params) throws BadRequestException;

    protected void checkNext(RelationshipParams params) throws BadRequestException {
        if (next != null) {
            next.check(params);
        }
    }

    protected void addError(String error) {
        errors.add(error);
    }
}
