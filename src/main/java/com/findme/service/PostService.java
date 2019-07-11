package com.findme.service;

import com.findme.dao.PostDAO;
import com.findme.exception.BadRequestException;
import com.findme.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class PostService {
    private PostDAO postDAO;
    private UserService userService;

    @Autowired
    public PostService(PostDAO postDAO, UserService userService) {
        this.postDAO = postDAO;
        this.userService = userService;
    }

    public Post save(Post post) throws Exception {
        validate(post);
        post.setUserPosted(userService.findById(post.getUserPosted().getId()));
        post.setDatePosted(new Date());

        return postDAO.save(post);
    }

    public Post update(Post post) throws Exception {
        validate(post);

        Post postDb = findById(post.getId());
        postDb.setUserPosted(userService.findById(post.getUserPosted().getId()));
        postDb.setMessage(post.getMessage());

        return postDAO.update(postDb);
    }

    public void delete(long id) throws Exception {
        postDAO.delete(findById(id));
    }

    public Post findById(long id) throws Exception{
        Post post = postDAO.findById(id);
        if (post == null) {
            throw new BadRequestException("Error: post(id: " + id + ") was not found");
        }

        return post;
    }

    private void validate(Post post) throws BadRequestException {
        if (post.getMessage() == null || post.getMessage().isEmpty()) {
            throw new BadRequestException("Error: message is required");
        }
        if (post.getUserPosted() == null) {
            throw new BadRequestException("Error: user posted is required");
        }
    }
}
