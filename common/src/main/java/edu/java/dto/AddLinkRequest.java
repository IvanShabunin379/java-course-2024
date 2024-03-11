package edu.java.dto;

import java.net.URI;
import jakarta.validation.constraints.NotEmpty;

public record AddLinkRequest(@NotEmpty URI link) {
}
