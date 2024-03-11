package edu.java.dto;

import java.util.List;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ListLinksResponse(@NotNull List<LinkResponse> links, @Min(value = 0) Long size) {
}
