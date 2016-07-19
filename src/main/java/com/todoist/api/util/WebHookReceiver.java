package com.todoist.api.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.todoist.api.data.events.Event;
import com.todoist.api.data.events.ItemEvent;
import com.todoist.api.data.events.LabelEvent;
import com.todoist.api.data.events.ProjectEvent;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import rx.Observable;
import rx.Subscriber;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WebHookReceiver extends HttpHandler implements Observable.OnSubscribe<Event>{

    ConcurrentLinkedQueue<Subscriber<? super Event>> subscribers = new ConcurrentLinkedQueue<>();
    private final ObjectMapper mapper;
    private final HttpServer server;


    public WebHookReceiver(int port){
        server = HttpServer.createSimpleServer("http://0.0.0.0", port);
        server.getServerConfiguration().addHttpHandler(this, "/event");
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }

    public void startService() throws IOException{
        server.start();
    }

    public void stopService(){
        subscribers.forEach(Subscriber::unsubscribe);
        server.shutdown();
    }

    @Override
    public void call(Subscriber<? super Event> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void service(Request request, Response response) throws Exception {
        Event event = parseRequest(request);
        if(event != null){
            publishEvent(event);
        }

        response.setStatus(200);
    }

    private Event parseRequest(Request request) {
        try {
            String string = null;


            string = CharStreams.toString(new InputStreamReader(request.getNIOInputStream(), "UTF-8"));

            HashMap<String,String> raw = mapper.readValue(string,
                    new TypeReference<HashMap>(){});

            switch (raw.get("event_name")){
                case "item:added":
                    return mapper.readValue(string, ItemEvent.class);
                case "project:added":
                    return mapper.readValue(string, ProjectEvent.class);
                case "label:added":
                    return mapper.readValue(string, LabelEvent.class);
            }

        } catch (IOException e) {}

        return null;
    }

    private void publishEvent(Event event) {
        assert event != null;

        subscribers.removeIf(Subscriber::isUnsubscribed);

        subscribers.forEach(subscriber -> subscriber.onNext(event));
    }

}
