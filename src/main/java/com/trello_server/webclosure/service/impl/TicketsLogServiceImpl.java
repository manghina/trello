package com.trello_server.webclosure.service.impl;

import com.trello_server.webclosure.domain.TicketsLog;
import com.trello_server.webclosure.repository.TicketsLogRepository;
import com.trello_server.webclosure.service.TicketsLogService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TicketsLog}.
 */
@Service
@Transactional
public class TicketsLogServiceImpl implements TicketsLogService {

    private final Logger log = LoggerFactory.getLogger(TicketsLogServiceImpl.class);

    private final TicketsLogRepository ticketsLogRepository;

    public TicketsLogServiceImpl(TicketsLogRepository ticketsLogRepository) {
        this.ticketsLogRepository = ticketsLogRepository;
    }

    @Override
    public TicketsLog save(TicketsLog ticketsLog) {
        log.debug("Request to save TicketsLog : {}", ticketsLog);
        return ticketsLogRepository.save(ticketsLog);
    }

    @Override
    public TicketsLog update(TicketsLog ticketsLog) {
        log.debug("Request to update TicketsLog : {}", ticketsLog);
        return ticketsLogRepository.save(ticketsLog);
    }

    @Override
    public Optional<TicketsLog> partialUpdate(TicketsLog ticketsLog) {
        log.debug("Request to partially update TicketsLog : {}", ticketsLog);

        return ticketsLogRepository
            .findById(ticketsLog.getId())
            .map(existingTicketsLog -> {
                if (ticketsLog.getChannel() != null) {
                    existingTicketsLog.setChannel(ticketsLog.getChannel());
                }
                if (ticketsLog.getIdBoard() != null) {
                    existingTicketsLog.setIdBoard(ticketsLog.getIdBoard());
                }
                if (ticketsLog.getIdList() != null) {
                    existingTicketsLog.setIdList(ticketsLog.getIdList());
                }
                if (ticketsLog.getIdCard() != null) {
                    existingTicketsLog.setIdCard(ticketsLog.getIdCard());
                }
                if (ticketsLog.getAction() != null) {
                    existingTicketsLog.setAction(ticketsLog.getAction());
                }
                if (ticketsLog.getTimestamp() != null) {
                    existingTicketsLog.setTimestamp(ticketsLog.getTimestamp());
                }

                return existingTicketsLog;
            })
            .map(ticketsLogRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketsLog> findAll(Pageable pageable) {
        log.debug("Request to get all TicketsLogs");
        return ticketsLogRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TicketsLog> findOne(Long id) {
        log.debug("Request to get TicketsLog : {}", id);
        return ticketsLogRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TicketsLog : {}", id);
        ticketsLogRepository.deleteById(id);
    }
}
