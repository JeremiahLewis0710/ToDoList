package com.jeremiahlewis.mytodolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by jeremiahlewis on 10/18/16.
 */

public class TaskDetailActivity extends AppCompatActivity {

    private EditText noteTitle;
    private EditText noteText;
    private EditText category;
    private EditText dueDate;
    private Button saveButton;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        noteTitle = (EditText)findViewById(R.id.note_Title);
        noteText = (EditText)findViewById(R.id.note_Text);
        category = (EditText)findViewById(R.id.category);
        dueDate = (EditText)findViewById(R.id.dueDate);

        saveButton = (Button)findViewById(R.id.save_Button);

        Intent intent = getIntent();

        noteTitle.setText(intent.getStringExtra("Title"));
        noteText.setText(intent.getStringExtra("Text"));
        category.setText(intent.getStringExtra("Category"));
        dueDate.setText(intent.getStringExtra("Due Date"));
        index = intent.getIntExtra("Index", -1);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("Title", noteTitle.getText().toString());
                intent.putExtra("Text", noteText.getText().toString());
                intent.putExtra("Category", category.getText().toString());
                intent.putExtra("Due Date", dueDate.getText().toString());
                intent.putExtra("Index", index);
                setResult(RESULT_OK, intent);
                finish();
            }
        });



    }
}
