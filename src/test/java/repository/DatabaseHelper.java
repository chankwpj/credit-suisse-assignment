package repository;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class DatabaseHelper {

    public List<EventRecord> getAllFromEvents(Connection connection) throws Exception {
        final String sql = "SELECT * FROM EVENTS";
        final ResultSet re = connection.createStatement().executeQuery(sql);
        final List<EventRecord> events = new ArrayList<>();
        while (re.next()) {
            final String eventId = re.getString("event_id");
            final long event_duration = re.getLong("event_duration");
            final String host = re.getString("host");
            final String type = re.getString("type");
            final boolean alert = re.getBoolean("alert");
            events.add(new EventRecord(eventId, event_duration, host, type, alert));
        }
        connection.close();
        return events;
    }

}
