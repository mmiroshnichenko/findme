package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.models.Message;
import com.findme.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MessageController {
    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/message/save", consumes = "application/json", produces = "text/plain")
    public @ResponseBody
    String save(Model model, @RequestBody Message message) {
        try {
            model.addAttribute("message", messageService.save(message));
            return "message";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/message/update", consumes = "application/json", produces = "text/plain")
    public @ResponseBody
    String update(Model model, @RequestBody Message message) {
        try{
            model.addAttribute("message", messageService.update(message));
            return "message";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/message/delete/{messageId}", produces = "text/plain")
    public @ResponseBody
    String delete(Model model, @PathVariable String messageId) {
        try {
            messageService.delete(Long.parseLong(messageId));
            return "messageRemoved";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/message/get/{messageId}", produces = "text/plain")
    public @ResponseBody
    String get(Model model, @PathVariable String messageId) {
        try {
            model.addAttribute("message", messageService.findById(Long.parseLong(messageId)));
            return "message";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }
}
