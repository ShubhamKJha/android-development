package app.com.shubham.todolist.db;

import android.provider.BaseColumns;

public class TaskData {
    public static final String DB_NAME = "app.com.shubham2.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns{
        public static final String TABLE = "tasks";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DESC = "descriptions";
        public static final String COL_TASK_DONE = "taskdone";
    }
}
