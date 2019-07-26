package com.findme.dao;


import com.findme.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UserDAO extends BaseDAO<User> {
    private static final String findUserDuplicate = "SELECT * FROM USER_FM WHERE EMAIL = ?1 OR PHONE = ?2";

    public UserDAO() {
        super(User.class);
    }

    public boolean checkUniqueEmailAndPhone(String email, String phone) {
        List<User> userList = entityManager.createNativeQuery(findUserDuplicate, User.class)
                .setParameter(1, email)
                .setParameter(2, phone)
                .getResultList();
        if (userList != null && userList.size() > 0) {
            return false;
        }
        return true;
    }
}
