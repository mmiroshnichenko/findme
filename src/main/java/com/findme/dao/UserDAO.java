package com.findme.dao;

import com.findme.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.math.BigInteger;

@Repository
@Transactional
public class UserDAO extends BaseDAO<User> {
    private static final String findUserDuplicate = "SELECT COUNT(*) FROM USER_FM WHERE EMAIL = ?1 OR PHONE = ?2";

    public UserDAO() {
        super(User.class);
    }

    public int countUsersWithEmailOrPhone(String email, String phone) {
        Query query = entityManager.createNativeQuery(findUserDuplicate, User.class);
        query.setParameter(1, email);
        query.setParameter(2, phone);

        return ((BigInteger) query.getSingleResult()).intValue();
    }
}
