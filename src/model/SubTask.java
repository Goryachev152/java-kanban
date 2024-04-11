package model;

import model.EpicTask;
import model.Status;
import model.Task;

public class SubTask extends Task {
    private EpicTask epic;
    private int id;
    public SubTask(EpicTask epicTask, String name, String description, Status status) {
        super(name, description, status);
        this.epic = epicTask;
    }
    public SubTask(int id, String name, String description, Status status, EpicTask epicTask) {
        super(name, description, status);
        this.epic = epicTask;
        setId(id);
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
