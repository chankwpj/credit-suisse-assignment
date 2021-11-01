package repository;

import lombok.extern.slf4j.Slf4j;
import models.ApplicationEvent;
import models.BasicEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class HsqlFileRepositoryTest {

    private HsqlFileRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        repository = new HsqlFileRepository();
        tearDown();
    }

    @AfterEach
    void tearDown() throws SQLException {
        final Connection conn = DriverManager.getConnection(repository.getConnectionString(), repository.getUser(), repository.getPassword());
        String query = "DELETE FROM EVENTS";
        conn.createStatement().executeUpdate(query);
        conn.close();
    }

    @Test
    void shouldSaveApplicationEvent() throws Exception {
        final Instant instant = Instant.now();
        repository.save(new ApplicationEvent("001", "host-05", instant.toEpochMilli(), instant.plusSeconds(10).toEpochMilli()));
        repository.save(new ApplicationEvent("002", "host-90", instant.toEpochMilli(), instant.plusSeconds(1).toEpochMilli()));
        final List<EventRecord> selectAllFromEvents = DatabaseHelper.getAllFromEvents(DriverManager.getConnection(repository.getConnectionString(), repository.getUser(), repository.getPassword()));
        assertThat(selectAllFromEvents).hasSize(2);
        assertThat(selectAllFromEvents).containsExactlyInAnyOrder(
                new EventRecord("001", 10000, "host-05", "APPLICATION", true),
                new EventRecord("002", 1000, "host-90", "APPLICATION", false)
        );
    }

    @Test
    void shouldSaveBasicEvent() throws Exception {
        final Instant instant = Instant.now();
        repository.save(new BasicEvent("001", instant.toEpochMilli(), instant.plusSeconds(10).toEpochMilli()));
        repository.save(new BasicEvent("002", instant.toEpochMilli(), instant.plusSeconds(1).toEpochMilli()));
        final List<EventRecord> selectAllFromEvents = DatabaseHelper.getAllFromEvents(DriverManager.getConnection(repository.getConnectionString(), repository.getUser(), repository.getPassword()));
        assertThat(selectAllFromEvents).containsExactlyInAnyOrder(
                new EventRecord("001", 10000, null, null, true),
                new EventRecord("002", 1000, null, null, false)
        );
    }

    //TODO: add error handling test

}