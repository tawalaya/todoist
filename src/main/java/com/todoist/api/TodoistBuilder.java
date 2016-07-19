package com.todoist.api;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.todoist.api.internal.Token;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Collections;

public class TodoistBuilder {
    private String token;

    public TodoistBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public TodoistBuilder withOAuthCode(String code){
        MultivaluedMap<String,String> request = new MultivaluedHashMap<>();
        request.add("client_id","c5ab7ebac34e4ca59c8a8b5147eec2c0");
        request.add("client_secret","7f494eee3e3b4267b7fe5d939b23167c");
        request.add("code",code);
        request.add("redirect_uri","http://localhost/success");

        Response response = WebClient

                .create("https://todoist.com/oauth/access_token", Collections.singletonList(new JacksonJsonProvider()))
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_FORM_URLENCODED)
                .post(request);

        Token token = response.readEntity(Token.class);

        setToken(token.getAccess_token());
        return this;
    }

    public Todoist build() {
        return new Todoist(token);
    }
}