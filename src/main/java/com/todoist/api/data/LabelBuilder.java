package com.todoist.api.data;

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