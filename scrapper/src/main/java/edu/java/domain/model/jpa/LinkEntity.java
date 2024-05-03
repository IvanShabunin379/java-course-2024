package edu.java.domain.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "last_check_time", nullable = false)
    private OffsetDateTime lastCheckedTime;

    @ManyToMany(mappedBy = "links")
    List<TgChatEntity> tgChats;

    @Override
    public String toString() {
        return "LinkEntity{" +
            "id=" + id +
            ", url=" + url +
            ", lastCheckedTime=" + lastCheckedTime +
            '}';
    }
}
