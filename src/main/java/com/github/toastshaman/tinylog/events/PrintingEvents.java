package com.github.toastshaman.tinylog.events;

import com.github.toastshaman.tinylog.Event;
import com.github.toastshaman.tinylog.Events;
import org.json.JSONObject;

import java.io.PrintStream;
import java.util.Objects;

/**
 * Thread-safe printing of events to a {@link PrintStream}.
 */
public class PrintingEvents implements Events {
    private final PrintStream writer;

    public PrintingEvents() {
        this(System.out);
    }

    public PrintingEvents(PrintStream writer) {
        this.writer = Objects.requireNonNull(writer);
    }

    @Override
    public void log(Event event) {
        writer.println(new JSONObject(event.toMap()));
    }
}
