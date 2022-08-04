package com.github.toastshaman.tinylog.events;

import com.github.toastshaman.tinylog.Event;
import com.github.toastshaman.tinylog.Events;
import com.github.toastshaman.tinylog.MetadataEvent;
import com.github.toastshaman.tinylog.StructuredLogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetadataAwareEvents implements Events {

    private final List<Events> delegates;
    private final StructuredLogs structuredLogs;

    public MetadataAwareEvents(StructuredLogs structuredLogs, Events... events) {
        this.structuredLogs = structuredLogs;
        this.delegates = new ArrayList<>(Arrays.stream(events).toList());
    }

    @Override
    public void log(Event event) {
        MetadataEvent eventWithMetadata = new MetadataEvent(structuredLogs.apply(event), event);
        delegates.forEach(it -> it.log(eventWithMetadata));
    }

    @Override
    public Events then(Events events) {
        delegates.add(events);
        return this;
    }
}
