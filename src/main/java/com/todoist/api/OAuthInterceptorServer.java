package com.todoist.api;

import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.grizzly.http.server.*;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * starts a server that serves a login page lisences for the OAuth Respone
 *
 * Login page will be availibe @ http://localhost/index.html
 */
public class OAuthInterceptorServer implements Callable<TodoistBuilder> {


        String code = null;
        private final HttpServer server;

        public OAuthInterceptorServer() throws IOException {

            server = HttpServer.createSimpleServer("http://localhost",80);
            server.getServerConfiguration().addHttpHandler(
                    new CLStaticHttpHandler(OAuthInterceptorServer.class.getClassLoader(),"/html/"), "/");
            server.getServerConfiguration().addHttpHandler(new HttpHandler() {
                @Override
                public void service(Request request, Response response) throws Exception {
                    code = request.getParameter("code");
                }
            },"/oauth");
            server.start();
        }

        @Override
        public TodoistBuilder call() throws Exception {
            while (code == null){
                Thread.yield();
            }

            server.shutdown();

            return new TodoistBuilder().withOAuthCode(code);
        }



}
