package com.findme.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostFilter {
    boolean onlyOwner;
    boolean friendsPosts;
    long authorId;
}
