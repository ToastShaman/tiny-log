package com.github.toastshaman.tinylog;

import com.github.toastshaman.tinylog.events.MetadataAwareEvents;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

public interface StructuredLogs {
    Map<String, Object> apply(Event event);

    default StructuredLogs then(StructuredLogs fn) {
        return event -> {
            var result = new HashMap<String, Object>();
            result.putAll(apply(event));
            result.putAll(fn.apply(event));
            return Map.copyOf(result);
        };
    }

    default Events then(Events events) {
        return new MetadataAwareEvents(this, events);
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
