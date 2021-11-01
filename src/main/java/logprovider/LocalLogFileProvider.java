package logprovider;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class LocalLogFileProvider implements LogStreamProvider {

    private final Path path;

    public Stream<String> getLogStream() throws IOException {
        return Files.newBufferedReader(path).lines();
    }

}
