package edu.java.domain.model.jdbc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TgChat {
    private long id;
    private OffsetDateTime createdAt;
}
