package com.github.toastshaman.tinylog.events;

import com.github.toastshaman.tinylog.Event;
import com.github.toastshaman.tinylog.Events;

import java.util.List;
import java.util.Objects;

public class CompositeEvents implements Events {

    private final List<Events> delegates;

    public CompositeEvents(List<Events> delegates) {
        this.delegates = List.copyOf(Objects.requireNonNull(delegates));
    }

    public CompositeEvents(Events... delegates) {
        this(List.of(Objects.requireNonNull(delegates)));
    }

    @Override
    public void log(Event event) {
        Objects.requireNonNull(event);
        delegates.forEach(it -> it.log(event));
    }
}
