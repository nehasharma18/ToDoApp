package com.example.nehasharma.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.PowerManager;
import android.os.Bundle;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.widget.Toast;
import java.util.Calendar;
import android.support.v4.app.NotificationCompat;
import android.app.TaskStackBuilder;
import android.app.NotificationManager;


public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    //final public static String ONE_TIME = "onetime";
    private String m_taskString;
    NotificationGenerator notify;

    public AlarmManagerBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();
        if(extras != null){
            m_taskString = extras.getString("Task");
            msgStr.append(m_taskString);
        }

        Format formatter = new SimpleDateFormat("hh:mm:ss a");
        msgStr.append(formatter.format(new Date()));
        Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();

        notify = new NotificationGenerator(context);
        notify.showNotification(msgStr.toString(), m_taskString);

       //Release the lock
        wl.release();
    }

    public Calendar getAlarmTime(int year, int month, int day, int hour, int min) {

        Calendar reminder = Calendar.getInstance();
        reminder.set(year, month, day, hour, min);
        return reminder;
    }

    public void setOnetimeTimer(Context context, String task, int year, int month, int day, int hour, int min) {

        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        //intent.putExtra(ONE_TIME, Boolean.TRUE);
        intent.putExtra("Task", task);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //TODO: Get the correct alarm time
        Calendar reminder = getAlarmTime(year, month, day, hour, min);
        if (reminder.before(Calendar.getInstance())) {
            Toast.makeText(context, "Invalid date. Reminder not set.", Toast.LENGTH_LONG).show();
            return;
        }
        am.set(AlarmManager.RTC_WAKEUP, reminder.getTimeInMillis(), pi);
        Toast.makeText(context, "Reminder Set.", Toast.LENGTH_LONG).show();

    }


}