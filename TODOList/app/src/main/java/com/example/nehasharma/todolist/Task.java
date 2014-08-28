package com.example.nehasharma.todolist;

import android.util.Log;
import android.widget.Toast;
import android.content.Context;
/**
 * Created by nehasharma on 8/16/14.
 */
public class Task {

    private String     m_task_text;
    private int m_task_status;
    private DataBase   m_db;
    private Context    m_context;
    private String     m_TAG = "Task";

    public Task(Context ctx,String text) {

        m_db = DataBase.getInstance(ctx);
        if(m_db == null || ctx == null) {
            // display toast or show error?
            Log.d(m_TAG, "DB or context found to be null");
            return;
        }
        if(text == null || text.length() == 0) {
            Toast msg = Toast.makeText( ctx, "Task cannot be empty!",
                    Toast.LENGTH_LONG);
            msg.show();
            return;
        }
        m_task_text   = text;
        m_task_status = 0;
        m_context = ctx;

        //Add this new task to the DB
        m_db.addNewTask(text);
    }
    String getTaskText() {
        return this.m_task_text;
    }

    public int getTaskStatus() {
        return this.m_task_status;
    }

    public Utilities.ReturnStatus setTaskStatus(int status) {

        m_task_status = status;
        //Update DB here
        return m_db.updateTaskStatus(status);
    }

    public Utilities.ReturnStatus setTaskText(String oldText, String newText) {

        if(newText == null || newText.length() == 0 || oldText == null || oldText.length() == 0) {
            // display toast or show error?
        }
        m_task_text   = newText;
        //Update DB here
        return m_db.updateTaskText(oldText, newText);
    }

    public Utilities.ReturnStatus deleteTask() {

        //Update DB here, delete task
        return m_db.deleteTask(m_task_text);
    }

    public static enum  TaskStatus {
        NEW, COMPLETED
    };
}
