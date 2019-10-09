package com.findme.validator.post;

import com.findme.exception.BadRequestException;

import java.util.regex.Pattern;

public class MessagePostValidator extends BasePostValidator {

    public static final String URL_REGEX = "^((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";

    @Override
    public void check(PostParams params) throws BadRequestException {
        String message = params.getPost().getMessage();
        if (message.length() == 0) {
            throw new BadRequestException("Error: message of post cannot be empty");
        }

        if (message.length() > 200) {
            throw new BadRequestException("Error: message of post cannot be more than 200 symbols.");
        }

        if (Pattern.compile(URL_REGEX).matcher(message).find()) {
            throw new BadRequestException("Error: message of post cannot contains URL.");
        }

        checkNext(params);
    }
}
