package com.trello_server.webclosure.service;

import com.trello_server.webclosure.domain.TicketsLog;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TicketsLog}.
 */
public interface TicketsLogService {
    /**
     * Save a ticketsLog.
     *
     * @param ticketsLog the entity to save.
     * @return the persisted entity.
     */
    TicketsLog save(TicketsLog ticketsLog);

    /**
     * Updates a ticketsLog.
     *
     * @param ticketsLog the entity to update.
     * @return the persisted entity.
     */
    TicketsLog update(TicketsLog ticketsLog);

    /**
     * Partially updates a ticketsLog.
     *
     * @param ticketsLog the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TicketsLog> partialUpdate(TicketsLog ticketsLog);

    /**
     * Get all the ticketsLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TicketsLog> findAll(Pageable pageable);

    /**
     * Get the "id" ticketsLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TicketsLog> findOne(Long id);

    /**
     * Delete the "id" ticketsLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
