package com.github.toastshaman.tinylog;

import java.util.Map;
import java.util.Objects;

public record MetadataEvent(
        Map<String, Object> metadata,
        Event event
) implements Event {

    public MetadataEvent {
        Objects.requireNonNull(metadata);
        Objects.requireNonNull(event);
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of("metadata", metadata, "event", event.toMap());
    }

    @Override
    public String name() {
        return event.name();
    }

    @Override
    public Category category() {
        return event.category();
    }
}
