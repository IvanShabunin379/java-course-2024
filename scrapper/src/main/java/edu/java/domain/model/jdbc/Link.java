package edu.java.domain.model.jdbc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.net.URI;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    private long id;
    private URI url;
    private OffsetDateTime lastCheckTime;
}
