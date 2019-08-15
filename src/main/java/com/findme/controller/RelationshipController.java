package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.ForbiddenException;
import com.findme.exception.NotFoundException;
import com.findme.helper.ArgumentHelper;
import com.findme.helper.AuthHelper;
import com.findme.models.Relationship;
import com.findme.models.User;
import com.findme.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class RelationshipController {
    private RelationshipService relationshipService;
    private ArgumentHelper argumentHelper;
    private AuthHelper authHelper;

    @Autowired
    public RelationshipController(RelationshipService relationshipService, ArgumentHelper argumentHelper, AuthHelper authHelper) {
        this.relationshipService = relationshipService;
        this.argumentHelper = argumentHelper;
        this.authHelper = authHelper;
    }

    @RequestMapping(path = "/relationship/add", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> addRelationoship(HttpSession session, @RequestBody Relationship relationship) {
        try {
            authHelper.checkAuthentication(session);
            relationshipService.addRelationship(relationship, (User) session.getAttribute("USER"));

            return new ResponseEntity<String>("ok", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/relationship/update", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<String> updateRelationoship(HttpSession session, @RequestBody Relationship relationship) {
        try {
            authHelper.checkAuthentication(session);
            relationshipService.updateRelationship(relationship, (User) session.getAttribute("USER"));

            return new ResponseEntity<String>("ok", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
