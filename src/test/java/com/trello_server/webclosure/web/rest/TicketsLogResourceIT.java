package com.trello_server.webclosure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.trello_server.webclosure.IntegrationTest;
import com.trello_server.webclosure.domain.TicketsLog;
import com.trello_server.webclosure.repository.TicketsLogRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TicketsLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TicketsLogResourceIT {

    private static final String DEFAULT_CHANNEL = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL = "BBBBBBBBBB";

    private static final String DEFAULT_ID_BOARD = "AAAAAAAAAA";
    private static final String UPDATED_ID_BOARD = "BBBBBBBBBB";

    private static final String DEFAULT_ID_LIST = "AAAAAAAAAA";
    private static final String UPDATED_ID_LIST = "BBBBBBBBBB";

    private static final String DEFAULT_ID_CARD = "AAAAAAAAAA";
    private static final String UPDATED_ID_CARD = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tickets-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TicketsLogRepository ticketsLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketsLogMockMvc;

    private TicketsLog ticketsLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketsLog createEntity(EntityManager em) {
        TicketsLog ticketsLog = new TicketsLog()
            .channel(DEFAULT_CHANNEL)
            .idBoard(DEFAULT_ID_BOARD)
            .idList(DEFAULT_ID_LIST)
            .idCard(DEFAULT_ID_CARD)
            .action(DEFAULT_ACTION)
            .timestamp(DEFAULT_TIMESTAMP);
        return ticketsLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketsLog createUpdatedEntity(EntityManager em) {
        TicketsLog ticketsLog = new TicketsLog()
            .channel(UPDATED_CHANNEL)
            .idBoard(UPDATED_ID_BOARD)
            .idList(UPDATED_ID_LIST)
            .idCard(UPDATED_ID_CARD)
            .action(UPDATED_ACTION)
            .timestamp(UPDATED_TIMESTAMP);
        return ticketsLog;
    }

    @BeforeEach
    public void initTest() {
        ticketsLog = createEntity(em);
    }

    @Test
    @Transactional
    void createTicketsLog() throws Exception {
        int databaseSizeBeforeCreate = ticketsLogRepository.findAll().size();
        // Create the TicketsLog
        restTicketsLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketsLog)))
            .andExpect(status().isCreated());

        // Validate the TicketsLog in the database
        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeCreate + 1);
        TicketsLog testTicketsLog = ticketsLogList.get(ticketsLogList.size() - 1);
        assertThat(testTicketsLog.getChannel()).isEqualTo(DEFAULT_CHANNEL);
        assertThat(testTicketsLog.getIdBoard()).isEqualTo(DEFAULT_ID_BOARD);
        assertThat(testTicketsLog.getIdList()).isEqualTo(DEFAULT_ID_LIST);
        assertThat(testTicketsLog.getIdCard()).isEqualTo(DEFAULT_ID_CARD);
        assertThat(testTicketsLog.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testTicketsLog.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    void createTicketsLogWithExistingId() throws Exception {
        // Create the TicketsLog with an existing ID
        ticketsLog.setId(1L);

        int databaseSizeBeforeCreate = ticketsLogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketsLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketsLog)))
            .andExpect(status().isBadRequest());

        // Validate the TicketsLog in the database
        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkChannelIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketsLogRepository.findAll().size();
        // set the field null
        ticketsLog.setChannel(null);

        // Create the TicketsLog, which fails.

        restTicketsLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketsLog)))
            .andExpect(status().isBadRequest());

        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdBoardIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketsLogRepository.findAll().size();
        // set the field null
        ticketsLog.setIdBoard(null);

        // Create the TicketsLog, which fails.

        restTicketsLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketsLog)))
            .andExpect(status().isBadRequest());

        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdListIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketsLogRepository.findAll().size();
        // set the field null
        ticketsLog.setIdList(null);

        // Create the TicketsLog, which fails.

        restTicketsLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketsLog)))
            .andExpect(status().isBadRequest());

        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdCardIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketsLogRepository.findAll().size();
        // set the field null
        ticketsLog.setIdCard(null);

        // Create the TicketsLog, which fails.

        restTicketsLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketsLog)))
            .andExpect(status().isBadRequest());

        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketsLogRepository.findAll().size();
        // set the field null
        ticketsLog.setAction(null);

        // Create the TicketsLog, which fails.

        restTicketsLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketsLog)))
            .andExpect(status().isBadRequest());

        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTicketsLogs() throws Exception {
        // Initialize the database
        ticketsLogRepository.saveAndFlush(ticketsLog);

        // Get all the ticketsLogList
        restTicketsLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticketsLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL)))
            .andExpect(jsonPath("$.[*].idBoard").value(hasItem(DEFAULT_ID_BOARD)))
            .andExpect(jsonPath("$.[*].idList").value(hasItem(DEFAULT_ID_LIST)))
            .andExpect(jsonPath("$.[*].idCard").value(hasItem(DEFAULT_ID_CARD)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    void getTicketsLog() throws Exception {
        // Initialize the database
        ticketsLogRepository.saveAndFlush(ticketsLog);

        // Get the ticketsLog
        restTicketsLogMockMvc
            .perform(get(ENTITY_API_URL_ID, ticketsLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticketsLog.getId().intValue()))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL))
            .andExpect(jsonPath("$.idBoard").value(DEFAULT_ID_BOARD))
            .andExpect(jsonPath("$.idList").value(DEFAULT_ID_LIST))
            .andExpect(jsonPath("$.idCard").value(DEFAULT_ID_CARD))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTicketsLog() throws Exception {
        // Get the ticketsLog
        restTicketsLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTicketsLog() throws Exception {
        // Initialize the database
        ticketsLogRepository.saveAndFlush(ticketsLog);

        int databaseSizeBeforeUpdate = ticketsLogRepository.findAll().size();

        // Update the ticketsLog
        TicketsLog updatedTicketsLog = ticketsLogRepository.findById(ticketsLog.getId()).get();
        // Disconnect from session so that the updates on updatedTicketsLog are not directly saved in db
        em.detach(updatedTicketsLog);
        updatedTicketsLog
            .channel(UPDATED_CHANNEL)
            .idBoard(UPDATED_ID_BOARD)
            .idList(UPDATED_ID_LIST)
            .idCard(UPDATED_ID_CARD)
            .action(UPDATED_ACTION)
            .timestamp(UPDATED_TIMESTAMP);

        restTicketsLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTicketsLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTicketsLog))
            )
            .andExpect(status().isOk());

        // Validate the TicketsLog in the database
        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeUpdate);
        TicketsLog testTicketsLog = ticketsLogList.get(ticketsLogList.size() - 1);
        assertThat(testTicketsLog.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testTicketsLog.getIdBoard()).isEqualTo(UPDATED_ID_BOARD);
        assertThat(testTicketsLog.getIdList()).isEqualTo(UPDATED_ID_LIST);
        assertThat(testTicketsLog.getIdCard()).isEqualTo(UPDATED_ID_CARD);
        assertThat(testTicketsLog.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testTicketsLog.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void putNonExistingTicketsLog() throws Exception {
        int databaseSizeBeforeUpdate = ticketsLogRepository.findAll().size();
        ticketsLog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketsLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ticketsLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketsLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketsLog in the database
        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTicketsLog() throws Exception {
        int databaseSizeBeforeUpdate = ticketsLogRepository.findAll().size();
        ticketsLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketsLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketsLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketsLog in the database
        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTicketsLog() throws Exception {
        int databaseSizeBeforeUpdate = ticketsLogRepository.findAll().size();
        ticketsLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketsLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketsLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TicketsLog in the database
        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTicketsLogWithPatch() throws Exception {
        // Initialize the database
        ticketsLogRepository.saveAndFlush(ticketsLog);

        int databaseSizeBeforeUpdate = ticketsLogRepository.findAll().size();

        // Update the ticketsLog using partial update
        TicketsLog partialUpdatedTicketsLog = new TicketsLog();
        partialUpdatedTicketsLog.setId(ticketsLog.getId());

        partialUpdatedTicketsLog.idList(UPDATED_ID_LIST);

        restTicketsLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicketsLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketsLog))
            )
            .andExpect(status().isOk());

        // Validate the TicketsLog in the database
        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeUpdate);
        TicketsLog testTicketsLog = ticketsLogList.get(ticketsLogList.size() - 1);
        assertThat(testTicketsLog.getChannel()).isEqualTo(DEFAULT_CHANNEL);
        assertThat(testTicketsLog.getIdBoard()).isEqualTo(DEFAULT_ID_BOARD);
        assertThat(testTicketsLog.getIdList()).isEqualTo(UPDATED_ID_LIST);
        assertThat(testTicketsLog.getIdCard()).isEqualTo(DEFAULT_ID_CARD);
        assertThat(testTicketsLog.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testTicketsLog.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    void fullUpdateTicketsLogWithPatch() throws Exception {
        // Initialize the database
        ticketsLogRepository.saveAndFlush(ticketsLog);

        int databaseSizeBeforeUpdate = ticketsLogRepository.findAll().size();

        // Update the ticketsLog using partial update
        TicketsLog partialUpdatedTicketsLog = new TicketsLog();
        partialUpdatedTicketsLog.setId(ticketsLog.getId());

        partialUpdatedTicketsLog
            .channel(UPDATED_CHANNEL)
            .idBoard(UPDATED_ID_BOARD)
            .idList(UPDATED_ID_LIST)
            .idCard(UPDATED_ID_CARD)
            .action(UPDATED_ACTION)
            .timestamp(UPDATED_TIMESTAMP);

        restTicketsLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicketsLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketsLog))
            )
            .andExpect(status().isOk());

        // Validate the TicketsLog in the database
        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeUpdate);
        TicketsLog testTicketsLog = ticketsLogList.get(ticketsLogList.size() - 1);
        assertThat(testTicketsLog.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testTicketsLog.getIdBoard()).isEqualTo(UPDATED_ID_BOARD);
        assertThat(testTicketsLog.getIdList()).isEqualTo(UPDATED_ID_LIST);
        assertThat(testTicketsLog.getIdCard()).isEqualTo(UPDATED_ID_CARD);
        assertThat(testTicketsLog.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testTicketsLog.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void patchNonExistingTicketsLog() throws Exception {
        int databaseSizeBeforeUpdate = ticketsLogRepository.findAll().size();
        ticketsLog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketsLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ticketsLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketsLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketsLog in the database
        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTicketsLog() throws Exception {
        int databaseSizeBeforeUpdate = ticketsLogRepository.findAll().size();
        ticketsLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketsLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketsLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketsLog in the database
        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTicketsLog() throws Exception {
        int databaseSizeBeforeUpdate = ticketsLogRepository.findAll().size();
        ticketsLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketsLogMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ticketsLog))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TicketsLog in the database
        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTicketsLog() throws Exception {
        // Initialize the database
        ticketsLogRepository.saveAndFlush(ticketsLog);

        int databaseSizeBeforeDelete = ticketsLogRepository.findAll().size();

        // Delete the ticketsLog
        restTicketsLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, ticketsLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TicketsLog> ticketsLogList = ticketsLogRepository.findAll();
        assertThat(ticketsLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
