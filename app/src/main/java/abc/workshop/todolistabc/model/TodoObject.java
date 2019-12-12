package abc.workshop.todolistabc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodoObject {


    private int id;

    private String title;

    private Boolean completed;

    private String user_id;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean active) {
        this.completed = active;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }

}