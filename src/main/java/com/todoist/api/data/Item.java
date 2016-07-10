package com.todoist.api.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class Item {
    int id;
    int user_id;
    int project_id;
    String content;
    String date_string;
    String date_lang;
    String due_date_utc;
    int indent;
    int priority;
    int item_order;
    int day_order;
    int collapsed;
    int[] children;
    int[] labels;
    int assigned_by_uid;
    String responsible_uid;
    int checked;
    int in_history;
    int is_deleted;
    int is_archived;
    String sync_id;
    String date_added;

    @JsonIgnore
    Set<Label> labelObjects;

    @JsonIgnore
    Project project;



    public Item(Project project, Set<Item> children, Set<Label> labels, String content, int priority, String date_string) {
        this.project = project;
        this.childrenObjects = children;
        this.labelObjects = labels;
        this.content = content;
        this.priority = priority;
        this.date_string = date_string;
    }

    public Item( Project project,Set<Item> children, Set<Label> labels, String content, int priority) {
        this.project = project;
        this.childrenObjects = children;
        this.labelObjects = labels;
        this.content = content;
        this.priority = priority;
    }

    public Item(){
        labelObjects = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate_string() {
        return date_string;
    }

    public void setDate_string(String date_string) {
        this.date_string = date_string;
    }

    public String getDate_lang() {
        return date_lang;
    }

    public void setDate_lang(String date_lang) {
        this.date_lang = date_lang;
    }

    public String getDue_date_utc() {
        return due_date_utc;
    }

    public void setDue_date_utc(String due_date_utc) {
        this.due_date_utc = due_date_utc;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getItem_order() {
        return item_order;
    }

    public void setItem_order(int item_order) {
        this.item_order = item_order;
    }

    public int getDay_order() {
        return day_order;
    }

    public void setDay_order(int day_order) {
        this.day_order = day_order;
    }

    public int getCollapsed() {
        return collapsed;
    }

    public void setCollapsed(int collapsed) {
        this.collapsed = collapsed;
    }

    public int[] getChildren() {
        return children;
    }

    public void setChildren(int[] children) {
        this.children = children;
    }

    public int[] getLabels() {
        return labels;
    }

    public void setLabels(int[] labels) {
        this.labels = labels;
    }

    public int getAssigned_by_uid() {
        return assigned_by_uid;
    }

    public void setAssigned_by_uid(int assigned_by_uid) {
        this.assigned_by_uid = assigned_by_uid;
    }

    public String getResponsible_uid() {
        return responsible_uid;
    }

    public void setResponsible_uid(String responsible_uid) {
        this.responsible_uid = responsible_uid;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getIn_history() {
        return in_history;
    }

    public void setIn_history(int in_history) {
        this.in_history = in_history;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }

    public int getIs_archived() {
        return is_archived;
    }

    public void setIs_archived(int is_archived) {
        this.is_archived = is_archived;
    }

    public String getSync_id() {
        return sync_id;
    }

    public void setSync_id(String sync_id) {
        this.sync_id = sync_id;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", project_id=" + project_id +
                ", content='" + content + '\'' +
                ", date_string='" + date_string + '\'' +
                ", date_lang='" + date_lang + '\'' +
                ", due_date_utc='" + due_date_utc + '\'' +
                ", indent=" + indent +
                ", priority=" + priority +
                ", item_order=" + item_order +
                ", day_order=" + day_order +
                ", collapsed=" + collapsed +
                ", children='" + children + '\'' +
                ", labels=" + Arrays.toString(labels) +
                ", assigned_by_uid=" + assigned_by_uid +
                ", responsible_uid='" + responsible_uid + '\'' +
                ", checked=" + checked +
                ", in_history=" + in_history +
                ", is_deleted=" + is_deleted +
                ", is_archived=" + is_archived +
                ", sync_id='" + sync_id + '\'' +
                ", date_added='" + date_added + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void addLabelObect(Label labelObject) {
        labelObjects.add(labelObject);
    }

    public Collection<Label> getLabelsAsObject(){
        return labelObjects;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @JsonIgnore
    Set<Item> childrenObjects = new HashSet<>();

    public void addItem(Item item) {
        childrenObjects.add(item);
    }

    public Collection<Item> getItems(){
        return childrenObjects;
    }

    public Project getProject() {
        return project;
    }
}
