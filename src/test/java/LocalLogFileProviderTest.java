import logprovider.LocalLogFileProvider;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class LocalLogFileProviderTest {

    @Test
    void shouldReturnLogLines() throws Exception {
        final Path logFilePath = new File(this.getClass().getClassLoader().getResource("test_log.txt").getFile()).toPath();
        final LocalLogFileProvider provider = new LocalLogFileProvider(logFilePath);
        assertThat(provider.getLogStream().collect(Collectors.toList())).containsExactly(
                "{\"id\":\"scsmbstgra\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1205410654000}",
                "{\"id\":\"scsmbstgra\",\"state\":\"FINISHED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1205410660000}"
        );
    }

    //TODO: test invalid path
}