package model;

import model.EpicTask;
import model.Status;
import model.Task;

import java.util.Objects;

public class SubTask extends Task {
    private EpicTask epic;
    private int id;
    public SubTask(EpicTask epicTask, String name, String description, Status status) {
        super(name, description, status);
        this.epic = epicTask;
    }
    public SubTask(int id, String name, String description, Status status, EpicTask epicTask) {
        super(id, name, description, status);
        this.epic = epicTask;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public EpicTask getEpic() {
        return epic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return id == subTask.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epic=" + epic.getId() +
                ", name=" + getName() +
                ", description=" + getDescription() +
                ", status=" + getStatus() +
                ", id=" + id +
                '}';
    }
}
