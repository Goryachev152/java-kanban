package model;

import java.util.Objects;

public class SubTask extends Task {
    private EpicTask epic;

    public SubTask(EpicTask epicTask, String name, String description, Status status, TaskType taskType) {
        super(name, description, status, taskType);
        this.epic = epicTask;
    }

    public SubTask(Integer id, EpicTask epicTask, String name, String description, Status status, TaskType taskType) {
        super(id, name, description, status, taskType);
        this.epic = epicTask;
    }

    public void setEpic(EpicTask epicTask) {
        this.epic = epicTask;
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
    public String toCsvString() {
        return super.toCsvString() + "," + epic.getId();
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
