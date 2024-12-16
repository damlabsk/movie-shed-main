package com.movieshed.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Entity
@Getter
@Setter
@ToString(exclude = "sender")
@Table(name = "message")
public class Message extends EntityBase {

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private MovieShedUser sender;

}
