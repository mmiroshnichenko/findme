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

        ConfirmedRelationshipValidator relationshipValidator = new ConfirmedRelationshipValidator();
        CanceledRelationshipValidator canceledRelationshipValidator = new CanceledRelationshipValidator();
        RejectedRelationshipValidator rejectedRelationshipValidator = new RejectedRelationshipValidator();
        DeletedRelationshipValidator deletedRelationshipValidator = new DeletedRelationshipValidator();
        RequestedRelationshipValidator requestedRelationshipValidator = new RequestedRelationshipValidator();

        relationshipValidator.linkWith(canceledRelationshipValidator);
        canceledRelationshipValidator.linkWith(rejectedRelationshipValidator);
        rejectedRelationshipValidator.linkWith(deletedRelationshipValidator);
        deletedRelationshipValidator.linkWith(requestedRelationshipValidator);

        relationshipValidator.check(buildParamsForValidation(dbRelationship, relationship, relationship.getUserFrom()));
    }

    private void validateNewRelationship(Relationship relationship, User authUser) throws Exception {
        if (!authUser.getId().equals(relationship.getUserFrom().getId())) {
            throw new BadRequestException("Error: incorrect userFrom");
        }
        if (relationshipDAO.getExistRelationship(authUser.getId(), relationship.getUserTo().getId()) != null) {
            throw new BadRequestException("Error: active relationship already exists");
        }

        BaseRelationshipValidator relationshipValidator = new RequestedRelationshipValidator();
        relationshipValidator.check(buildParamsForValidation(null, relationship, authUser));
    }

    private RelationshipParams buildParamsForValidation(Relationship curRelationship, Relationship newRelationship, User user) {
        RelationshipParams params = new RelationshipParams();
        params.setCurrentStatus(curRelationship != null ? curRelationship.getRelationshipStatus() : RelationshipStatus.NEW);
        params.setNextStatus(newRelationship.getRelationshipStatus());
        if (curRelationship != null) {
            params.setDateModify(curRelationship.getDateModify());
        }
        params.setRequestCount(relationshipDAO.getCountOutcomeRequests(user.getId()));
        params.setFriendsCount(relationshipDAO.getCountFriends(user.getId()));

        return params;
    }
}
