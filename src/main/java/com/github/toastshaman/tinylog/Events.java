package com.github.toastshaman.tinylog;

import com.github.toastshaman.tinylog.events.CompositeEvents;

public interface Events {
    void log(Event event);

    default Events then(Events events) {
        return new CompositeEvents(this, events);
    }
}
