package com.findme.service;

import com.findme.dao.UserDAO;
import com.findme.exception.BadRequestException;
import com.findme.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class UserService {
    private UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User save(User user) throws Exception {
        validateRegistration(user);
        user.setDateRegistered(new Date());

        return userDAO.save(user);
    }

    public User update(User user) throws Exception {
        validateUser(user);
        User userDb = findById(user.getId());
        userDb.setFirstName(user.getFirstName());
        userDb.setLastName(user.getLastName());
        userDb.setPhone(user.getPhone());
        userDb.setEmail(user.getEmail());
        userDb.setPassword(user.getPassword());
        userDb.setCountry(user.getCountry());
        userDb.setCity(user.getCity());
        userDb.setAge(user.getAge());
        userDb.setRelationshipStatus(user.getRelationshipStatus());
        userDb.setReligion(user.getReligion());
        userDb.setSchool(user.getSchool());
        userDb.setUniversity(user.getUniversity());

        return userDAO.update(userDb);
    }

    public void delete(long id) throws Exception {
        userDAO.delete(findById(id));
    }

    public User findById(long id) throws Exception{
        User user = userDAO.findById(id);
        if (user == null) {
            throw new BadRequestException("Error: user(id: " + id + ") was not found");
        }

        return user;
    }

    private void validateUser(User user) throws BadRequestException {
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            throw new BadRequestException("Error: first name is required");
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            throw new BadRequestException("Error: last name is required");
        }
        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            throw new BadRequestException("Error: phone is required");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new BadRequestException("Error: email is required");
        }
    }

    private void validateRegistration(User user) throws BadRequestException {
        validateUser(user);

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new BadRequestException("Error: password is required");
        }
        if (userDAO.getUserByEmailOrPhone(user.getEmail(), user.getPhone()).size() > 0) {
            throw new BadRequestException("Error: users with entered email or password already exist");
        }
    }
}
