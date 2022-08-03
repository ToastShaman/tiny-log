package com.github.toastshaman.tinylog;

public interface Events {
    void log(Event event);

    default Events then(Events events) {
        return new CompositeEvents(this, events);
    }
}
