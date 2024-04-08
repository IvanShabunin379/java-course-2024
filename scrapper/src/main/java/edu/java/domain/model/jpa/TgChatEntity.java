package edu.java.domain.model.jpa;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "tg_chats")
@Data
@NoArgsConstructor
public class TgChatEntity {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "links_trackings",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private List<LinkEntity> links;
}
