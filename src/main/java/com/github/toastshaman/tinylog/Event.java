package com.github.toastshaman.tinylog;

import com.github.toastshaman.tinylog.SimpleEvent.EventBuilder;
import org.json.JSONObject;

public interface Event {
    static EventBuilder builder() {
        return SimpleEvent.builder();
    }

    JSONObject toJson();

    String name();

    Category category();
}