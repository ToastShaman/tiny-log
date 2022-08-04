package com.github.toastshaman.tinylog.events;

import com.github.toastshaman.tinylog.Event;
import com.github.toastshaman.tinylog.Events;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.Objects;

public class Sl4jEvents implements Events {

    private final Logger logger;

    public Sl4jEvents() {
        this(LoggerFactory.getLogger(Sl4jEvents.class));
    }

    public Sl4jEvents(Logger logger) {
        this.logger = Objects.requireNonNull(logger);
    }

    @Override
    public void log(Event event) {
        var payload = new JSONObject(event.toMap()).toString();
        switch (event.category()) {
            case INFO, METRICS -> logger.info(payload);
            case WARN -> logger.warn(payload);
            case ERROR -> logger.error(payload);
        }
    }
}
