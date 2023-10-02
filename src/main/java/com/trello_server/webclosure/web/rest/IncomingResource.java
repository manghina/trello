package com.trello_server.webclosure.web.rest;

import com.trello_server.webclosure.domain.Email;
import com.trello_server.webclosure.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/incoming")
@Transactional
public class IncomingResource {
    private final Logger log = LoggerFactory.getLogger(EmailResource.class);
    @Autowired
    EmailService emailService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @GetMapping("/sync")
    public ResponseEntity<Boolean> getAllEmails() {
        log.debug("REST request to get a page of Emails");
        List<Email> emails = emailService.downloadEmails();
        emailService.save(emails);
        return ResponseEntity.ok().body(new Boolean("true"));
    }


}
