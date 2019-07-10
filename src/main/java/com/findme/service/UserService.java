package com.findme.service;

import com.findme.dao.UserDAO;
import com.findme.exception.BadRequestException;
import com.findme.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
    private UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User save(User user) throws Exception {
        return userDAO.save(user);
    }

    public User update(User user) throws Exception {
        User userDb = findById(user.getId());

        return userDAO.update(userDb);
    }

    public void delete(long id) throws Exception {
        userDAO.delete(null);
    }

    public User findById(long id) throws Exception{
        User user = userDAO.findById(id);
        if (user == null) {
            throw new BadRequestException("Error: user(id: " + id + ") was not found");
        }

        return user;
    }

    private void validate(User user) throws BadRequestException {

    }
}
