package com.github.toastshaman.tinylog;

import com.github.toastshaman.tinylog.SimpleEvent.EventBuilder;

import java.util.Map;

public interface Event {
    static EventBuilder builder() {
        return SimpleEvent.builder();
    }

    String name();

    Category category();

    Map<String, Object> toMap();
}