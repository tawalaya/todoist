package com.todoist.api.internal;

import java.io.UnsupportedEncodingException;

/**
 * Created by Sebastian Werner on 05-Jul-16.
 */
public class OAuthRequest {
    String client_id;
    String scope;
    String state;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public OAuthRequest() {
    }

    public static String asFromParameter(OAuthRequest request) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        builder.append("client_id=").append(request.client_id);
        builder.append("&");
        builder.append("scope=").append(request.scope);
        builder.append("&");
        builder.append("state=").append(request.state);
        return builder.toString();
    }
}
