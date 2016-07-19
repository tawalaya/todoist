package com.todoist.api.internal;

import com.todoist.api.data.Item;
import com.todoist.api.data.Label;
import com.todoist.api.data.Project;

import java.util.HashSet;
import java.util.Set;

public class ItemBuilder {
    private Project project;
    private Set<Item> children = new HashSet<>();
    private Set<Label> labels = new HashSet<>();
    private String content;
    private int priority;
    private String dateString;

    public ItemBuilder withProject(Project project) {
        this.project = project;
        return this;
    }

    public ItemBuilder withLabel(Label label){
        this.labels.add(label);
        return this;
    }

    public ItemBuilder withChild(Item item){
        children.add(item);
        return this;
    }

    public ItemBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public ItemBuilder setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public ItemBuilder setDate_string(String dateString) {
        this.dateString = dateString;
        return this;
    }

    public Item createItem() {
        if(dateString == null){
            return new Item(project, children, labels, content, priority);
        }
        return new Item(project, children, labels, content, priority, dateString);
    }
}