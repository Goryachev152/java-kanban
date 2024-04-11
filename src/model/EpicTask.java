package model;

import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>();
    private int id;
    public EpicTask(String name, String description) {
        super(name, description, Status.NEW);
    }


    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        this.subTasks.add(subTask);
    }
    public void removeSubTask() {
        subTasks.clear();
    }



    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "EpicTask{" +
                "name=" + getName() +
                ", description=" + getDescription() +
                ", subTasks=" + subTasks +
                ", id=" + id +
                ", status=" + getStatus() +
                '}';
    }
}
