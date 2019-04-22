package app.com.shubham.todolist.utils;

public class Task {
    private String Description;
    private String Title;

    private Boolean task_done;

    public Boolean getTask_done() {
        return task_done;
    }

    public void setTask_done(Boolean task_done) {
        this.task_done = task_done;
    }


    public Task(String title, String description, boolean task_done) {
        Title = title;
        Description = description;
        this.task_done = task_done;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


}
