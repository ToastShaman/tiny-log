package com.github.toastshaman.tinylog;

import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class CapturingEvents implements Events {
    public final BlockingDeque<Event> captured;

    public CapturingEvents() {
        this(100);
    }

    public CapturingEvents(int capacity) {
        this.captured = new LinkedBlockingDeque<>(capacity);
    }

    @Override
    public void log(Event event) {
        Objects.requireNonNull(event);
        boolean ok = captured.offer(event);
        if (!ok) {
            captured.removeFirst();
            captured.offer(event);
        }
    }
}
