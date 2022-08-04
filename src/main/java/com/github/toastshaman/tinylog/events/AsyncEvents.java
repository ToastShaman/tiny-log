package com.github.toastshaman.tinylog.events;

import com.github.toastshaman.tinylog.Event;
import com.github.toastshaman.tinylog.Events;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.SECONDS;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AsyncEvents implements Events {

    private final ExecutorService executor;
    private final BlockingQueue<Event> source;
    private final Events sink;

    private final AtomicBoolean running = new AtomicBoolean(true);
    private final int capacity;

    public AsyncEvents(Events events) {
        this(events, Integer.MAX_VALUE);
    }

    public AsyncEvents(Events events, int capacity) {
        this(events, Executors.newSingleThreadExecutor(), capacity);
    }

    public AsyncEvents(Events events,
                       ExecutorService executor,
                       int capacity) {
        this.capacity = capacity;
        this.sink = Objects.requireNonNull(events);
        this.executor = Objects.requireNonNull(executor);
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
                    if (event != null) {
                        List<Event> elements = new ArrayList<>(List.of(event));
                        source.drainTo(elements);
                        elements.forEach(sink::log);
                    }
                } catch (Exception ignore) {
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
        } catch (Exception ignore) {
            /* do nothing */
        }
        return this;
    }

    public int queueSize() {
        return source.size();
    }

    public boolean isRunning() {
        return running.get();
    }

    public int capacity() {
        return capacity;
    }
}
