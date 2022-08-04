package com.github.toastshaman.tinylog;

import com.github.toastshaman.tinylog.events.AsyncEvents;
import com.github.toastshaman.tinylog.events.CapturingEvents;
import com.github.toastshaman.tinylog.events.PrintingEvents;
import org.junit.jupiter.api.Test;

import java.time.Clock;

import static com.github.toastshaman.tinylog.Category.INFO;
import static com.google.common.truth.Truth.assertThat;
import static java.time.Instant.EPOCH;
import static java.time.ZoneOffset.UTC;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

class AsyncEventsTest {

    private final Clock clock = Clock.fixed(EPOCH, UTC);

    @Test
    void logs_async() {
        var capturingEvents = new CapturingEvents();
        var async = new AsyncEvents(capturingEvents);

        var events = StructuredLogs.AddTimestamp(clock)
                .then(StructuredLogs.AddServiceName("API#1"))
                .then(StructuredLogs.AddName())
                .then(StructuredLogs.AddCategory())
                .then(new PrintingEvents())
                .then(async);

        async.start();

        for (int i = 1; i <= 7; i++) {
            events.log(Event.builder().name("Test Event %d".formatted(i)).category(INFO).build());
        }

        await().atMost(5, SECONDS).until(() -> capturingEvents.captured.size() == 7);

        async.stop();

        assertThat(capturingEvents.captured.size()).isEqualTo(7);
    }
}