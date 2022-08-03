package com.github.toastshaman.tinylog;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Clock;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Instant.EPOCH;
import static java.time.ZoneOffset.UTC;

class EventsTest {

    @Test
    void structured_logging() {
        Event event = Event.builder()
                .name("My Event")
                .category(Category.INFO)
                .put("counter", 1)
                .put("name", "Some Name")
                .put("object", (it) -> it.put("nested", 2))
                .build();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(output, true, UTF_8);

        CompositeEvents events = new CompositeEvents(List.of(
                new PrintingEvents(Clock.fixed(EPOCH, UTC), stream),
                new PrintingEvents(Clock.systemUTC())));
        events.log(event);

        String expectedStr = """
                {"metadata":{"name":"My Event","category":"INFO","timestamp":"1970-01-01T00:00:00Z"},"payload":{"name":"Some Name","counter":1,"object":{"nested":2}}}""";

        JSONAssert.assertEquals(expectedStr, output.toString(), true);
    }
}