package app.com.shubham.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDbHelper extends SQLiteOpenHelper {
    public TaskDbHelper(Context context){
        super(context, TaskData.DB_NAME, null, TaskData.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TaskData.TaskEntry.TABLE +
                " ( " + TaskData.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskData.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TaskData.TaskEntry.TABLE);
        onCreate(sqLiteDatabase);
    }
}
