package ru.bez_createha.queue_bot.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "queues")
public class Queue extends IdBaseEntity {

    @Column(name = "tag")
    private String tag;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "status")

    @Enumerated(EnumType.STRING)
    private QueueStatus status;

    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "queue_users",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> queue_users;

    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group groupId;


}

