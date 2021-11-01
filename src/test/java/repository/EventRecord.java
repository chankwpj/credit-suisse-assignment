package repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
@AllArgsConstructor
public class EventRecord {
    private final String eventId;
    private final long event_duration;
    private final String host;
    private final String type;
    private final boolean alert;
}
