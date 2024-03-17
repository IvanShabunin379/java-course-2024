package edu.java.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(
    @Positive
    long id,
    @NotEmpty
    URI url,
    String description,
    @NotEmpty
    List<Long> tgChatIds
) {
}
