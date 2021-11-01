package models;

public interface Event {

    String getId();
    long getDuration();
    boolean shouldAlert();
    EventType getType();

}
