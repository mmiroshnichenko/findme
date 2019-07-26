package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.NotFoundException;
import com.findme.models.User;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

//    @RequestMapping(method = RequestMethod.POST, value = "/user/save", consumes = "application/json", produces = "text/plain")
//    public String save(Model model, @RequestBody User user) {
//        try {
//            model.addAttribute("user", userService.save(user));
//            return "profile";
//        } catch (BadRequestException e) {
//            model.addAttribute("errorMessage", e.getMessage());
//            return "errors/badRequest";
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", e.getMessage());
//            return "errors/internalError";
//        }
//    }

    @RequestMapping(method = RequestMethod.PUT, value = "/user/update", consumes = "application/json")
    public ResponseEntity<?> update(@RequestBody User user) {
        try {
            return new ResponseEntity<User>(userService.update(user), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user/delete/{userId}")
    public ResponseEntity<String> delete(@PathVariable String userId) {
        try {
            userService.delete(Long.parseLong(userId));
            return new ResponseEntity<String>("ok", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}", produces = "text/plain")
    public String get(Model model, @PathVariable String userId) {
        try {
            model.addAttribute("user", userService.findById(Long.parseLong(userId)));
            return "profile";
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

    @RequestMapping(path = "/register-user", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@ModelAttribute User user) {
        try {
            return new ResponseEntity<User>(userService.save(user), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
