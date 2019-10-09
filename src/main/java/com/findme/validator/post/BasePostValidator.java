package com.findme.validator.post;

import com.findme.exception.BadRequestException;

public abstract class BasePostValidator {
    private BasePostValidator next;

    public BasePostValidator linkWith(BasePostValidator next) {
        this.next = next;

        return next;
    }

    public abstract void check(PostParams params) throws BadRequestException;

    protected void checkNext(PostParams params) throws BadRequestException {
        if (next != null) {
            next.check(params);
        }
    }
}
