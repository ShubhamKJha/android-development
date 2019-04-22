package app.com.shubham.todolist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.com.shubham.todolist.adapters.TaskAdapter;
import app.com.shubham.todolist.db.TaskData;
import app.com.shubham.todolist.db.TaskDbHelper;
import app.com.shubham.todolist.utils.Task;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = this.getClass().getSimpleName();
    private TaskDbHelper mHelper = null;
    private ListView mTaskListView = null;
    private ArrayAdapter<String> mAdapter = null;
    private RecyclerView rv_tasks = null;

    private List<Task> taskList = new ArrayList<>();
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHelper = new TaskDbHelper(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogWindow();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rv_tasks = (RecyclerView)findViewById(R.id.rv_Content_page);
        adapter = new TaskAdapter(taskList);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(this);

        rv_tasks.setLayoutManager(llm);
        rv_tasks.setItemAnimator(new DefaultItemAnimator());
        rv_tasks.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rv_tasks.setAdapter(adapter);

        initializeDatabase();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialogWindow(){

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        final EditText taskEditText = new EditText(this);
        taskEditText.setHint("Title");
        final EditText taskDescEditText = new EditText(this);
        taskDescEditText.setHint("Description");

        linearLayout.addView(taskEditText);
        linearLayout.addView(taskDescEditText);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Add New Task")
                .setView(linearLayout)
                .setPositiveButton("ADD TASK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String task_title = taskEditText.getText().toString();
                        String task_desc = taskDescEditText.getText().toString();
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(TaskData.TaskEntry.COL_TASK_TITLE, task_title);
                        values.put(TaskData.TaskEntry.COL_TASK_DESC, task_desc);
                        values.put(TaskData.TaskEntry.COL_TASK_DONE, 0);
                        db.insertWithOnConflict(TaskData.TaskEntry.TABLE,
                                null,values, SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        taskList.add(new Task(task_title, task_desc, false));
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("CLOSE", null)
                .create();
        alertDialog.show();
    }

    private void initializeDatabase(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskData.TaskEntry.TABLE,
                new String[]{TaskData.TaskEntry._ID,
                        TaskData.TaskEntry.COL_TASK_TITLE,
                        TaskData.TaskEntry.COL_TASK_DESC,
                        TaskData.TaskEntry.COL_TASK_DONE},
                null,null,null,null,null);
        while(cursor.moveToNext()){
            int idx = cursor.getColumnIndex(TaskData.TaskEntry.COL_TASK_TITLE);
            String task_title = cursor.getString(idx);

            idx = cursor.getColumnIndex(TaskData.TaskEntry.COL_TASK_DESC);
            String task_desc = cursor.getString(idx);

            idx = cursor.getColumnIndex(TaskData.TaskEntry.COL_TASK_DONE);
            int task_done = cursor.getInt(idx);

            taskList.add(new Task(task_title, task_desc, task_done==1));
        }
        adapter.notifyDataSetChanged();

        cursor.close();
        db.close();

    }

    public void change_to_done(View v){
        CheckBox cb = (CheckBox)v;
        boolean checked = cb.isChecked();

        int pos = (int)cb.getTag();

        RelativeLayout parent = (RelativeLayout) cb.getParent();
        TextView textTitle = (TextView)parent.findViewById(R.id.tv_task_name);
        String title = textTitle.getText().toString();

        int res;
        if(checked)
            res = 1;
        else res = 0;

        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        String Filter = TaskData.TaskEntry.COL_TASK_TITLE+"='"+title+"'";
        values.put(TaskData.TaskEntry.COL_TASK_DONE,res);
        db.update(TaskData.TaskEntry.TABLE,values,Filter,null);
        db.close();
        taskList.get(pos).setTask_done(checked);
    }

    public void update_data(View v){
        ImageButton btn = (ImageButton)v;
        final int pos = (int)btn.getTag();

        final Task past_task = taskList.get(pos);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        final EditText taskEditText = new EditText(this);
        taskEditText.setHint("Title");
        final EditText taskDescEditText = new EditText(this);
        taskDescEditText.setHint("Description");

        linearLayout.addView(taskEditText);
        linearLayout.addView(taskDescEditText);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Update Task")
                .setView(linearLayout)
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String task_title = taskEditText.getText().toString();
                        String task_desc = taskDescEditText.getText().toString();
                        Task new_task = new Task(
                                task_title,
                                task_desc,
                                past_task.getTask_done()
                        );
                        updateDatabase(past_task, new_task);
                        taskList.add(pos, new_task);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("CLOSE", null)
                .create();
        dialog.show();

        adapter.notifyDataSetChanged();
    }

    public void delete_data(View v){
        ImageButton btn = (ImageButton)v;
        int pos = (int)btn.getTag();

        RelativeLayout rlayout = (RelativeLayout)v.getParent().getParent().getParent();
        String text = ((TextView)rlayout.findViewById(R.id.tv_task_name)).getText().toString();

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskData.TaskEntry.TABLE,
                TaskData.TaskEntry.COL_TASK_TITLE+" = ?",
                new String[]{text});
        db.close();

        taskList.remove(pos);
        adapter.notifyDataSetChanged();
    }

    private void updateDatabase(Task inp,Task change_to){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int done_val;
        if(change_to.getTask_done()) done_val = 1;
        else done_val = 0;

        Cursor cursor = db.query(TaskData.TaskEntry.TABLE,
                new String[]{TaskData.TaskEntry._ID},
                TaskData.TaskEntry.COL_TASK_TITLE+"=?",new String[]{inp.getTitle()},
                null,null,null);
        cursor.moveToNext();
        int ID = cursor.getInt(cursor.getColumnIndex(TaskData.TaskEntry._ID));
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(TaskData.TaskEntry.COL_TASK_TITLE, change_to.getTitle());
        values.put(TaskData.TaskEntry.COL_TASK_DESC, change_to.getDescription());
        values.put(TaskData.TaskEntry.COL_TASK_DONE, done_val);
        db.update(TaskData.TaskEntry.TABLE,
                values,TaskData.TaskEntry._ID+"=?",new String[]{""+ID});
        db.close();
    }
}
