package com.github.toastshaman.tinylog;

import org.junit.jupiter.api.Test;

import static com.github.toastshaman.tinylog.Category.INFO;
import static com.google.common.truth.Truth.assertThat;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

class AsyncEventsTest {

    @Test
    void logs_async() {
        var printingEvents = new PrintingEvents();
        var capturingEvents = new CapturingEvents();
        var sink = new CompositeEvents(
                printingEvents,
                capturingEvents
        );

        var events = new AsyncEvents(sink)
                .start();

        for (int i = 1; i <= 7; i++) {
            events.log(Event.builder().name("Test Event %d".formatted(i)).category(INFO).build());
        }

        await().atMost(5, SECONDS).until(() -> capturingEvents.captured.size() == 7);

        events.stop();

        assertThat(capturingEvents.captured.size()).isEqualTo(7);
    }
}