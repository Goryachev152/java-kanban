package service;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager {
    Task createTask(Task task);

    Task updateTask(Task task);

    ArrayList<Task> getAllTask();

    void removeAllTask();

    Task getTask(int id);

    void removeTask(int id);

    EpicTask createEpic(EpicTask epicTask);

    EpicTask getEpicTask(int id);

    ArrayList<SubTask> getSubTaskEpicTask(EpicTask epicTask);

    void removeEpicTask(int id);

    ArrayList<EpicTask> getAllEpicTask();

    void removeAllEpicTask();

    void updateEpicTask(EpicTask epicTask);

    ArrayList<SubTask> getAllSubTaskEpic(EpicTask epicTask);

    SubTask createSubTask(SubTask subTask);

    SubTask getSubTask(int id);

    ArrayList<SubTask> getAllSubTask();

    void removeSubTask(int id);

    void removeAllSubTask();

    void updateSubTask(SubTask subTask);
}
