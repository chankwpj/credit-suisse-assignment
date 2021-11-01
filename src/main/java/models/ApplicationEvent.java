package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class ApplicationEvent implements Event {

    private static final int ThresholdInMillSec = 4000;
    private final String id;
    private final String host;
    private final long duration;

    public ApplicationEvent(final String id, final String host, final long startTimestamp, final long completeTimestamp) {
        this.id = id;
        this.host = host;
        this.duration =  completeTimestamp - startTimestamp;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean shouldAlert() {
        return duration > ThresholdInMillSec;
    }

    @Override
    public EventType getType() {
        return EventType.APPLICATION;
    }

}
