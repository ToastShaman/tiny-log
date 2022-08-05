package com.github.toastshaman.tinylog.events;

import com.github.toastshaman.tinylog.Event;
import com.github.toastshaman.tinylog.Events;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class AsyncEvents implements Events {

    private final BlockingQueue<Event> source;
    private final Worker worker;

    public AsyncEvents(Events events) {
        this(events, Integer.MAX_VALUE);
    }

    public AsyncEvents(Events sink, int capacity) {
        this.source = new LinkedBlockingDeque<>(capacity);
        this.worker = new Worker(source, Objects.requireNonNull(sink));
    }

    @Override
    public void log(Event event) {
        boolean ok = source.offer(Objects.requireNonNull(event));
        if (!ok) {
            System.err.println("Failed to queue event. Maximum queue capacity reached.");
        }
    }

    public synchronized AsyncEvents start() {
        worker.start();
        return this;
    }

    public synchronized AsyncEvents stop() {
        if (!worker.isRunning()) {
            return this;
        }

        worker.halt();
        worker.interrupt();
        return this;
    }

    public int queueSize() {
        return source.size();
    }

    public int remainingCapacity() {
        return source.remainingCapacity();
    }

    private static class Worker extends Thread {
        private final AtomicBoolean running = new AtomicBoolean(false);

        public final BlockingQueue<Event> source;
        public final Events sink;

        public Worker(BlockingQueue<Event> source, Events sink) {
            setDaemon(true);
            this.source = source;
            this.sink = sink;
        }

        @Override
        public synchronized void start() {
            running.set(true);
            super.start();
        }

        public synchronized void halt() {
            running.set(false);
        }

        public boolean isRunning() {
            return running.get();
        }

        @Override
        public void run() {
            while (running.get()) {
                try {
                    Event event = source.take();
                    List<Event> elements = new ArrayList<>(List.of(event));
                    source.drainTo(elements);
                    elements.forEach(sink::log);
                } catch (InterruptedException e) {
                    // exit if interrupted
                    break;
                }
            }

            // flush remaining messages
            source.forEach(sink::log);
            source.clear();
        }
    }
}
