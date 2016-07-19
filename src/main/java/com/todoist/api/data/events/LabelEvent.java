package com.todoist.api.data.events;

import com.todoist.api.data.Label;

public class LabelEvent extends Event{
    Label event_data;

    public Label getEvent_data() {
        return event_data;
    }

    public void setEvent_data(Label event_data) {
        this.event_data = event_data;
    }
}
