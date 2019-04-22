package app.com.shubham.todolist.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import app.com.shubham.todolist.R;
import app.com.shubham.todolist.utils.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> list_of_tasks;

    public TaskAdapter(List<Task> list_of_tasks) {
        this.list_of_tasks = list_of_tasks;
    }



    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.to_do_description,viewGroup,false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout fmlayout = (FrameLayout)view.findViewById(R.id.fl_description_window);
                if(fmlayout.getVisibility()==View.VISIBLE)
                    fmlayout.setVisibility(View.GONE);
                else if(fmlayout.getVisibility()==View.GONE)
                    fmlayout.setVisibility(View.VISIBLE);
            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder viewHolder, int i) {
        Task task = list_of_tasks.get(i);
        viewHolder.title_tv.setText(task.getTitle());
        viewHolder.description_tv.setText(task.getDescription());
        viewHolder.task_done_cb.setActivated(task.getTask_done());
        viewHolder.task_done_cb.setTag(i);
    }

    @Override
    public int getItemCount() {
        return list_of_tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title_tv, description_tv;
        public ImageButton del_image_btn, update_image_btn;
        public CheckBox task_done_cb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = (TextView) itemView.findViewById(R.id.tv_task_name);
            description_tv = (TextView)itemView.findViewById(R.id.tv_task_description);
            del_image_btn = (ImageButton)itemView.findViewById(R.id.ib_delete_task);
            update_image_btn = (ImageButton)itemView.findViewById(R.id.ib_edit_task);
            task_done_cb = (CheckBox)itemView.findViewById(R.id.task_done_check_box);
        }
    }
}
