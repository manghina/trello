package com.trello_server.webclosure.web.rest;

import com.trello_server.webclosure.domain.TicketsLog;
import com.trello_server.webclosure.repository.TicketsLogRepository;
import com.trello_server.webclosure.service.TicketsLogService;
import com.trello_server.webclosure.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
 * REST controller for managing {@link com.trello_server.webclosure.domain.TicketsLog}.
 */
@RestController
@RequestMapping("/api")
public class TicketsLogResource {

    private final Logger log = LoggerFactory.getLogger(TicketsLogResource.class);

    private static final String ENTITY_NAME = "ticketsLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketsLogService ticketsLogService;

    private final TicketsLogRepository ticketsLogRepository;

    public TicketsLogResource(TicketsLogService ticketsLogService, TicketsLogRepository ticketsLogRepository) {
        this.ticketsLogService = ticketsLogService;
        this.ticketsLogRepository = ticketsLogRepository;
    }

    /**
     * {@code POST  /tickets-logs} : Create a new ticketsLog.
     *
     * @param ticketsLog the ticketsLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketsLog, or with status {@code 400 (Bad Request)} if the ticketsLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tickets-logs")
    public ResponseEntity<TicketsLog> createTicketsLog(@Valid @RequestBody TicketsLog ticketsLog) throws URISyntaxException {
        log.debug("REST request to save TicketsLog : {}", ticketsLog);
        if (ticketsLog.getId() != null) {
            throw new BadRequestAlertException("A new ticketsLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TicketsLog result = ticketsLogService.save(ticketsLog);
        return ResponseEntity
            .created(new URI("/api/tickets-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tickets-logs/:id} : Updates an existing ticketsLog.
     *
     * @param id the id of the ticketsLog to save.
     * @param ticketsLog the ticketsLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketsLog,
     * or with status {@code 400 (Bad Request)} if the ticketsLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticketsLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tickets-logs/{id}")
    public ResponseEntity<TicketsLog> updateTicketsLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TicketsLog ticketsLog
    ) throws URISyntaxException {
        log.debug("REST request to update TicketsLog : {}, {}", id, ticketsLog);
        if (ticketsLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketsLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketsLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TicketsLog result = ticketsLogService.update(ticketsLog);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticketsLog.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tickets-logs/:id} : Partial updates given fields of an existing ticketsLog, field will ignore if it is null
     *
     * @param id the id of the ticketsLog to save.
     * @param ticketsLog the ticketsLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketsLog,
     * or with status {@code 400 (Bad Request)} if the ticketsLog is not valid,
     * or with status {@code 404 (Not Found)} if the ticketsLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the ticketsLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tickets-logs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TicketsLog> partialUpdateTicketsLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TicketsLog ticketsLog
    ) throws URISyntaxException {
        log.debug("REST request to partial update TicketsLog partially : {}, {}", id, ticketsLog);
        if (ticketsLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketsLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketsLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TicketsLog> result = ticketsLogService.partialUpdate(ticketsLog);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticketsLog.getId().toString())
        );
    }

    /**
     * {@code GET  /tickets-logs} : get all the ticketsLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ticketsLogs in body.
     */
    @GetMapping("/tickets-logs")
    public ResponseEntity<List<TicketsLog>> getAllTicketsLogs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TicketsLogs");
        Page<TicketsLog> page = ticketsLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tickets-logs/:id} : get the "id" ticketsLog.
     *
     * @param id the id of the ticketsLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketsLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tickets-logs/{id}")
    public ResponseEntity<TicketsLog> getTicketsLog(@PathVariable Long id) {
        log.debug("REST request to get TicketsLog : {}", id);
        Optional<TicketsLog> ticketsLog = ticketsLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ticketsLog);
    }

    /**
     * {@code DELETE  /tickets-logs/:id} : delete the "id" ticketsLog.
     *
     * @param id the id of the ticketsLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tickets-logs/{id}")
    public ResponseEntity<Void> deleteTicketsLog(@PathVariable Long id) {
        log.debug("REST request to delete TicketsLog : {}", id);
        ticketsLogService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
