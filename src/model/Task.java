package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {
    protected static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected TaskType taskType;
    protected Duration duration;
    protected LocalDateTime startTime;


    public Task() {

    }

    public Task(String name, String description, Status status, TaskType taskType, String startTime, Integer duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.taskType = taskType;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, TIME_FORMATTER);
    }

    public Task(int id, String name, String description, Status status, TaskType taskType, String startTime, Integer duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.taskType = taskType;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, TIME_FORMATTER);
    }

    @Override
    public int compareTo(Task task) {
        return this.startTime.compareTo(task.startTime);
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s", id, taskType, name, status, description, startTime.format(TIME_FORMATTER), duration.toMinutes());
    }

    public void setDuration(Integer duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public void setStartTime(String startTime) {
        this.startTime = LocalDateTime.parse(startTime, TIME_FORMATTER);
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,", id, taskType, name, status,
                description, startTime.format(TIME_FORMATTER), duration.toMinutes());
    }
}
