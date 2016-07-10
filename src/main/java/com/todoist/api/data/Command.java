package com.todoist.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Sebastian Werner on 10-Jul-16.
 */
public class Command {

    @JsonProperty(required = true)
    private String type;
    @JsonProperty(required = true)
    private String uuid;

    @JsonProperty(required = false)
    private String temp_id;

    @JsonProperty(required = true)
    private Map<String,String> args;

    public Command() {
        uuid = UUID.randomUUID().toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTemp_id() {
        return temp_id;
    }

    public void setTemp_id(String temp_id) {
        this.temp_id = temp_id;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }
}
