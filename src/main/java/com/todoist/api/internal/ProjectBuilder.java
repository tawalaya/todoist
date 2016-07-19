package com.todoist.api.internal;

import com.todoist.api.data.Item;
import com.todoist.api.data.Project;

import java.util.HashSet;
import java.util.Set;

public class ProjectBuilder {
    private String name;
    private Set<Item> items = new HashSet<>();
    private int color = 0;

    public ProjectBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProjectBuilder withItem(Item item) {
        this.items.add(item);
        return this;
    }

    public ProjectBuilder setColor(int color) {
        this.color = color;
        return this;
    }

    public Project createProject() {
        return new Project(name, items, color);
    }
}