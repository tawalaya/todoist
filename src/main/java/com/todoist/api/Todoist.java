package com.todoist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.todoist.api.data.*;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Todoist {

    private HashMap<Integer, Label> labelsById;
    private HashMap<Integer, Project> projectsById;
    private HashMap<String, Label> labelsByName;
    private HashMap<String, Project> projectsByName;
    private final ObjectMapper objectMapper;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<TodoistBuilder> builderFuture = null;
        try {
            OAuthInterceptorServer oAuth = new OAuthInterceptorServer();
            builderFuture = Executors.newSingleThreadExecutor().submit(oAuth);
        } catch (IOException e) {
        }

        TodoistBuilder todoistBuilder = builderFuture.get();

        Todoist todoist = todoistBuilder.build();
        try {
            todoist.addItem(new ItemBuilder().setContent("HallO").createItem());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private String token;
    private SyncRequest state;

    private final WebClient client;

    public Todoist(String token) {
        objectMapper = new ObjectMapper();

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        JacksonJsonProvider jsonProvider = new JacksonJsonProvider(objectMapper);


        client = WebClient

                .create("https://todoist.com/API/v7",
                        Collections.singletonList(jsonProvider));


        this.token = token;

        sync();
    }


    /**
     * fetch updates
     */
    public Todoist sync(){
        String sync_token;

        if(state != null){
            sync_token = state.getSync_token();
        } else {
            sync_token = "*";
        }

        MultivaluedMap<String,String> request = new MultivaluedHashMap<>();
        request.add("token",token);
        request.add("sync_token",sync_token);
        request.add("resource_types", "[\"labels\",\"projects\",\"items\"]");


        Response response = client.path("/sync").accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_FORM_URLENCODED)
                .post(request);


        if(response.getStatus() != 200){
            throw new IllegalArgumentException("could not fetch update");
        }

        SyncRequest syncRequest = response.readEntity(SyncRequest.class);
        if(syncRequest.isFull_sync()){
            state = syncRequest;
        } else {
            state.updateWithRequest(syncRequest);
        }

        updateInternalState();

        return this;
    }

    /**
     * addes object linking from state respone and updates inital datastructures
     */
    private void updateInternalState() {
        if(state == null) return;

        labelsById = new HashMap<>();
        labelsByName = new HashMap<String,Label>();

        for (Label label : state.getLabels()){
            labelsById.put(label.getId(),label);
            labelsByName.put(label.getName(),label);
        }

        projectsById = new HashMap<>();
        projectsByName = new HashMap<String,Project>();

        for(Project project:state.getProjects()){
            projectsById.put(project.getId(),project);
            projectsByName.put(project.getName(),project);
        }

        HashMap<Integer, Item> itemsById = new HashMap<>();


        for (Item item:state.getItems()){
            for (int label : item.getLabels()){
                Label labelObject;
                if((labelObject = labelsById.get(label)) != null) {
                    item.addLabelObect(labelObject);
                    labelObject.addItem(item);
                }
            }
            Project project = projectsById.get(item.getProject_id());
            if(project != null){
                project.addItem(item);
                item.setProject(project);
            }
            itemsById.put(item.getId(),item);
        }

        for(Item item:itemsById.values()){
            if(item.getChildren() != null){
                for(int child:item.getChildren()){
                    item.addItem(itemsById.get(child));
                }
            }
        }

    }

    public Project getProjectByName(String name){
        return projectsByName.get(name);
    }

    public Project getProjectById(int id){
        return projectsById.get(id);
    }

    public Label getLabelByName(String name){
        return labelsByName.get(name);
    }

    public Label getLabelById(int id){
        return labelsById.get(id);
    }

    public List<Project> getProjects(){
        if(state != null){
            return state.getProjects();
        } else {
            throw new IllegalStateException("API not in sync!");
        }
    }

    public List<Item> getItems(){
        if(state != null){
            return state.getItems();
        } else {
            throw new IllegalStateException("API not in sync!");
        }
    }

    public List<Label> getLabels(){
        if(state != null){
            return state.getLabels();
        } else {
            throw new IllegalStateException("API not in sync!");
        }
    }

    public void addItem(Item item) throws JsonProcessingException {
        Project project = item.getProject();

        Command command = new Command();
        command.setType("item_add");
        command.setTemp_id(UUID.randomUUID().toString());
        command.setArgs(Collections.singletonMap("content","TestTask"));



        MultivaluedMap<String,String> request = new MultivaluedHashMap<>();
        request.add("token",token);
        request.add("commands", objectMapper.writeValueAsString(Collections.singletonList(command)));


        Response response = client.path("/sync").accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_FORM_URLENCODED)
                .post(request);

        String s = response.readEntity(String.class);
        response.close();
    }

    public void addProject(Project project){


        Collection<Item> items = project.getItems();

    }

    public void addLabel(Label label){

    }

}
