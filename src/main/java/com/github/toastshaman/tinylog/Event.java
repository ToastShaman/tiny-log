package com.github.toastshaman.tinylog;

import org.json.JSONObject;

import java.util.Objects;
import java.util.function.Consumer;

public record Event(
        String name,
        Category category,
        JSONObject payload
) {

    public static EventBuilder builder() {
        return new EventBuilder();
    }

    public static class EventBuilder {
        private String name;
        private Category category = Category.INFO;
        private JSONObject payload = new JSONObject();

        public EventBuilder name(String value) {
            this.name = value;
            return this;
        }

        public EventBuilder category(Category value) {
            this.category = value;
            return this;
        }

        public EventBuilder payload(JSONObject value) {
            this.payload = value;
            return this;
        }

        public EventBuilder put(String key, Object value) {
            this.payload.putOnce(key, value);
            return this;
        }

        public EventBuilder put(String key, Consumer<JSONObject> fn) {
            JSONObject nested = new JSONObject();
            fn.accept(nested);
            this.payload.putOnce(key, nested);
            return this;
        }

        public Event build() {
            Objects.requireNonNull(name);
            Objects.requireNonNull(category);
            Objects.requireNonNull(payload);
            return new Event(name, category, payload);
        }
    }
}
