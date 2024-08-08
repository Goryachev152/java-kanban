package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EpicTask extends Task {
    private List<SubTask> subTasks = new ArrayList<>();

    public EpicTask(String name, String description, Status status, TaskType taskType) {
        super(name, description, status, taskType);
    }

    public EpicTask(Integer id, String name, String description, Status status, TaskType taskType) {
        super(id, name, description, status, taskType);
    }

    public EpicTask(Integer id) {
        super();
        this.id = id;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        this.subTasks.add(subTask);
    }

    public void removeSubTask() {
        subTasks.clear();
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
