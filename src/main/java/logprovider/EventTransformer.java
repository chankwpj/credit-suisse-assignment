package logprovider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import models.EventData;

import java.util.Optional;

@UtilityClass
@Slf4j
public class EventTransformer {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Optional<EventData> to(final String jsonLogLine) {
        try {
            return Optional.of(OBJECT_MAPPER.readValue(jsonLogLine, EventData.class));
        } catch (JsonProcessingException e) {
            log.error("Cannot handle invalid log line: {}", jsonLogLine, e);
            return Optional.empty();
        }
    }

}
