package com.jeremiahlewis.mytodolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TasksActivity extends AppCompatActivity {
    private ListView notesList;
    String[] notes = new String[]{"Task 1", "Task 2", "Task 3"};
    private TasksArrayAdapter tasksArrayAdapter;
    private ArrayList<TaskItem> tasksArray;
    private SharedPreferences notesprefs;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        //hook up our UI elements to Java Code
        //This was our simple adapter
        notesprefs = getPreferences(Context.MODE_PRIVATE);
        gson = new Gson();
        setupNotes();

        Collections.sort(tasksArray);

        notesList = (ListView) findViewById(R.id.listView);

        tasksArrayAdapter = new TasksArrayAdapter(this, R.layout.task_list_item, tasksArray);
        notesList.setAdapter(tasksArrayAdapter);

//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TasksActivity.this, TaskDetailActivity.class);
//                startActivityForResult(intent, 2);
//
//            }
//        });

        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskItem taskItem = tasksArray.get(position);

                Intent intent = new Intent(TasksActivity.this, TaskDetailActivity.class);
                intent.putExtra("Title", taskItem.getName());
                intent.putExtra("Text", taskItem.getText());
                intent.putExtra("Index", position);

                startActivityForResult(intent, 1);

            }
        });


        notesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //what to do if the view gets a long click
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //builds an alert box
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(TasksActivity.this);
                // gives this box a title & message. the buttons are also established.
                alertBuilder.setTitle("Delete");
                alertBuilder.setMessage("You sure?");
                // if user clicks cancel then do nothing
                alertBuilder.setNegativeButton("Cancel", null);
                //if user clicks the delete then do some stuff
                alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TaskItem taskItem = tasksArray.get(position);
                        deleteFile(taskItem.getKey());
                        tasksArray.remove(position);
                        tasksArrayAdapter.updateAdapter(tasksArray);

                    }
                });
                alertBuilder.create().show();

                return true;
            }
        });


        //notesList.setAdapter(new ArrayAdapter<>(this, R.layout.task_textview_list_item, notes));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int index = data.getIntExtra("Index", -1);
            TaskItem taskItem = new TaskItem(data.getStringExtra("Title"),
                    data.getStringExtra("Text"), new Date(),
                    data.getStringExtra("Category"),
                    data.getStringExtra("Due Date"));
            if (index == -1) {
                tasksArray.add(taskItem);
                writeFile(taskItem);
            } else {
                TaskItem oldTaskItem = tasksArray.get(index);
                taskItem.setKey(oldTaskItem.getKey());
                tasksArray.set(index, taskItem);
            }
            writeFile(taskItem);
            Collections.sort(tasksArray);
            tasksArrayAdapter.updateAdapter(tasksArray);

        }
    }

    /**
     * setupNotes
     * This initiates the listview that contains the tasksArray
     */
    private void setupNotes() {
        tasksArray = new ArrayList<>();
        if (notesprefs.getBoolean("firstRun", true)) {
            SharedPreferences.Editor editor = notesprefs.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();

            TaskItem taskItem1 = new TaskItem("TaskItem 1", "This is a note", new Date(), "Homework", "11/23/16");
            tasksArray.add(taskItem1);
            tasksArray.add(new TaskItem("TaskItem 2", "This is another note", new Date(), "Homework", "11/23/16"));
            tasksArray.add(new TaskItem("TaskItem 2", "This is another note", new Date(), "Homework", "11/23/16"));

            for (TaskItem taskItem : tasksArray) {
                writeFile(taskItem);
            }
        } else {
            readFile();
        }
    }

    private void writeFile(TaskItem taskItem) {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(taskItem.getKey(), Context.MODE_PRIVATE);
            outputStream.write(gson.toJson(taskItem).getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (Exception ignored) {}
        }

    }

    /**
     * readFile
     * looks at the files that have already been added and displays them in the notes view
     */
    private void readFile() {
        File[] filesDir = this.getFilesDir().listFiles();
        for (File file : filesDir) {
            FileInputStream inputStream = null;
            String title = file.getName();

            Date date = new Date(file.lastModified());
            String text = "";
            String category = "";
            String dueDate = "";
            try {
                inputStream = openFileInput(title);
                byte[] input = new byte[inputStream.available()];
                while (inputStream.read(input) != -1) {
                }
                text += new String(input);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                TaskItem task = gson.fromJson(text, TaskItem.class);
                task.setDateModified(date);
                tasksArray.add(task);
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();

                } catch (NullPointerException e) {
                    e.printStackTrace();

                }
            }

            tasksArray.add(new TaskItem(title, text, date, category, dueDate));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(TasksActivity.this, TaskDetailActivity.class);

            intent.putExtra("Title", "");
            intent.putExtra("Text", "");

            startActivityForResult(intent, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
