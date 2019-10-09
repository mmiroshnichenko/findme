package com.findme.validator.post;

import com.findme.models.Post;
import com.findme.models.Relationship;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostParams {
    private Post post;
    private Relationship relationship;
}
