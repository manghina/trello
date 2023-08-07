package com.trello_server.webclosure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.trello_server.webclosure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TicketsLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketsLog.class);
        TicketsLog ticketsLog1 = new TicketsLog();
        ticketsLog1.setId(1L);
        TicketsLog ticketsLog2 = new TicketsLog();
        ticketsLog2.setId(ticketsLog1.getId());
        assertThat(ticketsLog1).isEqualTo(ticketsLog2);
        ticketsLog2.setId(2L);
        assertThat(ticketsLog1).isNotEqualTo(ticketsLog2);
        ticketsLog1.setId(null);
        assertThat(ticketsLog1).isNotEqualTo(ticketsLog2);
    }
}
