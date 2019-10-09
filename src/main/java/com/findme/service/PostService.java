package com.findme.service;

import com.findme.dao.PostDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.NotFoundException;
import com.findme.models.Post;
import com.findme.models.Relationship;
import com.findme.validator.post.BasePostValidator;
import com.findme.validator.post.MessagePostValidator;
import com.findme.validator.post.PostParams;
import com.findme.validator.post.UserPagePostedValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class PostService {
    private PostDAO postDAO;
    private UserService userService;
    private RelationshipService relationshipService;

    @Autowired
    public PostService(PostDAO postDAO, UserService userService, RelationshipService relationshipService) {
        this.postDAO = postDAO;
        this.userService = userService;
        this.relationshipService = relationshipService;
    }

    public Post save(Post post) throws Exception {
        post.setDatePosted(new Date());
        validateNewPost(post);

        return postDAO.save(post);
    }

    public Post update(Post post) throws Exception {
        validate(post);
        post.setUserPosted(userService.findById(post.getUserPosted().getId()));

        return postDAO.update(post);
    }

    public void delete(long id) throws Exception {
        postDAO.delete(findById(id));
    }

    public Post findById(long id) throws Exception{
        Post post = postDAO.findById(id);
        if (post == null) {
            throw new NotFoundException("Error: post(id: " + id + ") was not found");
        }

        return post;
    }

    private void validateNewPost(Post post) throws Exception {
        BasePostValidator messagePostValidator = new MessagePostValidator();
        messagePostValidator.linkWith(new UserPagePostedValidator());

        messagePostValidator.check(PostParams.builder()
            .post(post)
            .relationship(relationshipService.getRelationshipBetweenUsers(post.getUserPosted().getId(), post.getUserPagePosted().getId()))
            .build()
        );
    }

    private void validate(Post post) throws Exception {
        if (post.getId() != null) {
            findById(post.getId());
        }
        if (post.getMessage() == null || post.getMessage().isEmpty()) {
            throw new BadRequestException("Error: message is required");
        }
        if (post.getUserPosted() == null) {
            throw new BadRequestException("Error: user posted is required");
        }
    }
}
