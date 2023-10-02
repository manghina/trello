package com.trello_server.webclosure.web.rest;

import com.trello_server.webclosure.domain.Email;
import com.trello_server.webclosure.repository.EmailRepository;
import com.trello_server.webclosure.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.trello_server.webclosure.domain.Email}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EmailResource {

    private final Logger log = LoggerFactory.getLogger(EmailResource.class);

    private static final String ENTITY_NAME = "email";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailRepository emailRepository;

    public EmailResource(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    /**
     * {@code POST  /emails} : Create a new email.
     *
     * @param email the email to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new email, or with status {@code 400 (Bad Request)} if the email has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/emails")
    public ResponseEntity<Email> createEmail(@Valid @RequestBody Email email) throws URISyntaxException {
        log.debug("REST request to save Email : {}", email);
        if (email.getId() != null) {
            throw new BadRequestAlertException("A new email cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Email result = emailRepository.save(email);
        return ResponseEntity
            .created(new URI("/api/emails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /emails/:id} : Updates an existing email.
     *
     * @param id the id of the email to save.
     * @param email the email to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated email,
     * or with status {@code 400 (Bad Request)} if the email is not valid,
     * or with status {@code 500 (Internal Server Error)} if the email couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/emails/{id}")
    public ResponseEntity<Email> updateEmail(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Email email)
        throws URISyntaxException {
        log.debug("REST request to update Email : {}, {}", id, email);
        if (email.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, email.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Email result = emailRepository.save(email);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, email.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /emails/:id} : Partial updates given fields of an existing email, field will ignore if it is null
     *
     * @param id the id of the email to save.
     * @param email the email to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated email,
     * or with status {@code 400 (Bad Request)} if the email is not valid,
     * or with status {@code 404 (Not Found)} if the email is not found,
     * or with status {@code 500 (Internal Server Error)} if the email couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/emails/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Email> partialUpdateEmail(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Email email
    ) throws URISyntaxException {
        log.debug("REST request to partial update Email partially : {}, {}", id, email);
        if (email.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, email.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Email> result = emailRepository
            .findById(email.getId())
            .map(existingEmail -> {
                if (email.getSubject() != null) {
                    existingEmail.setSubject(email.getSubject());
                }
                if (email.getBody() != null) {
                    existingEmail.setBody(email.getBody());
                }
                if (email.getFrom() != null) {
                    existingEmail.setFrom(email.getFrom());
                }
                if (email.getTimestamp() != null) {
                    existingEmail.setTimestamp(email.getTimestamp());
                }

                return existingEmail;
            })
            .map(emailRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, email.getId().toString())
        );
    }

    /**
     * {@code GET  /emails} : get all the emails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emails in body.
     */
    @GetMapping("/emails")
    public ResponseEntity<List<Email>> getAllEmails(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Emails");
        Page<Email> page = emailRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /emails/:id} : get the "id" email.
     *
     * @param id the id of the email to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the email, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/emails/{id}")
    public ResponseEntity<Email> getEmail(@PathVariable Long id) {
        log.debug("REST request to get Email : {}", id);
        Optional<Email> email = emailRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(email);
    }

    /**
     * {@code DELETE  /emails/:id} : delete the "id" email.
     *
     * @param id the id of the email to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/emails/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        log.debug("REST request to delete Email : {}", id);
        emailRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
