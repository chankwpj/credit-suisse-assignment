import logprovider.EventTransformer;
import logprovider.LocalLogFileProvider;
import logprovider.LogStreamProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.EventData;
import models.EventFactory;
import repository.EventRepository;
import repository.HsqlFileRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class LogFileProcessorApp {

    public static void main(String[] path) throws Exception {
        new LogFileProcessorApp().run(Paths.get(path[0]));
    }

    public void run(final Path path) throws Exception {
        final EventRepository eventRepository = new HsqlFileRepository();
        final Map<String, EventData> eventIdLogLineMap = new HashMap<>();
        final LogStreamProvider logStreamProvider = new LocalLogFileProvider(path);
        Stream<String> logStream;
        try {
            logStream = logStreamProvider.getLogStream();
        } catch (IOException ioException) {
            log.error("Not able to retrieve log stream.", ioException);
            return;
        }
        log.info("Processing file.....");
        logStream.forEach(jsonLogLine -> {
                    final Optional<EventData> eventOptional = EventTransformer.to(jsonLogLine);
                    if (!eventOptional.isPresent()) {
                        return;
                    }
                    final EventData eventData = eventOptional.get();

                    if (!eventIdLogLineMap.containsKey(eventData.getId())) {
                        eventIdLogLineMap.put(eventData.getId(), eventData);
                    } else {
                        final EventData anotherEventDataWithSameId = eventIdLogLineMap.get(eventData.getId());
                        if (eventData.getState() == anotherEventDataWithSameId.getState()) {
                            log.warn("Ignore duplicate eventData wit id {} and state {}", eventData.getId(), eventData.getState());
                        } else {
                            eventRepository.save(EventFactory.create(eventData, anotherEventDataWithSameId));
                        }
                    }
                });
        log.info("Process completed.....");
    }

}

