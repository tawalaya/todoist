package com.todoist.api.data.events;

import com.todoist.api.data.Item;

public class ItemEvent extends Event{
    Item event_data;

    public Item getEvent_data() {
        return event_data;
    }

    public void setEvent_data(Item event_data) {
        this.event_data = event_data;
    }
}
