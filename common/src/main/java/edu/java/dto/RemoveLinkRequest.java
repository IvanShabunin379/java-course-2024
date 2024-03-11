package edu.java.dto;

import java.net.URI;
import jakarta.validation.constraints.NotEmpty;

public record RemoveLinkRequest(@NotEmpty URI link) {
}
