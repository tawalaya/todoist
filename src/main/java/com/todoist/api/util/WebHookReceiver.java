package com.todoist.api.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.todoist.api.data.Event;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.observables.AsyncOnSubscribe;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WebHookReceiver extends HttpHandler implements Observable.OnSubscribe<Event>{

    ConcurrentLinkedQueue<Subscriber<? super Event>> subscribers = new ConcurrentLinkedQueue<>();
    private final ObjectMapper mapper;
    private final HttpServer server;


    public WebHookReceiver(int port){
        server = HttpServer.createSimpleServer("http://localhost", port);
        server.getServerConfiguration().addHttpHandler(this);
        mapper = new ObjectMapper();

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
        publishEvent(event);
        response.setStatus(200);
    }

    private Event parseRequest(Request request) {
        try {
            return mapper.readValue(request.getInputStream(), Event.class);
        } catch (IOException e) {}

        return null;
    }

    private void publishEvent(Event event) {
        assert event != null;

        subscribers.removeIf(Subscriber::isUnsubscribed);

        subscribers.forEach(subscriber -> subscriber.onNext(event));
    }
}
