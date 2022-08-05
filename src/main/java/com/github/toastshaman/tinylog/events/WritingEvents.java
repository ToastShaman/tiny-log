package com.github.toastshaman.tinylog.events;

import com.github.toastshaman.tinylog.Event;
import com.github.toastshaman.tinylog.Events;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Objects;

public class WritingEvents implements Events {
    private final Writer writer;

    public WritingEvents(Writer writer) {
        this.writer = Objects.requireNonNull(writer);
    }

    @Override
    public void log(Event event) {
        try {
            writer.write("%s\n".formatted(new JSONObject(event.toMap())));
        } catch (IOException ignore) {
            /* do nothing */
        }
    }
}
