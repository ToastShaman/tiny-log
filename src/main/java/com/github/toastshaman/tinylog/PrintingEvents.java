package com.github.toastshaman.tinylog;

import java.io.PrintStream;
import java.util.Objects;

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
        writer.println(Objects.requireNonNull(event).toJson());
    }
}
