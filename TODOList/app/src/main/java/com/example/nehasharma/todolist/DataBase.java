package com.example.nehasharma.todolist;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;

import java.util.Enumeration;

/**
 * Created by nehasharma on 8/16/14.
 */
public class DataBase extends SQLiteOpenHelper {

    private static String m_DB_NAME       = "TASK_DB";
    private static String m_TABLE_NAME    = "TASKS_TABLE";
    private static String m_COLUMN_ID     = "ID";
    private static String m_COLUMN_TASK   = "TASK";
    private static String m_COLUMN_STATUS = "STATUS";
    private static String m_COLUMN_DATE   = "DATE";
    private static String m_COLUMN_TIME   = "TIME";
    private static String m_COLUMN_NOTES  = "NOTES";
    private static int    m_version       = 1;
    private Context m_context;

    private static DataBase m_instance = null;

    public static DataBase getInstance(Context ctx) {
        if(m_instance == null) {
            m_instance = new DataBase(ctx);
        }
        return m_instance;
    }

    /*
     * protected SQLiteOpenHelper (Context context, String name, SQLiteDatabase.CursorFactory factory,
     * int version)
     */
    protected DataBase(Context ctx) {
        super(ctx, m_DB_NAME, null, m_version);

        m_context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS "
                + m_TABLE_NAME
                + "("
                + m_COLUMN_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + m_COLUMN_TASK   + " TEXT,"
                + m_COLUMN_STATUS + " INTEGER DEFAULT 0,"
                + m_COLUMN_NOTES  + " TEXT,"
                + m_COLUMN_TIME   + " TIME DEFAULT NULL,"
                + m_COLUMN_DATE   + " DATE DEFAULT NULL"
                + ")";

        db.execSQL(CREATE_TABLE_QUERY );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + m_TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public Utilities.ReturnStatus addNewTask(String text) {

        SQLiteDatabase dataBase = this.getWritableDatabase();
        if(dataBase == null) {
            return Utilities.ReturnStatus.FAILURE;
        }
        ContentValues values = new ContentValues();
        values.put(m_COLUMN_TASK, text); // task name
        // status of task- can be 0 for not done and 1 for done
        values.put(m_COLUMN_STATUS, 0);
        values.put(m_COLUMN_NOTES, "");
        // Inserting Row
        dataBase.insert(m_TABLE_NAME, null, values);
        // Closing database connection
        dataBase.close();

        return Utilities.ReturnStatus.SUCCESS;
    }
    public int getTaskID(String text) {

        int id = -1;

        String selectQuery = "SELECT " + m_COLUMN_ID +" FROM " + m_TABLE_NAME + " WHERE " +
                m_COLUMN_TASK + "= '" + text + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
            } while (cursor.moveToNext());
         }
        //get task ID by the text string, use query
        db.close();
        return id;
    }

    public Utilities.ReturnStatus deleteTask(String text) {
        //call getTaskID(String text)
        //then call deleteTask(int id)
        SQLiteDatabase dataBase = this.getWritableDatabase();
        String[] whereArgs = new String[] { text };
        dataBase.delete(
                m_TABLE_NAME,
                m_COLUMN_TASK + "= '"
                        + text + "'", null);
        dataBase.close();

        return Utilities.ReturnStatus.SUCCESS;
    }

    public Utilities.ReturnStatus deleteTask(int id) {
        return Utilities.ReturnStatus.SUCCESS;
    }

    public Utilities.ReturnStatus updateTaskStatus(int status) {
        return Utilities.ReturnStatus.SUCCESS;
    }

    public Utilities.ReturnStatus updateTaskText(String oldText, String newText) {

        ContentValues values = new ContentValues();
        values.put(m_COLUMN_TASK, newText);

        int taskID = getTaskID(oldText);
        SQLiteDatabase db = this.getWritableDatabase();
        if(taskID == -1) {
            //task not found
            db.close();
            return Utilities.ReturnStatus.FAILURE;
        }
         db.update(m_TABLE_NAME, values, m_COLUMN_ID + "=?",
                new String[]{Integer.toString(taskID)});
        db.close();
        listTasks();
        return Utilities.ReturnStatus.SUCCESS;
    }

    public String getTaskDate(String text) {

        String date = "";

        String selectQuery = "SELECT " + m_COLUMN_DATE +" FROM " + m_TABLE_NAME + " WHERE " +
                m_COLUMN_TASK + "= '" + text + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                date = cursor.getString(0);
                if(date != null && date.length()>0) {
                    break;
                }
            } while (cursor.moveToNext());
        }
        //get task ID by the text string, use query
        db.close();
        return date;
    }

    public Utilities.ReturnStatus updateTaskDate(String text, String date) {

        ContentValues values = new ContentValues();
        values.put(m_COLUMN_DATE, date);

        int taskID = getTaskID(text);
        SQLiteDatabase db = this.getWritableDatabase();
        if(taskID == -1) {
            //task not found
            db.close();
            return Utilities.ReturnStatus.FAILURE;
        }
        db.update(m_TABLE_NAME, values, m_COLUMN_ID + "=?",
                new String[]{Integer.toString(taskID)});
        db.close();
        return Utilities.ReturnStatus.SUCCESS;
    }


    public String getTaskTime(String text) {

        String time = "";

        String selectQuery = "SELECT " + m_COLUMN_TIME +" FROM " + m_TABLE_NAME + " WHERE " +
                m_COLUMN_TASK + "= '" + text + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                time = cursor.getString(0);
                if(time != null && time.length()>0) {
                    break;
                }
            } while (cursor.moveToNext());
        }
        //get task ID by the text string, use query
        db.close();
        return time;
    }

    public Utilities.ReturnStatus updateTaskTime(String text, String time) {

        ContentValues values = new ContentValues();
        values.put(m_COLUMN_TIME, time);

        int taskID = getTaskID(text);
        SQLiteDatabase db = this.getWritableDatabase();
        if(taskID == -1) {
            //task not found
            db.close();
            return Utilities.ReturnStatus.FAILURE;
        }
        db.update(m_TABLE_NAME, values, m_COLUMN_ID + "=?",
                new String[]{Integer.toString(taskID)});
        db.close();
        return Utilities.ReturnStatus.SUCCESS;
    }

    public String getTaskNotes(String text) {

        String notes = "";

        String selectQuery = "SELECT " + m_COLUMN_NOTES +" FROM " + m_TABLE_NAME + " WHERE " +
                m_COLUMN_TASK + "= '" + text + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                notes = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        //get task ID by the text string, use query
        db.close();
        return notes;
    }
    public Utilities.ReturnStatus updateTaskNotes(String text, String notes) {

        ContentValues values = new ContentValues();
        values.put(m_COLUMN_NOTES, notes);

        int taskID = getTaskID(text);
        SQLiteDatabase db = this.getWritableDatabase();
        if(taskID == -1) {
            //task not found
            db.close();
            return Utilities.ReturnStatus.FAILURE;
        }
        db.update(m_TABLE_NAME, values, m_COLUMN_ID + " = ?",
                new String[]{Integer.toString(taskID)});
        db.close();
        return Utilities.ReturnStatus.SUCCESS;

    }

    public void listTasks() {

        List<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + m_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(m_context, cursor.getString(1));
                //task.setTaskStatus(cursor.getInt(2));
                // Adding contact to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        db.close();
    }
        public List<String> getAllTasks() {

        //List<Task> taskList = new ArrayList<Task>();
        List<String> taskList = new ArrayList<String>();
            // Select All Query
        String selectQuery = "SELECT  * FROM " + m_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //Task task = new Task(m_context, cursor.getString(1));
                taskList.add(cursor.getString(1));
                //task.setTaskStatus(cursor.getInt(2));
                // Adding contact to list
                //taskList.add(task);
            } while (cursor.moveToNext());
        }
            db.close();
        // return task list
        return taskList;
    }

}
