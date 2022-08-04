package com.github.toastshaman.tinylog;

import com.github.toastshaman.tinylog.events.CapturingEvents;
import com.github.toastshaman.tinylog.events.PrintingEvents;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Clock;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Instant.EPOCH;
import static java.time.ZoneOffset.UTC;

@SuppressWarnings("ConstantConditions")
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

        var expectedStr = """
                {"metadata":{"name":"My Event","category":"INFO","service":"API#1","timestamp":"1970-01-01T00:00:00Z"},"event":{"counter":1,"object":{"nested":2}}}""";

        StructuredLogs.AddTimestamp(clock)
                .then(StructuredLogs.AddServiceName("API#1"))
                .then(StructuredLogs.AddName())
                .then(StructuredLogs.AddCategory())
                .then(new PrintingEvents())
                .then(new AssertEvent(expectedStr))
                .log(event);
    }

    @Test
    void can_log_custom_events() {
        var event = new MyCustomEvent();

        CapturingEvents capturingEvents = new CapturingEvents();

        StructuredLogs.AddTimestamp(clock)
                .then(StructuredLogs.AddName())
                .then(StructuredLogs.AddCategory())
                .then(capturingEvents)
                .then(new PrintingEvents())
                .log(event);

        Event actual = capturingEvents.captured.peekFirst();
        assertThat(actual).isInstanceOf(MetadataEvent.class);
        assertThat(((MetadataEvent) actual).event()).isInstanceOf(MyCustomEvent.class);
    }

    @Test
    void can_add_custom_metadata() {
        var event = new MyCustomEvent();

        var expectedStr = """
                {"metadata":{"custom-header":"foobar","timestamp":"1970-01-01T00:00:00Z"},"event":{"counter":1}}""";

        StructuredLogs.AddTimestamp(clock)
                .then((StructuredLogs) it -> Map.of("custom-header", "foobar"))
                .then(new PrintingEvents())
                .then(new AssertEvent(expectedStr))
                .log(event);
    }

    static class MyCustomEvent extends AbstractEvent {

        public MyCustomEvent() {
            super(Category.INFO);
        }

        @Override
        public JSONObject toJson() {
            JSONObject payload = new JSONObject();
            payload.putOnce("counter", 1);
            return payload;
        }
    }

    static class AssertEvent implements Events {
        private final ByteArrayOutputStream output = new ByteArrayOutputStream();
        private final PrintStream stream = new PrintStream(output, true, UTF_8);
        private final PrintingEvents events = new PrintingEvents(stream);
        private final String expected;

        public AssertEvent(String expected) {
            this.expected = expected;
        }

        @Override
        public void log(Event event) {
            events.log(event);
            JSONAssert.assertEquals(expected, output.toString(), true);
        }
    }
}