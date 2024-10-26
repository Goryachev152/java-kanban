package model;

import java.util.Objects;

public class SubTask extends Task {
    private EpicTask epic;

    public SubTask(EpicTask epicTask, String name, String description, Status status, TaskType taskType, String startTime, Integer duration) {
        super(name, description, status, taskType, startTime, duration);
        this.epic = epicTask;
    }

    public SubTask(Integer id, EpicTask epicTask, String name, String description, Status status, TaskType taskType, String startTime, Integer duration) {
        super(id, name, description, status, taskType, startTime, duration);
        this.epic = epicTask;
    }

    public SubTask() {
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
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", id, taskType, name, status, description, startTime.format(TIME_FORMATTER), duration.toMinutes(), epic.getId());
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", id, taskType, name, status,
                description, startTime.format(TIME_FORMATTER), duration.toMinutes(), epic.getId());
    }
}
