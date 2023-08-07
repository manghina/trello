package com.trello_server.webclosure.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TicketsLog.
 */
@Entity
@Table(name = "tickets_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketsLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "channel", nullable = false)
    private String channel;

    @NotNull
    @Column(name = "id_board", nullable = false)
    private String idBoard;

    @NotNull
    @Column(name = "id_list", nullable = false)
    private String idList;

    @NotNull
    @Column(name = "id_card", nullable = false)
    private String idCard;

    @NotNull
    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "timestamp")
    private Instant timestamp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TicketsLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannel() {
        return this.channel;
    }

    public TicketsLog channel(String channel) {
        this.setChannel(channel);
        return this;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getIdBoard() {
        return this.idBoard;
    }

    public TicketsLog idBoard(String idBoard) {
        this.setIdBoard(idBoard);
        return this;
    }

    public void setIdBoard(String idBoard) {
        this.idBoard = idBoard;
    }

    public String getIdList() {
        return this.idList;
    }

    public TicketsLog idList(String idList) {
        this.setIdList(idList);
        return this;
    }

    public void setIdList(String idList) {
        this.idList = idList;
    }

    public String getIdCard() {
        return this.idCard;
    }

    public TicketsLog idCard(String idCard) {
        this.setIdCard(idCard);
        return this;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAction() {
        return this.action;
    }

    public TicketsLog action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public TicketsLog timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketsLog)) {
            return false;
        }
        return id != null && id.equals(((TicketsLog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketsLog{" +
            "id=" + getId() +
            ", channel='" + getChannel() + "'" +
            ", idBoard='" + getIdBoard() + "'" +
            ", idList='" + getIdList() + "'" +
            ", idCard='" + getIdCard() + "'" +
            ", action='" + getAction() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
