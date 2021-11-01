package repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import models.ApplicationEvent;
import models.Event;
import models.EventType;

import java.sql.*;

@Getter
@Slf4j
public class HsqlFileRepository implements EventRepository {

    private final String jdbcDriver = "org.hsqldb.jdbc.JDBCDriver";
    private final String connectionString = "jdbc:hsqldb:file:./database/eventDb";
    private final String user = "SA";
    private final String password = null;

    public HsqlFileRepository() throws Exception {
        Class.forName(jdbcDriver);
        createTableIfNotExists();
    }

    @Override
    public void save(final Event event) {
        log.info("Saving event with event id {}", event.getId());
        try {
            final Connection con = getConnection();
            con.createStatement();
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO EVENTS(event_id, event_duration, alert, type, host) VALUES (?, ?, ?, ?, ?)");
            pstmt.setString(1, event.getId());
            pstmt.setLong(2, event.getDuration());
            pstmt.setBoolean(3, event.shouldAlert());
            if (event.getType() == EventType.APPLICATION) {
                pstmt.setString(4, EventType.APPLICATION.name());
                pstmt.setString(5, ((ApplicationEvent) event).getHost());
            } else if (event.getType() == EventType.UNKNOWN) {
                pstmt.setString(4, null);
                pstmt.setString(5, null);
            }
            pstmt.executeUpdate();
            con.commit();
            con.close();
        } catch (Exception e) {
            log.error("Failed to save event", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString, user, password);
    }

    private void createTableIfNotExists() throws SQLException {
        final Connection con = getConnection();
        if (!tableExists(con, "EVENTS")) {
            log.info("Creating table EVENTS");
            String createTableSql = "CREATE TABLE EVENTS " +
                    "(event_id VARCHAR(255) not NULL, " +
                    " event_duration BIGINT not NULL, " +
                    " host VARCHAR(255), " +
                    " type VARCHAR(255), " +
                    " alert BOOLEAN not null, " +
                    " PRIMARY KEY ( event_id ));";
            con.createStatement().execute(createTableSql);
            con.commit();
            con.close();
        }
    }

    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) "
                + "FROM information_schema.tables "
                + "WHERE table_name = ?"
                + "LIMIT 1;");
        preparedStatement.setString(1, tableName);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1) != 0;
    }

}
