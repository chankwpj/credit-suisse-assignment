package logprovider;

import java.io.IOException;
import java.util.stream.Stream;

public interface LogStreamProvider {

    Stream<String> getLogStream() throws IOException;

}
