package org.example.service;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class DefaultReaderProvider implements ReaderProvider {

    @Override
    public BufferedReader getReader(Path path) throws IOException {
        return Files.newBufferedReader(path);
    }
}
