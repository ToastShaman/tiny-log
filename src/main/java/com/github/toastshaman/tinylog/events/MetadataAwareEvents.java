package com.github.toastshaman.tinylog.events;

import com.github.toastshaman.tinylog.Event;
import com.github.toastshaman.tinylog.Events;
import com.github.toastshaman.tinylog.MetadataEvent;
import com.github.toastshaman.tinylog.StructuredLogs;

import java.util.*;

public class MetadataAwareEvents implements Events {

    private final List<Events> delegates;
    private final StructuredLogs structuredLogs;

    public MetadataAwareEvents(StructuredLogs structuredLogs, Events events) {
        Objects.requireNonNull(events);
        Objects.requireNonNull(structuredLogs);
        this.structuredLogs = structuredLogs;
        this.delegates = Collections.synchronizedList(new ArrayList<>(List.of(events)));
    }

    @Override
    public void log(Event event) {
        Objects.requireNonNull(event);
        MetadataEvent eventWithMetadata = new MetadataEvent(structuredLogs.apply(event), event);
        delegates.forEach(it -> it.log(eventWithMetadata));
    }

    @Override
    public Events then(Events events) {
        delegates.add(Objects.requireNonNull(events));
        return this;
    }
}
