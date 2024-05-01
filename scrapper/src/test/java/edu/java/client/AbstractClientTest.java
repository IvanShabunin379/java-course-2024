package edu.java.client;

import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;

public abstract class AbstractClientTest {
    @SneakyThrows
    public String jsonToString(String filePath) {
        return Files.readString(Paths.get(filePath));
    }
}
