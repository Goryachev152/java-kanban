package model;

import java.util.ArrayList;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return id == epicTask.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
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
