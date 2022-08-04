package com.github.toastshaman.tinylog;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public record SimpleEvent(
        String name,
        Category category,
        Map<String, Object> payload
) implements Event {

    public SimpleEvent {
        Objects.requireNonNull(name);
        Objects.requireNonNull(category);
        Objects.requireNonNull(payload);
    }

    public static EventBuilder builder() {
        return new EventBuilder();
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.copyOf(payload);
    }

    public static class EventBuilder {
        private String name;
        private Category category = Category.INFO;
        private Map<String, Object> payload = new HashMap<>();

        public EventBuilder name(String value) {
            this.name = Objects.requireNonNull(value);
            return this;
        }

        public EventBuilder category(Category value) {
            category = Objects.requireNonNull(value);
            return this;
        }

        public EventBuilder payload(Map<String, Object> value) {
            payload = Objects.requireNonNull(value);
            return this;
        }

        public EventBuilder put(String key, Object value) {
            Objects.requireNonNull(key);
            Objects.requireNonNull(value);

            if (payload.containsKey(key)) {
                throw new RuntimeException("Duplicate key %s".formatted(key));
            }

            payload.put(key, value);
            return this;
        }

        public EventBuilder put(String key, Consumer<Map<String, Object>> fn) {
            Objects.requireNonNull(key);
            Objects.requireNonNull(fn);

            Map<String, Object> nested = new HashMap<>();
            fn.accept(nested);
            payload.put(key, nested);
            return this;
        }

        public SimpleEvent build() {
            return new SimpleEvent(name, category, Map.copyOf(payload));
        }
    }
}
