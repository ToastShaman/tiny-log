package com.github.toastshaman.tinylog;

import com.github.toastshaman.tinylog.events.PrintingEvents;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Clock;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Instant.EPOCH;
import static java.time.ZoneOffset.UTC;

class EventsTest {

    private final Clock clock = Clock.fixed(EPOCH, UTC);

    @Test
    void structured_logging() {
        var event = Event.builder()
                .name("My Event")
                .category(Category.INFO)
                .put("counter", 1)
                .put("object", (it) -> it.put("nested", 2))
                .build();

        var output = new ByteArrayOutputStream();
        var stream = new PrintStream(output, true, UTF_8);

        StructuredLogs.AddTimestamp(clock)
                .then(StructuredLogs.AddServiceName("API#1"))
                .then(StructuredLogs.AddName())
                .then(StructuredLogs.AddCategory())
                .then(new PrintingEvents())
                .then(new PrintingEvents(stream))
                .log(event);

        var expectedStr = """
                {"metadata":{"name":"My Event","category":"INFO","service":"API#1","timestamp":"1970-01-01T00:00:00Z"},"event":{"counter":1,"object":{"nested":2}}}""";

        JSONAssert.assertEquals(expectedStr, output.toString(), true);
    }
}