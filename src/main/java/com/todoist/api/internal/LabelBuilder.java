package com.todoist.api.internal;

import com.todoist.api.data.Label;

public class LabelBuilder {
    private String name;
    private int color;

    public LabelBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public LabelBuilder setColor(int color) {
        this.color = color;
        return this;
    }

    public Label build() {
        return new Label(name, color);
    }
}