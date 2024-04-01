package edu.java.domain.model;

import java.time.OffsetDateTime;

public record TgChat(long id, OffsetDateTime createdAt, String createdBy) {
}
