import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.DatabaseHelper;
import repository.EventRecord;
import repository.HsqlFileRepository;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LogFileProcessorAppTest {

    private HsqlFileRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        repository = new HsqlFileRepository();
        tearDown();
    }

    @AfterEach
    void tearDown() throws SQLException {
        final Connection conn = getConnection();
        String query = "DELETE FROM EVENTS";
        conn.createStatement().executeUpdate(query);
        conn.close();
    }

    @Test
    void test() throws Exception {
        final Path logFilePath = new File(this.getClass().getClassLoader().getResource("input_log.txt").getFile()).toPath();
        LogFileProcessorApp logFileProcessorApp = new LogFileProcessorApp();
        logFileProcessorApp.run(logFilePath);
        final List<EventRecord> allFromEvents = DatabaseHelper.getAllFromEvents(getConnection());
        assertThat(allFromEvents).hasSize(6);
        assertThat(allFromEvents).containsExactlyInAnyOrder(
                new EventRecord("alt1", 10000, "host1", "APPLICATION", true),
                new EventRecord("alt2", 8000, "host2", "APPLICATION", true),
                new EventRecord("alf1", 1000, "host3", "APPLICATION", false),
                new EventRecord("blt1", 10000,null, null, true),
                new EventRecord("blt2", 8000,null, null, true),
                new EventRecord("blf1", 4000,null, null, false)
        );
    }

    //TODO: test incomplete events

    //TODO: test invalid log lines

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(repository.getConnectionString(), repository.getUser(), repository.getPassword());
    }

}