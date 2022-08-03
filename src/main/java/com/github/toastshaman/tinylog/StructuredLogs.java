package com.github.toastshaman.tinylog;

import java.time.Clock;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public interface StructuredLogs {
    Map<String, Object> apply(Event event);

    default StructuredLogs then(StructuredLogs fn) {
        return event -> {
            var first = Map.copyOf(this.apply(event));
            var second = Map.copyOf(fn.apply(event));
            return Stream.concat(first.entrySet().stream(), second.entrySet().stream())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        };
    }

    default Events then(Events events) {
        return event -> events.log(new MetadataEvent(this.apply(event), event));
    }

    static StructuredLogs AddTimestamp(Clock clock) {
        return event -> Map.of("timestamp", clock.instant().toString());
    }

    static StructuredLogs AddServiceName(String name) {
        return event -> Map.of("service", name);
    }

    static StructuredLogs AddName() {
        return event -> Map.of("name", event.name());
    }

    static StructuredLogs AddCategory() {
        return event -> Map.of("category", event.category());
    }

    static StructuredLogs Noop() {
        return event -> Map.of();
    }
}
