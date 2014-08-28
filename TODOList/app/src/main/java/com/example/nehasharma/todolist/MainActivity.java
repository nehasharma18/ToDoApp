package com.example.nehasharma.todolist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    String   m_TAG = "MainActivity";
    Context  m_context;
    DataBase m_db;
    ArrayAdapter<Task> m_TaskListAdapter;
    List<Task> m_taskList;
    private static final int REQUEST_CODE = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setHasOptionsMenu(true);
        m_context = getApplicationContext();
        m_db      = DataBase.getInstance(m_context);

        //Adapter related code
        //m_TaskListAdapter.clear();
        if(m_taskList == null) {
            m_taskList = new ArrayList<Task>();
        }
        m_TaskListAdapter = new CheckBoxArrayAdapter(this, m_taskList);

        if(m_taskList.size() == 0) {
            m_taskList.addAll(m_db.getAllTasks());
        }

        ListView list = (ListView) findViewById(R.id.listViewTodo);
        list.setClickable(true);
        list.setAdapter(m_TaskListAdapter);

        final EditText newTaskText;
        final ImageButton addButton;

        newTaskText = (EditText) findViewById(R.id.editText);
        addButton = (ImageButton) findViewById(R.id.addImageButton);

        addButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String task_str = newTaskText.getText().toString();
                Log.d(m_TAG, task_str);
                Task task = new Task(m_context, task_str);
                newTaskText.setText(""); //set edit test space to blank again
                m_taskList.add(task);
                //m_TaskListAdapter.add(task);
                m_TaskListAdapter.notifyDataSetChanged();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String item = m_TaskListAdapter.getItem(position).getTaskText();
                Log.v("MainActivity.java", "Clicked!");
                Intent showDetails = new Intent(m_context, TaskDetailsActivity.class);
                showDetails.putExtra("task_string", item);
                startActivity(showDetails);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if(m_taskList != null) {
            m_taskList.clear();
           m_taskList.addAll(m_db.getAllTasks());
            m_TaskListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
