package com.github.toastshaman.tinylog;

import org.json.JSONObject;

import java.io.PrintStream;
import java.time.Clock;
import java.util.Objects;

public class PrintingEvents implements Events {

    private final Clock clock;
    private final PrintStream writer;

    public PrintingEvents() {
        this(Clock.systemUTC(), System.out);
    }

    public PrintingEvents(Clock clock) {
        this(clock, System.out);
    }

    public PrintingEvents(Clock clock, PrintStream writer) {
        this.clock = Objects.requireNonNull(clock);
        this.writer = Objects.requireNonNull(writer);
    }

    @Override
    public void log(Event event) {
        Objects.requireNonNull(event);

        JSONObject metadata = new JSONObject();
        metadata.put("timestamp", clock.instant().toString());
        metadata.put("name", event.name());
        metadata.put("category", event.category());

        JSONObject envelope = new JSONObject();
        envelope.put("metadata", metadata);
        envelope.put("payload", event.payload());

        writer.println(envelope);
    }
}
