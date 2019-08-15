package com.findme.dao;

import com.findme.models.Relationship;
import com.findme.models.RelationshipStatus;
import com.findme.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class RelationshipDAO extends BaseDAO<Relationship> {
    private static final String FIND_EXIST_RELATIONSHIP = "SELECT * FROM RELATIONSHIP " +
            "WHERE (USER_FROM_ID = ?1 AND USER_TO_ID = ?2) OR (USER_FROM_ID = ?2 AND USER_TO_ID = ?1)";

    private static final String FIND_RELATIONSHIPS_FOR_USER = "SELECT * FROM RELATIONSHIP " +
            "WHERE USER_FROM_ID = ?1 OR USER_TO_ID = ?1";

    private static final String FIND_INCOME_REQ = "SELECT * FROM RELATIONSHIP " +
            "WHERE USER_TO_ID = ?1 AND STATUS = ?2";

    private static final String FIND_OUTCOME_REQ = "SELECT * FROM RELATIONSHIP " +
            "WHERE USER_FROM_ID = ?1 AND STATUS = ?2";

    public RelationshipDAO() {
        super(Relationship.class);
    }

    public Relationship getExistRelationship(User userFrom, User userTo) {
        try {
            Query query = entityManager.createNativeQuery(FIND_EXIST_RELATIONSHIP, Relationship.class);
            query.setParameter(1, userFrom.getId());
            query.setParameter(2, userTo.getId());

            return (Relationship) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Relationship> getRelationshipsForUser(User user) {
        Query query = entityManager.createNativeQuery(FIND_RELATIONSHIPS_FOR_USER, Relationship.class);
        query.setParameter(1, user.getId());

        return query.getResultList();
    }

    public List<Relationship> getIncomeRequests(User user) {
        Query query = entityManager.createNativeQuery(FIND_INCOME_REQ, Relationship.class);
        query.setParameter(1, user.getId());
        query.setParameter(2, RelationshipStatus.REQUESTED);

        return query.getResultList();
    }

    public List<Relationship> getOutcomeRequests(User user) {
        Query query = entityManager.createNativeQuery(FIND_OUTCOME_REQ, Relationship.class);
        query.setParameter(1, user.getId());
        query.setParameter(2, RelationshipStatus.REQUESTED);

        return query.getResultList();
    }
}
