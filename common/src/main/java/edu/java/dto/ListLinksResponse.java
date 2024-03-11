package edu.java.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ListLinksResponse(@NotNull List<LinkResponse> links, @Min(value = 0) Long size) {
}
