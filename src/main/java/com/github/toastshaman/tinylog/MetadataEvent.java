package com.github.toastshaman.tinylog;

import org.json.JSONObject;

import java.util.Map;

public record MetadataEvent(
        Map<String, Object> metadata,
        Event event
) implements Event {

    @Override
    public JSONObject toJson() {
        JSONObject metadata = new JSONObject(metadata());

        JSONObject envelope = new JSONObject();
        envelope.put("metadata", metadata);
        envelope.put("event", event.toJson());

        return envelope;
    }

    @Override
    public String name() {
        return event.name();
    }

    @Override
    public Category category() {
        return event.category();
    }
}
