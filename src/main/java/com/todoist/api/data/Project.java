package com.todoist.api.data;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todoist.api.data.events.Payload;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Project extends Payload {

    int id;
    String name;
    int color;
    int indent;
    int item_order;
    int collapsed;
    boolean shared;
    int is_deleted;
    int is_archived;

    @JsonIgnore
    Set<Item> items;

    public Project() {
        items = new HashSet<>();
    }

    public Project(String name, Set<Item> items, int color) {
        this();
        this.name = name;
        this.items = items;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public int getItem_order() {
        return item_order;
    }

    public void setItem_order(int item_order) {
        this.item_order = item_order;
    }

    public int getCollapsed() {
        return collapsed;
    }

    public void setCollapsed(int collapsed) {
        this.collapsed = collapsed;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }

    public int getIs_archived() {
        return is_archived;
    }

    public void setIs_archived(int is_archived) {
        this.is_archived = is_archived;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color=" + color +
                ", indent=" + indent +
                ", item_order=" + item_order +
                ", collapsed=" + collapsed +
                ", shared=" + shared +
                ", is_deleted=" + is_deleted +
                ", is_archived=" + is_archived +
                ", items="+items+
                '}';
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Collection<Item> getItems(){
        return items;
    }

}
