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
    //ArrayAdapter<Task> m_TaskListAdapter;
    ArrayAdapter<String> m_TaskListAdapter;
    //List<Task> m_taskList;
    List<String> m_taskList;
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
            m_taskList = new ArrayList<String>();
        }
        //m_TaskListAdapter = new CheckBoxArrayAdapter(this, m_taskList);
        m_TaskListAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.list_layout, R.id.label, m_taskList);

        if(m_taskList.size() == 0) {
            m_taskList.addAll(m_db.getAllTasks());
        }

        final ListView list = (ListView) findViewById(R.id.listViewTodo);
        list.setClickable(true);
        list.setAdapter(m_TaskListAdapter);

        final EditText newTaskText;
        final ImageButton addButton;

        newTaskText = (EditText) findViewById(R.id.editText);
        addButton = (ImageButton) findViewById(R.id.addImageButton);

        addButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String task_str = newTaskText.getText().toString();
                if(task_str == null || task_str.length() == 0) {
                    String error = "Failed to add empty task. Please try again.";
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
                Log.d(m_TAG, task_str);
                //Task task = new Task(m_context, task_str);
                newTaskText.setText(""); //set edit test space to blank again
                m_taskList.add(task_str);
                m_db.addNewTask(task_str);
                //m_TaskListAdapter.add(task);
                m_TaskListAdapter.notifyDataSetChanged();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //String item = m_TaskListAdapter.getItem(position).getTaskText();
                String item = m_TaskListAdapter.getItem(position).toString();
                Log.v("MainActivity.java", "Clicked!");
                Intent showDetails = new Intent(m_context, TaskDetailsActivity.class);
                showDetails.putExtra("task_string", item);
                startActivityForResult(showDetails, REQUEST_CODE);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {


                m_db.updateTaskStatus(1);
                String curr_task = m_TaskListAdapter.getItem(position).toString();
                m_taskList.remove(curr_task);
                m_db.deleteTask(curr_task);
                //list.remove(position);
                m_TaskListAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data.hasExtra("old_string") && data.hasExtra("new_string")) {

                String original = data.getExtras().getString("old_string");
                String new_str = data.getExtras().getString("new_string");

                if(new_str != null && original != null) {
                    m_taskList.remove(original);
                    m_taskList.add(new_str);
                    m_TaskListAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Task Updated: " + data.getExtras().getString("new_string"),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        /*if(m_taskList != null) {
            m_taskList.clear();
           m_taskList.addAll(m_db.getAllTasks());
            m_TaskListAdapter.notifyDataSetChanged();
        }*/
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
