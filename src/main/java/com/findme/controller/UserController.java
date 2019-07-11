package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.models.User;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/save")
    public @ResponseBody
    String save(Model model, @RequestBody User user) {
        try {
            model.addAttribute("user", userService.save(user));
            return "profile";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/user/update", consumes = "application/json")
    public @ResponseBody
    String update(Model model, @RequestBody User user) {
        try{
            model.addAttribute("user", userService.update(user));
            return "profile";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user/delete/{userId}", produces = "text/plain")
    public @ResponseBody
    String delete(Model model, @PathVariable String userId) {
        try {
            userService.delete(Long.parseLong(userId));
            return "profileRemoved";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}")
    public String get(Model model, @PathVariable String userId) {
        try {
            model.addAttribute("user", userService.findById(Long.parseLong(userId)));
            return "profile";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }
}
