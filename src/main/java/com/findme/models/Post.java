package com.findme.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "POST")
@Getter
@Setter
@ToString
public class Post {

    //TODO
    //levels permissions

    //TODO
    //comments

    @Id
    @SequenceGenerator(name = "POST_SEQ", sequenceName = "POST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "DATE_POSTED")
    private Date datePosted;

    @Column(name = "LOCATION")
    private String location;

    @ManyToOne
    @JoinColumn(name = "USER_POSTED_ID", nullable = false)
    private User userPosted;

    @ManyToOne
    @JoinColumn(name = "USER_PAGE_POSTED_ID", nullable = false)
    private User userPagePosted;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "POST_USER_TAG", joinColumns = { @JoinColumn(name = "POST_ID") },
            inverseJoinColumns = { @JoinColumn(name = "USER_ID") })
    public List<User> usersTagged;
}
