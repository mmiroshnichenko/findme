package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.models.Post;
import com.findme.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/post/save", produces = "text/plain")
    public @ResponseBody
    String save(Model model, @RequestBody Post post) {
        try {
            model.addAttribute("post", postService.save(post));
            return "post";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/post/update", produces = "text/plain")
    public @ResponseBody
    String update(Model model, @RequestBody Post post) {
        try{
            model.addAttribute("post", postService.update(post));
            return "post";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/post/delete/{postId}", produces = "text/plain")
    public @ResponseBody
    String delete(Model model, @PathVariable String postId) {
        try {
            postService.delete(Long.parseLong(postId));
            return "postRemoved";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/post/{postId}", produces = "text/plain")
    public @ResponseBody
    String get(Model model, @PathVariable String postId) {
        try {
            model.addAttribute("user", postService.findById(Long.parseLong(postId)));
            return "post";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }
}
