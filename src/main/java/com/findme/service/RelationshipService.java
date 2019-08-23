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
    private Map<RelationshipStatus, List<RelationshipStatus>> nextStatuses;

    @Autowired
    public RelationshipService(RelationshipDAO relationshipDAO) {
        this.relationshipDAO = relationshipDAO;

        initNextStatuses();
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
        if (!nextStatuses.get(dbRelationship.getRelationshipStatus()).contains(relationship.getRelationshipStatus())) {
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

    private void initNextStatuses() {
        nextStatuses = new HashMap<>();

        List<RelationshipStatus> nextRequestedStatuses = new ArrayList<>();
        nextRequestedStatuses.add(RelationshipStatus.CANCELED);
        nextRequestedStatuses.add(RelationshipStatus.CONFIRMED);
        nextRequestedStatuses.add(RelationshipStatus.REJECTED);
        nextStatuses.put(RelationshipStatus.REQUESTED, nextRequestedStatuses);

        List<RelationshipStatus> nextCanceledStatuses = new ArrayList<>();
        nextCanceledStatuses.add(RelationshipStatus.REQUESTED);
        nextCanceledStatuses.add(RelationshipStatus.DELETED);
        nextStatuses.put(RelationshipStatus.CANCELED, nextCanceledStatuses);

        List<RelationshipStatus> nextRejectedStatuses = new ArrayList<>();
        nextRejectedStatuses.add(RelationshipStatus.DELETED);
        nextStatuses.put(RelationshipStatus.REJECTED, nextRejectedStatuses);

        List<RelationshipStatus> nextConfirmedStatuses = new ArrayList<>();
        nextConfirmedStatuses.add(RelationshipStatus.DELETED);
        nextStatuses.put(RelationshipStatus.CONFIRMED, nextConfirmedStatuses);

        List<RelationshipStatus> nextDeletedStatuses = new ArrayList<>();
        nextDeletedStatuses.add(RelationshipStatus.REQUESTED);
        nextStatuses.put(RelationshipStatus.DELETED, nextDeletedStatuses);
    }
}
