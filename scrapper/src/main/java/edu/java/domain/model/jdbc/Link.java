package edu.java.domain.model.jdbc;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    private long id;
    private URI url;
    private OffsetDateTime lastCheckTime;
}
