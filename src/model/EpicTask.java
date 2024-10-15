package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EpicTask extends Task {
    protected List<SubTask> subTasks = new ArrayList<>();
    protected LocalDateTime endTime = getEndTime();

    public EpicTask(String name, String description, Status status, TaskType taskType) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.taskType = taskType;
    }

    public EpicTask(Integer id, String name, String description, Status status, TaskType taskType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.taskType = taskType;
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

    public LocalDateTime getStartTime() {
        startTime = subTasks.stream().map(SubTask::getStartTime).min(LocalDateTime::compareTo).orElse(null);
        return startTime;
    }

    @Override
    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%s", id, taskType, name, status, description);
    }

    @Override
    public LocalDateTime getEndTime() {
        endTime = subTasks.stream().map(SubTask::getEndTime).max(LocalDateTime::compareTo).orElse(null);
        return endTime;
    }

    @Override
    public Duration getDuration() {
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            duration = Duration.between(getStartTime(), getEndTime());
        }
        return duration;
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
        if (Objects.isNull(duration) & Objects.nonNull(startTime)) {
            return String.format("%s,%s,%s,%s,%s,%s,%s", id, taskType, name, status, description, getStartTime().format(TIME_FORMATTER), null);
        } else if (Objects.isNull(startTime)) {
            return String.format("%s,%s,%s,%s,%s,%s,%s", id, taskType, name, status, description, null, null);
        } else {
            return String.format("%s,%s,%s,%s,%s,%s,%s", id, taskType, name, status, description,
                    getStartTime().format(TIME_FORMATTER), null);
        }
    }
}
