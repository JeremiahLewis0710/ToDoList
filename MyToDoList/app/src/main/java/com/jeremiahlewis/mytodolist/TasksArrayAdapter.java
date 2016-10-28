package com.jeremiahlewis.mytodolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jeremiahlewis on 10/18/16.
 */

public class TasksArrayAdapter extends ArrayAdapter<TaskItem> {
    private int resource;
    private ArrayList<TaskItem> taskItems;
    private LayoutInflater inflater;
    private SimpleDateFormat formatter;
// Custom Array Adapter
    //Simple ones will not work because of the complex data that it has to store
    public TasksArrayAdapter(Context context, int resource, ArrayList<TaskItem> objects){
        //call super class of the ArrayAdapter
        super(context, resource, objects);
        //Assign the constructor input values for later use
        this.resource = resource;
        this.taskItems = objects;

        //Create the inflater, will need it later to inflate the complex layout into the view(listview)

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        formatter = new SimpleDateFormat("MM/dd/yyyy");
    }
//the array adapter already calls getView but you ovveride it so you can specifically tell it what to do
    //while you are dealing with complex data
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View notesRow = inflater.inflate(resource, parent, false);

        TextView taskTitle = (TextView)notesRow.findViewById(R.id.task_title);
        TextView taskText = (TextView)notesRow.findViewById(R.id.task_text);
        TextView taskDate = (TextView)notesRow.findViewById(R.id.task_date);

        TaskItem taskItem = taskItems.get(position);

        taskTitle.setText(taskItem.getName());
        taskText.setText(taskItem.getText());
        taskDate.setText(formatter.format(taskItem.getDateModified()));

        return notesRow;
    }

    public void updateAdapter(ArrayList<TaskItem> taskItems){
        this.taskItems = taskItems;
        super.notifyDataSetChanged();
    }
}
