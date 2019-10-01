package com.findme.service;

import com.findme.dao.RelationshipDAO;
import com.findme.exception.BadRequestException;
import com.findme.models.Relationship;
import com.findme.models.RelationshipStatus;
import com.findme.models.User;
import com.findme.validator.relationship.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RelationshipService {
    private RelationshipDAO relationshipDAO;

    @Autowired
    public RelationshipService(RelationshipDAO relationshipDAO) {
        this.relationshipDAO = relationshipDAO;
    }

    public void addRelationship(Relationship relationship, User authUser) throws Exception {
        validateNewRelationship(relationship, authUser);
        relationship.setDateModify(new Date());

        relationshipDAO.save(relationship);
    }

    public void updateRelationship(Relationship relationship) throws Exception {
        validateUpdatedRelationship(relationship);
        relationship.setDateModify(new Date());

        relationshipDAO.update(relationship);
    }

    public List<Relationship> getRelationshipsForUser(Long userId) {
        return relationshipDAO.getRelationshipsForUser(userId);
    }

    public List<Relationship> getIncomeRequests(Long userId) throws Exception {
        return relationshipDAO.getIncomeRequests(userId);
    }

    public List<Relationship> getOutcomeRequests(Long userId) throws Exception {
        return relationshipDAO.getOutcomeRequests(userId);
    }

    private void validateUpdatedRelationship(Relationship relationship) throws Exception {
        Relationship dbRelationship = relationshipDAO.findById(relationship.getId());
        if (!relationship.getUserFrom().getId().equals(dbRelationship.getUserFrom().getId())) {
            throw new BadRequestException("Error: incorrect userFrom");
        }
        if (!relationship.getUserTo().getId().equals(dbRelationship.getUserTo().getId())) {
            throw new BadRequestException("Error: incorrect userTo");
        }

        BaseRelationshipValidator relationshipValidator = new ConfirmedRelationshipValidator();

        relationshipValidator.linkWith(new CanceledRelationshipValidator())
                .linkWith(new RejectedRelationshipValidator())
                .linkWith(new DeletedRelationshipValidator())
                .linkWith(new RequestedRelationshipValidator());

        relationshipValidator.check(RelationshipParams.builder()
                .currentStatus(dbRelationship.getRelationshipStatus())
                .nextStatus(relationship.getRelationshipStatus())
                .dateModify(dbRelationship.getDateModify())
                .requestCount(relationshipDAO.getCountOutcomeRequests(relationship.getUserFrom().getId()))
                .friendsCount(relationshipDAO.getCountFriends(relationship.getUserFrom().getId()))
                .build()
            );
    }

    private void validateNewRelationship(Relationship relationship, User authUser) throws Exception {
        if (!authUser.getId().equals(relationship.getUserFrom().getId())) {
            throw new BadRequestException("Error: incorrect userFrom");
        }
        if (relationshipDAO.getExistRelationship(authUser.getId(), relationship.getUserTo().getId()) != null) {
            throw new BadRequestException("Error: active relationship already exists");
        }

        BaseRelationshipValidator relationshipValidator = new RequestedRelationshipValidator();

        relationshipValidator.check(RelationshipParams.builder()
                .currentStatus(RelationshipStatus.NEW)
                .nextStatus(relationship.getRelationshipStatus())
                .requestCount(relationshipDAO.getCountOutcomeRequests(relationship.getUserFrom().getId()))
                .friendsCount(relationshipDAO.getCountFriends(relationship.getUserFrom().getId()))
                .build());
    }
}
