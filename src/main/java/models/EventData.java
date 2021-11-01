package models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
public class EventData {

    private String id;
    private State state;
    private String host;
    private long timestamp;
    private String type;

}
