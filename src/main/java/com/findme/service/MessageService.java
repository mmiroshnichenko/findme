package com.findme.service;

import com.findme.dao.MessageDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.NotFoundException;
import com.findme.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class MessageService {
    private MessageDAO messageDAO;
    private UserService userService;

    @Autowired
    public MessageService(MessageDAO messageDAO, UserService userService) {
        this.messageDAO = messageDAO;
        this.userService = userService;
    }

    public Message save(Message message) throws Exception {
        validate(message);
        message.setDateSent(new Date());
        message.setUserFrom(userService.findById(message.getUserFrom().getId()));
        message.setUserTo(userService.findById(message.getUserTo().getId()));

        return messageDAO.save(message);
    }

    public Message update(Message message) throws Exception {
        validate(message);
        message.setUserFrom(userService.findById(message.getUserFrom().getId()));
        message.setUserTo(userService.findById(message.getUserTo().getId()));

        return messageDAO.update(message);
    }

    public void delete(long id) throws Exception {
        messageDAO.delete(findById(id));
    }

    public Message findById(long id) throws Exception{
        if (id <= 0) {
            throw new BadRequestException("Error: incorrect id: " + id);
        }
        Message message = messageDAO.findById(id);
        if (message == null) {
            throw new NotFoundException("Error: message(id: " + id + ") was not found");
        }

        return message;
    }

    private void validate(Message message) throws Exception {
        if (message.getId() != null) {
            findById(message.getId());
        }
        if (message.getText() == null || message.getText().isEmpty()) {
            throw new BadRequestException("Error: text is required");
        }
        if (message.getUserFrom() == null) {
            throw new BadRequestException("Error: user from is required");
        }
        if (message.getUserTo() == null) {
            throw new BadRequestException("Error: user to is required");
        }
    }
}
