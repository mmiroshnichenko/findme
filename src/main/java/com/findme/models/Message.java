package com.findme.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MESSAGE")
public class Message {
    private Long id;
    private String text;
    private Date dateSent;
    private Date dateRead;
    private User userFrom;
    private User userTo;

    @Id
    @SequenceGenerator(name = "MESSAGE_SEQ",sequenceName = "MESSAGE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MESSAGE_SEQ")
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    @Column(name = "TEXT")
    public String getText() {
        return text;
    }

    @Column(name = "DATE_SENT")
    public Date getDateSent() {
        return dateSent;
    }

    @Column(name = "DATE_READ")
    public Date getDateRead() {
        return dateRead;
    }

    @ManyToOne
    @JoinColumn(name = "USER_FROM_ID", nullable = false)
    public User getUserFrom() {
        return userFrom;
    }

    @ManyToOne
    @JoinColumn(name = "USER_TO_ID", nullable = false)
    public User getUserTo() {
        return userTo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public void setDateRead(Date dateRead) {
        this.dateRead = dateRead;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", dateSent=" + dateSent +
                ", dateRead=" + dateRead +
                ", userFrom=" + userFrom.getId() +
                ", userTo=" + userTo.getId() +
                '}';
    }
}
