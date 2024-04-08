package edu.java.domain.model.jpa;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "links")
@Data
@NoArgsConstructor
public class LinkEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Convert(converter = URIConverter.class)
    @Column(name = "url", nullable = false, unique = true)
    private URI url;

    @Column(name = "last_checked_time", nullable = false)
    private OffsetDateTime lastCheckedTime;

    @ManyToMany(mappedBy = "tg_chats")
    List<TgChatEntity> tgChats;
}
