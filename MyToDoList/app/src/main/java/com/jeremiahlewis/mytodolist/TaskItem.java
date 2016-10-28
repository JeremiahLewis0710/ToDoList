package com.jeremiahlewis.mytodolist;

import android.widget.CheckBox;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

/**
 * Created by jeremiahlewis on 10/18/16.
 * Each To-Do item needs to have the following properties
 * <p>
 * o To-Do name
 * <p>
 * o Due Date/Time
 * <p>
 * o Date/Time Modified
 * <p>
 * o Category
 */

public class TaskItem implements Comparable<TaskItem> {
    @SerializedName("name")
    private String name;

    @SerializedName("text")
    private String text;

    @SerializedName("dateModified")
    private Date dateModified;

    @SerializedName("category")
    private String category;

    @SerializedName("dueDate")
    private String dueDate;

    @SerializedName("key")
    private String key;

    public TaskItem(String name, String text, Date dateModified, String category, String dueDate) {
        this.name = name;
        this.text = text;
        this.dateModified = dateModified;
        this.category = category;
        this.dueDate = dueDate;
        this.key = UUID.randomUUID().toString();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    @Override
    public int compareTo(TaskItem another) {
        //return another.getDateModified().compareTo(getDateModified());
        return getDateModified().compareTo(another.getDateModified());
//        another.getCategory().compareToIgnoreCase(getCategory());
    }
}
