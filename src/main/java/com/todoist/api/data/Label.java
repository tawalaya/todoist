package com.todoist.api.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todoist.api.data.events.Payload;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Label extends Payload {
    int id;
    String name;
    int color;
    int item_order;
    int is_deleted;


    @JsonIgnore
    private Set<Item> items;

    public Label(){
        items = new HashSet<>();
    }

    public Label(String name, int color) {
        this();
        this.name = name;
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

    public int getItem_order() {
        return item_order;
    }

    public void setItem_order(int item_order) {
        this.item_order = item_order;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }

    @Override
    public String toString() {
        return "Label{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color=" + color +
                ", item_order=" + item_order +
                ", is_deleted=" + is_deleted +
                '}';
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Collection<Item> getItems() {
        return items;
    }
}
