package com.todoist.api.data.events;

import com.todoist.api.data.Project;

public class ProjectEvent extends Event{
    Project event_data;

    public Project getEvent_data() {
        return event_data;
    }

    public void setEvent_data(Project event_data) {
        this.event_data = event_data;
    }
}
