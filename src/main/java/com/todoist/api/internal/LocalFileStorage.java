package com.todoist.api.internal;

import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Sebastian Werner on 18.07.2016.
 */
public class LocalFileStorage implements ILocalStorage {

    private HashMap<String,String> config;
    private String filename;

    public LocalFileStorage(String filename){
        this.filename = filename;
        config = new HashMap<>();
    }

    @Override
    public ILocalStorage load() throws IOException {
        Path path = Paths.get(filename);
        if(!path.toFile().exists()){
            throw new IOException("File not found!");
        }

        CharSource source = Files.asCharSource(path.toFile(), Charset.defaultCharset());
        for (String line : source.readLines()) {
            int i = line.indexOf('=');
            if(i > 0){
                config.put(line.substring(0,i-1),line.substring(i+1));
            }
        }
        return this;
    }

    @Override
    public String getToken() {
        return config.get("token");
    }

    @Override
    public void storeToken(String token) throws IOException {
        config.put("token",token);
        store();
    }

    private void store() throws IOException{
        Path path = Paths.get(filename);
        path.toFile().delete();
        //convert config to strings with key=value
        CharSink charSink = Files.asCharSink(path.toFile(), Charset.defaultCharset(), FileWriteMode.APPEND);
        Stream<CharSequence> stream = config.entrySet().stream()
                .map(kv -> kv.getKey() + "=" + kv.getValue());

        charSink.writeLines(stream.collect(Collectors.toSet()));
    }
}
