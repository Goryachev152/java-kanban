package service;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    Task updateTask(Task task);

    List<Task> getAllTask();

    void removeAllTask();

    Task getTask(int id);

    Task removeTask(int id);

    EpicTask createEpic(EpicTask epicTask);

    EpicTask getEpicTask(int id);

    List<SubTask> getSubTaskEpicTask(EpicTask epicTask);

    EpicTask removeEpicTask(int id);

    List<EpicTask> getAllEpicTask();

    void removeAllEpicTask();

    EpicTask updateEpicTask(EpicTask epicTask);

    List<SubTask> getAllSubTaskEpic(EpicTask epicTask);

    SubTask createSubTask(SubTask subTask);

    SubTask getSubTask(int id);

    List<SubTask> getAllSubTask();

    SubTask removeSubTask(int id);

    void removeAllSubTask();

    SubTask updateSubTask(SubTask subTask);

    boolean isValidTask(Task task);

    List<Task> getPrioritizedTasks();

    HistoryManager getHistoryManager();
}
