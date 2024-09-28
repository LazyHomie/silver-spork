package org.example.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;

public interface ReaderProvider {
    BufferedReader getReader(Path path) throws IOException;
}
