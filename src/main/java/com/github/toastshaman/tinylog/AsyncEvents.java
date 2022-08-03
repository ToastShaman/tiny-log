package com.github.toastshaman.tinylog;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.SECONDS;

public class AsyncEvents implements Events {

    private final ExecutorService executor;
    private final BlockingQueue<Event> source;
    private final Events sink;

    private final AtomicBoolean running = new AtomicBoolean(true);

    public AsyncEvents(Events events) {
        this(events, Integer.MAX_VALUE);
    }

    public AsyncEvents(Events events, int maxValue) {
        this(events, Executors.newSingleThreadExecutor(), maxValue);
    }

    public AsyncEvents(Events events,
                       ExecutorService executor,
                       int capacity) {
        this.sink = events;
        this.executor = executor;
        this.source = new LinkedBlockingDeque<>(capacity);
    }

    @Override
    public void log(Event event) {
        source.offer(Objects.requireNonNull(event));
    }

    public AsyncEvents start() {
        executor.submit(() -> {
            while (running.get()) {
                try {
                    Event event = source.poll(1, SECONDS);
                    if (event != null) sink.log(event);
                } catch (InterruptedException ignore) {
                    /* do nothing */
                }
            }
        });
        return this;
    }

    public AsyncEvents stop() {
        try {
            running.set(false);
            executor.shutdown();
            executor.awaitTermination(5, SECONDS);
        } catch (InterruptedException ignore) {
            /* do nothing */
        }
        return this;
    }
}
