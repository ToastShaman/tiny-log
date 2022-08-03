package com.github.toastshaman.tinylog;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CompositeEvents implements Events {

    private final List<Events> delegates;

    public CompositeEvents(List<Events> delegates) {
        this.delegates = Objects.requireNonNull(delegates);
    }

    public CompositeEvents(Events... delegates) {
        this(Arrays.stream(delegates).toList());
    }

    @Override
    public void log(Event event) {
        delegates.forEach(it -> it.log(Objects.requireNonNull(event)));
    }
}
