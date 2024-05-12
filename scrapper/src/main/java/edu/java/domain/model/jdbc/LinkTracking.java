package edu.java.domain.model.jdbc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkTracking {
    private long tgChatId;
    private long linkId;
}
