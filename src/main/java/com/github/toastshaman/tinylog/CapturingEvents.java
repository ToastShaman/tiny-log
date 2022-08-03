package com.github.toastshaman.tinylog;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class CapturingEvents implements Events {

    public BlockingDeque<Event> captured = new LinkedBlockingDeque<>();

    @Override
    public void log(Event event) {
        captured.add(event);
    }
}
