package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.NotFoundException;
import com.findme.helper.ArgumentHelper;
import com.findme.helper.AuthHelper;
import com.findme.models.Post;
import com.findme.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class PostController {

    private PostService postService;
    private ArgumentHelper argumentHelper;
    private AuthHelper authHelper;

    @Autowired
    public PostController(PostService postService, ArgumentHelper argumentHelper, AuthHelper authHelper) {
        this.postService = postService;
        this.argumentHelper = argumentHelper;
        this.authHelper = authHelper;
    }

    @RequestMapping(path = "/post/save", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> save(HttpSession session, @RequestBody Post post) {
        try {
            authHelper.checkAuthentication(session);
            postService.save(post);
            return new ResponseEntity<String>("ok", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/post/update", consumes = "application/json")
    public ResponseEntity<String> update(@RequestBody Post post) {
        try {
            postService.update(post);
            return new ResponseEntity<String>("ok", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/post/delete/{postId}")
    public ResponseEntity<String> delete(@PathVariable String postId) {
        try {
            postService.delete(argumentHelper.parseLongArgument(postId));
            return new ResponseEntity<String>("post deleted", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/post/{postId}", produces = "text/plain")
    public String get(Model model, @PathVariable String postId) {
        try {
            model.addAttribute("post", postService.findById(argumentHelper.parseLongArgument(postId)));
            return "post";
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/notFound";
        } catch (BadRequestException | NumberFormatException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }
}
