package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.NotFoundException;
import com.findme.helper.ArgumentHelper;
import com.findme.helper.AuthHelper;
import com.findme.models.Post;
import com.findme.models.PostFilter;
import com.findme.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

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

    @RequestMapping(method = RequestMethod.GET, value = "/post/list", produces = "text/plain")
    public String getList(Model model, HttpSession session, PostFilter filter) {
        try {
            authHelper.checkAuthentication(session);
            List<Post> postList = postService.getList(authHelper.getAuthUser(session), filter);
            model.addAttribute("postList", postList);
            return "postList";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/feed", produces = "text/plain")
    public String getFeed(Model model, HttpSession session, @RequestParam(value = "start", required = false, defaultValue = "0") int start) {
        try {
            authHelper.checkAuthentication(session);
            List<Post> feed = postService.getFeed(authHelper.getAuthUser(session), start);
            model.addAttribute("feed", feed);
            return "feed";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/post/update", consumes = "application/json")
    public ResponseEntity<String> update(HttpSession session, @RequestBody Post post) {
        try {
            authHelper.checkAuthentication(session);
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
    public ResponseEntity<String> delete(HttpSession session, @PathVariable String postId) {
        try {
            authHelper.checkAuthentication(session);
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
    public String get(Model model, HttpSession session, @PathVariable String postId) {
        try {
            authHelper.checkAuthentication(session);
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
