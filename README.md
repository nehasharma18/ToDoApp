ToDoApp
=======

Simple Android todo app.

**

-----------------------------------------------------------------------
*This is a Todo List application with the following usage features:*
----------------------------------------------------------------------
1) Add new task.

2) Edit existing task.

3) Schedule a reminder for a task.

4) Add notes associated with a task.

5) Delete a task (set as completed).

-----------------------
*Key features:*
-----------------------
1) SQLite Database was used to store the task list and its related data such as - task string,
completion status, reminder date, reminder time, notes.

2) Date and time Android dialog features were used to enable reminder scheduling.

3) Android Alarm Manager and broadcast receiver was used to enable reminder alarm.

4) Reminder date is checked for being invalid.

-------------------
*Known /issues*
-------------------
1) 5) Added code for generating a notification for reminders. But, for some reason the emulator
does not generate notifications. Tried to debug but, could not find the issue.

2) Tried added checkbox option for deleting tasks using custom adapters. But, on resume the 
tasks in the list were being duplicated. Commented the checkbox feature due this issue.


------------------------------------------------
*Step by step run through the app*
------------------------------------------------

*Add New Task*

[![Add Task](https://github.com/nehasharma18/screenshots/blob/master/Add_task2.gif)]


*Edit Task*

[![Edit Task](https://github.com/nehasharma18/screenshots/blob/master/Edit_task2.gif)]

*Delete Task*

[![Delete Task](https://github.com/nehasharma18/screenshots/blob/master/Delete_task2.gif)]



*Add Reminder*

[![Add Reminder](https://github.com/nehasharma18/screenshots/blob/master/Add_reminder2.gif)]



Waiting for the alarm to go off. A toast is generated as a reminder. 

[![Add Reminder](https://github.com/nehasharma18/screenshots/blob/master/Add_reminder3.gif)]



Error message is generated if reminder date is invalid.

[![Add Reminder](https://github.com/nehasharma18/screenshots/blob/master/Add_reminder4.gif)]


*Add Notes*

[![Add Notes](https://github.com/nehasharma18/screenshots/blob/master/Add_Notes.gif)]


*All information saved in DB*

[![Info Saved](https://github.com/nehasharma18/screenshots/blob/master/info_saved2.gif)]

