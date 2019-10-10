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

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
