package edu.java.domain.model;

import java.net.URI;
import java.time.OffsetDateTime;

public record Link(long id, URI url, OffsetDateTime lastCheckTime) {
}
