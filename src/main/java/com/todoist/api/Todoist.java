package com.todoist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.collect.Lists;
import com.todoist.api.data.*;
import com.todoist.api.internal.*;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import org.apache.cxf.jaxrs.ext.form.Form;
public class Todoist {

    private HashMap<Integer, Label> labelsById;
    private HashMap<Integer, Project> projectsById;
    private HashMap<String, Label> labelsByName;
    private HashMap<String, Project> projectsByName;
    private final ObjectMapper objectMapper;


    private ILocalStorage localStorage;

    private String token;
    private SyncRequest state;

    private final WebClient client;

    public Todoist(String token) {
        this.token = token;

        objectMapper = new ObjectMapper();

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        JacksonJsonProvider jsonProvider = new JacksonJsonProvider(objectMapper);


        client = WebClient

                .create("https://todoist.com/API/v7",
                        Collections.singletonList(jsonProvider));


        sync();
    }

    public void store() throws IOException {
        if(localStorage == null){
            throw new IllegalStateException("LocalStorage not define");
        }
        localStorage.storeToken(token);
    }

    public Todoist(ILocalStorage localStorage) throws IOException {
        this(localStorage.load().getToken());
        this.localStorage = localStorage;
    }

    public Todoist(String token,ILocalStorage localStorage) throws IOException {
        this(token);
        this.localStorage = localStorage;
        localStorage.load();
        localStorage.storeToken(token);
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


        Response response = client.replacePath("/sync").accept(MediaType.APPLICATION_JSON)
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

    public void addLabel(Label label) throws JsonProcessingException {
        Command command = buildAddLabelCommand(label);
        sendCommand(command);
        sync();
    }

    public void addItem(Item item) throws JsonProcessingException {
        ArrayList<Command> command = buildAddItemCommand(item);
        sendCommand(command);
        sync();
    }

    public void addProject(Project project) throws JsonProcessingException {
        ArrayList<Command> command = buildAddProjectCommand(project);
        sendCommand(command);
        sync();
    }

    private ArrayList<Command> buildAddItemCommand(Item item) throws JsonProcessingException {
        return buildAddItemCommand(item,null);
    }

    private Command buildAddLabelCommand(Label label) {
        Map<String,String> args = new HashMap<>();

        args.put("name",label.getName());

        if(label.getColor() != 0){
            args.put("color",Integer.toString(label.getColor()));
        }

        if(label.getItem_order() != 0){
            args.put("item_order",Integer.toString(label.getItem_order()));
        }

        Command command = new Command();
        command.setType("label_add");
        command.setTemp_id(UUID.randomUUID().toString());
        command.setArgs(args);
        return command;
    }
    private ArrayList<Command> buildAddItemCommand(Item item,String project_id) throws JsonProcessingException {
        Project project = item.getProject();

        Map<String,String> args = new HashMap<>();

        ArrayList<Command> commands = new ArrayList<>();

        int i = 0;
        args.put("content",item.getContent());
        if(item.getLabelsAsObject() != null){
            if(!item.getLabelsAsObject().isEmpty()) {

                String[] labels = new String[item.getLabelsAsObject().size()];
                for (Label label : item.getLabelsAsObject()) {
                    if(label.getId() == 0){
                        Command command = buildAddLabelCommand(label);
                        commands.add(command);
                        labels[i++] = command.getTemp_id();
                    } else {
                        labels[i++] = Integer.toString(label.getId());
                    }

                }
                args.put("labels", objectMapper.writeValueAsString(labels));
            }
        }

        if(project_id != null){
            args.put("project_id",project_id);
        } else if(project != null){
            args.put("project_id",Integer.toString(project.getId()));
        }

        Command command = new Command();
        command.setType("item_add");
        command.setTemp_id(UUID.randomUUID().toString());
        command.setArgs(args);
        commands.add(command);
        return commands;
    }
    private ArrayList<Command> buildAddProjectCommand(Project project) throws JsonProcessingException {
        ArrayList<Command> commands = new ArrayList<>();

        String project_id = UUID.randomUUID().toString();


        Collection<Item> items = project.getItems();
        if(items != null){
            if(!items.isEmpty()){
                for (Item item : items) {
                    commands.addAll(buildAddItemCommand(item,project_id));
                }
            }
        }

        Map<String, String> args = new HashMap<>();
        args.put("name",project.getName());

        if(project.getColor() != 0){
            args.put("color",Integer.toString(project.getColor()));
        }

        if(project.getIndent() != 0){
            args.put("item_order",Integer.toString(project.getItem_order()));
        }

        if(project.getItem_order() != 0){
            args.put("item_order",Integer.toString(project.getItem_order()));
        }

        Command command = new Command();
        command.setType("project_add");
        command.setTemp_id(project_id);
        command.setArgs(args);
        commands.add(command);
        return commands;
    }

    private void sendCommand(Command command) throws JsonProcessingException {
        sendCommand(Lists.newArrayList(command));
    }

    private void sendCommand(ArrayList<Command> command) throws JsonProcessingException {
        String value = objectMapper.writeValueAsString(command);


        Response response =
                client.replacePath("/sync")
                        .accept(MediaType.APPLICATION_JSON)
                        .form(new Form().set("token",token)
                                .set("commands",value));

        response.close();
    }


}
