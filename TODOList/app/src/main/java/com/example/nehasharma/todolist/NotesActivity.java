package com.example.nehasharma.todolist;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import com.example.nehasharma.todolist.R;

public class NotesActivity extends ActionBarActivity {

    DataBase m_db;
    String m_taskString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        m_db = DataBase.getInstance(getApplicationContext());
        m_taskString = getIntent().getStringExtra("task_string");

        final TextView taskText = (TextView) findViewById(R.id.txtTask);
        taskText.setText(m_taskString);

        final EditText editNotesText;
        editNotesText = (EditText) findViewById(R.id.editNotes);

        //TODO: get notes and display
        String current_notes = m_db.getTaskNotes(m_taskString);
        if( current_notes != null) {
            editNotesText.setText(current_notes);
        }

        final Button      doneButton = (Button) findViewById(R.id.btnDone);

        //When done is clicked return to the main activity
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                m_db.updateTaskNotes(m_taskString, editNotesText.getText().toString());
                // Return to main activity
                finish();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
       //get notes and display
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
