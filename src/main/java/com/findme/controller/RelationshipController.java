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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

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
    public ResponseEntity<String> addRelationship(HttpSession session, @RequestBody Relationship relationship) {
        try {
            relationshipService.addRelationship(relationship, authHelper.getAuthUser(session));

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
    public ResponseEntity<String> updateRelationship(HttpSession session, @RequestBody Relationship relationship) {
        try {
            relationshipService.updateRelationship(relationship);

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

    @RequestMapping(method = RequestMethod.GET, value = "/relationship/requests/income", produces = "text/plain")
    public String getIncomeRequests(HttpSession session, Model model) {
        try {
            List<Relationship> incomeRequests = relationshipService.getIncomeRequests(authHelper.getAuthUser(session).getId());
            model.addAttribute("incomeRequests", incomeRequests);

            return "incomeRequests";
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/notFound";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (ForbiddenException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/forbidden";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/relationship/requests/outcome", produces = "text/plain")
    public String getOutcomeRequests(HttpSession session, Model model, @PathVariable String userId) {
        try {
            List<Relationship> outcomeRequests = relationshipService.getOutcomeRequests(authHelper.getAuthUser(session).getId());
            model.addAttribute("outcomeRequests", outcomeRequests);

            return "incomeRequests";
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/notFound";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/badRequest";
        } catch (ForbiddenException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/forbidden";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors/internalError";
        }
    }
}
