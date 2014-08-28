package com.example.nehasharma.todolist;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.nehasharma.todolist.R;
import android.app.Activity;
import android.app.Notification;
import android.widget.Toast;

public class NotificationGenerator {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }*/

    Context m_context;

    public NotificationGenerator(Context ctx) {
        m_context = ctx;
    }
    public void showNotification(String msg, String task) {

        Toast.makeText(m_context,
                "Reminder!: " + task, Toast.LENGTH_LONG)
                .show();

        Intent intent = new Intent(m_context, TaskDetailsActivity.class);
        intent.putExtra("task_string", task);
        PendingIntent pIntent = PendingIntent.getActivity(m_context, 0, intent, 0);

        Notification notification = new Notification.Builder(m_context)
                .setContentTitle("Todo Reminder: ")
                .setContentText(task + "  " + msg).setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent).getNotification();
        NotificationManager notificationManager = (NotificationManager) m_context.getSystemService(m_context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);

    }
}
