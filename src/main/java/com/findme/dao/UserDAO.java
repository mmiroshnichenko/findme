package com.findme.dao;


import com.findme.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class UserDAO extends BaseDAO<User> {
    public UserDAO() {
        super(User.class);
    }

    public List<User> getUserByEmailOrPhone(String email, String phone) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        List<Predicate> predicates = new ArrayList<>();

        Predicate predicateEmail = builder.equal(root.get("email"), email);
        Predicate predicatePhone = builder.equal(root.get("phone"), phone);
        predicates.add(builder.or(predicateEmail, predicatePhone));

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
