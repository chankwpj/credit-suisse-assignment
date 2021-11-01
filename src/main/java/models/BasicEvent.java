package models;

import lombok.ToString;

@ToString
public class BasicEvent implements Event {

    private static final int ThresholdInMillSec = 4000;
    private final String id;
    private final long duration;

    public BasicEvent(final String id, final long startTimestamp, final long completeTimestamp) {
        this.id = id;
        this.duration =  completeTimestamp - startTimestamp;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    public boolean shouldAlert() {
        return duration > ThresholdInMillSec;
    }

    @Override
    public EventType getType() {
        return EventType.UNKNOWN;
    }

}
