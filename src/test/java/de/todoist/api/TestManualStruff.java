package de.todoist.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.todoist.api.data.Command;
import okhttp3.*;
import okhttp3.MediaType;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.*;
import org.apache.cxf.jaxrs.ext.form.Form;

import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by Sebastian Werner on 12-Jul-16.
 */
public class TestManualStruff {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        JacksonJsonProvider jsonProvider = new JacksonJsonProvider(objectMapper);


        WebClient client = WebClient
                .create("https://todoist.com/API/v7/sync",
                        Collections.singletonList(jsonProvider));

        String token = "e208351314f24b7513ad52dc4b8455133ee77987";





        javax.ws.rs.core.Response response =
                client.form(new Form().set("token",token).set("commands","[{" +
                        "\"type\":\"item_add\"," +
                        "\"uuid\":\""+UUID.randomUUID().toString()+"\"," +
                        "\"temp_id\":\"2d734e75-7e2b-4e05-b915-251230de589\"," +
                        "\"args\":{\"content\":\"TestTask\"}" +
                        "}]"));

        String s = response.readEntity(String.class);
        response.close();
    }
}
