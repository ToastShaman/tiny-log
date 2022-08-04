package com.github.toastshaman.tinylog;

public abstract class AbstractEvent implements Event {
    private final Category category;

    public AbstractEvent(Category category) {
        this.category = category;
    }

    @Override
    public Category category() {
        return category;
    }

    @Override
    public String name() {
        return super.getClass().getSimpleName();
    }
}
