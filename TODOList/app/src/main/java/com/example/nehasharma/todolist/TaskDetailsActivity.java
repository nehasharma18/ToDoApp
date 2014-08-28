package com.example.nehasharma.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.app.TimePickerDialog;
import java.text.SimpleDateFormat;
import android.view.View.OnFocusChangeListener;

import com.example.nehasharma.todolist.R;

import java.util.Date;

public class TaskDetailsActivity extends ActionBarActivity {

    DataBase m_db;
    String   m_taskString;
    String   m_originalTaskString;

    TextView m_dateText;
    TextView m_timeText;
    private AlarmManagerBroadcastReceiver m_alarm;
    int m_year;
    int m_month;
    int m_date;
    int m_hour;
    int m_minute;
    boolean m_time_changed = false;
    boolean m_date_changed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        m_year   = 0;
        m_month  = 0;
        m_date   = 0;
        m_hour   = 0;
        m_minute = 0;

        m_db = DataBase.getInstance(getApplicationContext());
        m_taskString = getIntent().getStringExtra("task_string");
        Toast.makeText(getApplication(),
                "Task String: " + m_taskString, Toast.LENGTH_LONG)
                .show();


        final EditText taskText = (EditText) findViewById(R.id.editTask);
        taskText.setText(m_taskString);

        final Button dateButton = (Button) findViewById(R.id.btnChangeDate);
        final Button timeButton = (Button) findViewById(R.id.btnChangeTime);
        final ImageButton notesButton = (ImageButton) findViewById(R.id.btnAddNotes);
        final Button doneButton = (Button) findViewById(R.id.btnDone);
        m_dateText = (TextView) findViewById(R.id.txtViewDate);
        m_timeText = (TextView) findViewById(R.id.txtViewTime);


        String date = m_db.getTaskDate(m_taskString);
        if( date != null) {
            m_dateText.setText(date);
        }

        String time = m_db.getTaskTime(m_taskString);
        if( time != null) {
            m_timeText.setText(time);
        }

        //When done is clicked return to the main activity
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    if(m_taskString.compareTo(taskText.getText().toString()) != 0) {
                        updateTaskText(m_taskString, taskText.getText().toString());
                }
                // If the date and time are non null, set a timer
                if(m_dateText.length() > 2
                        && m_timeText.length() > 2
                        && (m_time_changed==true || m_date_changed == true)) {

                    reminder();
                }
                // Return to main activity
                finish();
            }
        });


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                int year  = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day   = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(TaskDetailsActivity.this,
                        new m_DateSetListener(), year, month, day);
                dialog.show();

            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Process to get Current Time
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(TaskDetailsActivity.this,
                        new m_TimeSetListener(), hour, minute, true);
                dialog.show();
            }

        });

        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent addNotes = new Intent(getApplicationContext(), NotesActivity.class);
                addNotes.putExtra("task_string", m_taskString);
                startActivity(addNotes);
            }
        });

        m_alarm = new AlarmManagerBroadcastReceiver();

    }

    @Override
    public void finish() {
        // Prepare data intent
        Intent data = new Intent();
        data.putExtra("old_string", m_originalTaskString);
        data.putExtra("new_string", m_taskString);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data);
        super.finish();
    }

    public void reminder() {

        if(m_year == 0) {
            String date = m_dateText.getText().toString();
            String[] date_str = date.split("-");
            m_year  = Integer.parseInt(date_str[0]);
            m_month = Integer.parseInt(date_str[1]);
            m_date  = Integer.parseInt(date_str[2]);
        }

        if(m_hour == 0) {
            String time = m_timeText.getText().toString();
            String[] time_str = time.split(":");
            m_hour  = Integer.parseInt(time_str[0]);
            m_minute = Integer.parseInt(time_str[1]);
        }
        if(m_alarm != null){
            m_alarm.setOnetimeTimer(this.getApplicationContext(), m_taskString, m_year, m_month, m_date, m_hour, m_minute);
        }
    }

    public void updateTaskText(String oldText, String newText) {

        m_db.updateTaskText(oldText, newText);
        m_originalTaskString = m_taskString;
        m_taskString = newText;
    }
    public void updateTaskTime(String time) {
        m_db.updateTaskTime(m_taskString, time);

    }
    public void updateTaskDate(String date) {

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //String date = sdf.format(c.getTime());
        m_db.updateTaskDate(m_taskString, date);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task_details, menu);
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

    class m_DateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            m_year  = year;
            m_month = monthOfYear;
            m_date  = dayOfMonth;

            m_dateText.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(year).append("-").append(monthOfYear + 1).append("-")
                    .append(dayOfMonth).append(" "));
            updateTaskDate(Integer.toString(year) + "-" + Integer.toString(monthOfYear + 1) + "-" + Integer.toString(dayOfMonth));
            m_date_changed = true;
        }
    }

    class m_TimeSetListener implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // TODO Auto-generated method stub

            m_hour   = hour;
            m_minute = minute;

            m_timeText.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(hour).append(":").append(minute).append(" "));
            updateTaskTime(Integer.toString(hour) + ":" + Integer.toString(minute) + ":" + Integer.toString(0));
            m_time_changed = true;

        }
    }
}
