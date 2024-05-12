package edu.java.domain.model.jdbc;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TgChat {
    private long id;
    private OffsetDateTime createdAt;
}
