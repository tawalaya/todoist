package com.todoist.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SyncRequest {

    @JsonProperty(value = "items",required = true)
    List<Item> items;
    List<Project> projects;
    List<Label> labels;

    String sync_token;
    boolean full_sync;

    public SyncRequest() {
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public String getSync_token() {
        return sync_token;
    }

    public void setSync_token(String sync_token) {
        this.sync_token = sync_token;
    }

    public boolean isFull_sync() {
        return full_sync;
    }

    public void setFull_sync(boolean full_sync) {
        this.full_sync = full_sync;
    }

    public void updateWithRequest(SyncRequest syncRequest) {
        this.setSync_token(syncRequest.getSync_token());
        this.setFull_sync(false);

        items.addAll(syncRequest.getItems());
        projects.addAll(syncRequest.getProjects());
        labels.addAll(syncRequest.getLabels());

    }
}
