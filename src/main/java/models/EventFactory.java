package models;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventFactory {

    public Event create(EventData eventData1, EventData eventData2) {
        long startTimestamp = eventData1.getState() == State.STARTED ? eventData1.getTimestamp() : eventData2.getTimestamp();
        long finishTimestamp = eventData1.getState() == State.FINISHED ? eventData1.getTimestamp() : eventData2.getTimestamp();
        if ("APPLICATION_LOG".equals(eventData1.getType())) {
            return new ApplicationEvent(eventData1.getId(), eventData1.getHost(), startTimestamp, finishTimestamp);
        } else {
            return new BasicEvent(eventData1.getId(), startTimestamp, finishTimestamp);
        }
    }

}
