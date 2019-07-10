package com.findme.controller;

import com.findme.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {
    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(Model model, @PathVariable String userId) {
        // TODO: controller-service-DAO

        User user = new User();
        user.setFirstName("Andrey");
        user.setCity("TestCity");
        model.addAttribute("user", user);
        return "profile";
    }
}
