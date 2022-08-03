package com.github.toastshaman.tinylog;

import java.util.List;

public class CompositeEvents implements Events {

    private final List<Events> delegates;

    public CompositeEvents(List<Events> delegates) {
        this.delegates = delegates;
    }

    @Override
    public void log(Event event) {
        delegates.forEach(it -> it.log(event));
    }
}
