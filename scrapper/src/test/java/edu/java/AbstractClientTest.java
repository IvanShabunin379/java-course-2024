package edu.java;

import lombok.SneakyThrows;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractClientTest {
    @SneakyThrows
    public String jsonToString(String filePath) {
        return Files.readString(Paths.get(filePath));
    }
}
