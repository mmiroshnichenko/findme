package com.findme.dao;


import com.findme.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDAO extends BaseDAO<User> {
    public UserDAO() {
        super(User.class);
    }
}
