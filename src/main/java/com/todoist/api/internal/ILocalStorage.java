package com.todoist.api.internal;

import java.io.IOException;

/**
 * Created by Sebastian Werner on 18.07.2016.
 */
public interface ILocalStorage {

    ILocalStorage load() throws IOException;

    String getToken();

    void storeToken(String token) throws IOException;
}
