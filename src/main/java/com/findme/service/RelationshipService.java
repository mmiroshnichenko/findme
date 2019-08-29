package com.findme.service;

import com.findme.dao.RelationshipDAO;
import com.findme.exception.BadRequestException;
import com.findme.models.Relationship;
import com.findme.models.RelationshipStatus;
import com.findme.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelationshipService {
    private RelationshipDAO relationshipDAO;

    @Autowired
    public RelationshipService(RelationshipDAO relationshipDAO) {
        this.relationshipDAO = relationshipDAO;
    }

    public void addRelationship(Relationship relationship, User authUser) throws Exception {
        validateNewRelationship(relationship, authUser);

        relationshipDAO.save(relationship);
    }

    public void updateRelationship(Relationship relationship) throws Exception {
        validateUpdatedRelationship(relationship);

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
        if (!checkNextStatus(dbRelationship.getRelationshipStatus(), relationship.getRelationshipStatus())) {
            throw new BadRequestException("Error: incorrect relationship status");
        }
    }

    private void validateNewRelationship(Relationship relationship, User authUser) throws Exception {
        if (!authUser.getId().equals(relationship.getUserFrom().getId())) {
            throw new BadRequestException("Error: incorrect userFrom");
        }
        if (!relationship.getRelationshipStatus().equals(RelationshipStatus.REQUESTED)) {
            throw new BadRequestException("Error: incorrect status");
        }
        if (relationshipDAO.getExistRelationship(authUser.getId(), relationship.getUserTo().getId()) != null) {
            throw new BadRequestException("Error: active relationship already exists");
        }
    }

    private boolean checkNextStatus(RelationshipStatus currentStatus, RelationshipStatus nextStatus) {
        if (currentStatus.equals(RelationshipStatus.REQUESTED) &&
                (nextStatus.equals(RelationshipStatus.CANCELED) || nextStatus.equals(RelationshipStatus.REJECTED)
                        || nextStatus.equals(RelationshipStatus.CONFIRMED))) {
            return true;
        }

        if (currentStatus.equals(RelationshipStatus.CANCELED) &&
                (nextStatus.equals(RelationshipStatus.REQUESTED) || nextStatus.equals(RelationshipStatus.DELETED))) {
            return true;
        }

        if (currentStatus.equals(RelationshipStatus.REJECTED) && nextStatus.equals(RelationshipStatus.DELETED)) {
            return true;
        }

        if (currentStatus.equals(RelationshipStatus.CONFIRMED) && nextStatus.equals(RelationshipStatus.DELETED)) {
            return true;
        }

        if (currentStatus.equals(RelationshipStatus.DELETED) && nextStatus.equals(RelationshipStatus.REQUESTED)) {
            return true;
        }

        return false;
    }
}
