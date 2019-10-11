package com.findme.dao;

import com.findme.models.Post;
import com.findme.models.PostFilter;
import com.findme.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class PostDAO extends BaseDAO<Post> {
    private static final String findFeed = "SELECT P.* FROM POST P"
            + " INNER JOIN RELATIONSHIP R ON (R.USER_FROM_ID = ?1 AND R.USER_TO_ID = P.USER_POSTED_ID) OR (R.USER_TO_ID = ?1 AND R.USER_FROM_ID = P.USER_POSTED_ID)"
            + " WHERE R.STATUS = 'CONFIRMED'"
            + " ORDER BY P.DATE_POSTED DESC";
    private static final int limitRows = 20;

    public PostDAO() {
        super(Post.class);
    }

    public List<Post> getPostsByFilter(Long authUserId, PostFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> criteriaQuery = cb.createQuery(Post.class);
        Root<Post> root = criteriaQuery.from(Post.class);
        Join<Post, User> userPostedJoin = root.join("userPosted");
        Join<Post, User> userPagePostedJoin = root.join("userPagePosted");
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(userPagePostedJoin.get("id"), authUserId));
        if (filter.isOnlyOwner()) {
            predicates.add(cb.equal(userPostedJoin.get("id"), authUserId));
        }
        if (filter.isFriendsPosts()) {
            predicates.add(cb.notEqual(userPostedJoin.get("id"), authUserId));
        }
        if (filter.getAuthorId() > 0) {
            predicates.add(cb.equal(userPostedJoin.get("id"), filter.getAuthorId()));
        }

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]))
            .orderBy(cb.desc(root.get("datePosted")));;

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<Post> getFeed(Long userId, int start) {
        return entityManager.createNativeQuery(findFeed, Post.class)
                .setParameter(1, userId)
                .setFirstResult(start)
                .setMaxResults(limitRows)
                .getResultList();
    }
}
