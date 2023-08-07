package com.trello_server.webclosure.repository;

import com.trello_server.webclosure.domain.TicketsLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TicketsLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketsLogRepository extends JpaRepository<TicketsLog, Long> {}
