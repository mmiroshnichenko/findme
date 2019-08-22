package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.ForbiddenException;
import com.findme.exception.NotFoundException;
import com.findme.helper.ArgumentHelper;
import com.findme.helper.AuthHelper;
import com.findme.models.User;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private UserService userService;
    private ArgumentHelper argumentHelper;
    private AuthHelper authHelper;

    @Autowired
    public UserController(UserService userService, ArgumentHelper argumentHelper, AuthHelper authHelper) {
        this.userService = userService;
        this.argumentHelper = argumentHelper;
        this.authHelper = authHelper;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/user/update", consumes = "application/json")
    public ResponseEntity<String> update(HttpSession session, @RequestBody User user) {
        try {
            authHelper.checkAuthentication(session);
            userService.update(user);
            return new ResponseEntity<String>("ok", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user/delete/{userId}")
    public ResponseEntity<String> delete(HttpSession session, @PathVariable String userId) {
        try {
            authHelper.checkAuthentication(session);
            userService.delete(argumentHelper.parseLongArgument(userId));
            return new ResponseEntity<String>("ok", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}", produces = "text/plain")
    public String get(HttpSession session, Model model, @PathVariable String userId) {
        try {
            authHelper.checkAuthentication(session);
            model.addAttribute("user", userService.findById(argumentHelper.parseLongArgument(userId)));
            return "profile";
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/notFound";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (ForbiddenException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/forbidden";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }

    @RequestMapping(path = "/register-user", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@ModelAttribute User user) {
        try {
            userService.save(user);
            return new ResponseEntity<String>("ok", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String home() {
        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> loginUser(HttpSession session, HttpServletRequest request) {
        try {
            User user = userService.login(request.getParameter("email"), request.getParameter("password"));
            session.setAttribute("USER", user);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/user/" + user.getId());
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public ResponseEntity<String> logout(HttpSession session) {
        try {
            authHelper.checkAuthentication(session);
            session.invalidate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/login");
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (ForbiddenException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
