package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    HashMap<Integer, Task> tasks;
    HashMap<Integer, EpicTask> epics;
    HashMap<Integer, SubTask> subTasks;
    HistoryManager historyManager;
    private int id;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.historyManager = historyManager;
    }
    private int generateId() {
        return ++id;
    }
    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeAllTask() {
        tasks.clear();
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
    }

    @Override
    public EpicTask createEpic(EpicTask epicTask) {
        epicTask.setId(generateId());
        epics.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    @Override
    public EpicTask getEpicTask(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public ArrayList<SubTask> getSubTaskEpicTask(EpicTask epicTask) {
        return new ArrayList<SubTask>(epicTask.getSubTasks());
    }

    @Override
    public void removeEpicTask(int id) {
        EpicTask epicTaskCopy = epics.remove(id);
        ArrayList<SubTask> subTask = epicTaskCopy.getSubTasks();
        for (SubTask subTaskCopy : subTask) {
            subTasks.remove(subTaskCopy.getId());
        }
        subTask.clear();
    }

    @Override
    public ArrayList<EpicTask> getAllEpicTask() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllEpicTask() {
        removeAllSubTask();
        epics.clear();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
            EpicTask epicTaskCopy = epics.get(epicTask.getId());
            epicTaskCopy.setName(epicTask.getName());
            epicTaskCopy.setDescription(epicTask.getDescription());
    }

    @Override
    public ArrayList<SubTask> getAllSubTaskEpic(EpicTask epicTask) {
        return epicTask.getSubTasks();
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        EpicTask epic = epics.get(subTask.getEpic().getId());
        epic.addSubTask(subTask);
        subTask.setId(generateId());
        updateStatus(epic);
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    @Override
    public SubTask getSubTask(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void removeSubTask(int id) {
        SubTask removeSubTask = subTasks.remove(id);
        EpicTask epicTask = removeSubTask.getEpic();
        epicTask.getSubTasks().remove(removeSubTask);
        updateStatus(epicTask);

    }

    @Override
    public void removeAllSubTask() {
        subTasks.clear();
        Collection<EpicTask> epicTasks = epics.values();
        for (EpicTask epicTaskCopy : epicTasks) {
            epicTaskCopy.removeSubTask();
            updateStatus(epicTaskCopy);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            updateStatus(subTask.getEpic());
            subTasks.put(subTask.getId(), subTask);
        }
    }

    private void updateStatus(EpicTask epicTask) {
        boolean isStatusDone = true;
        for (SubTask copyEpicSubTask : epicTask.getSubTasks()) {
            if (copyEpicSubTask.getStatus() != Status.DONE) {
                isStatusDone = false;
                break;
            }
        }
        if (!isStatusDone) {
            for (SubTask copyEpicSubTask : epicTask.getSubTasks()) {
                if (copyEpicSubTask.getStatus() == Status.IN_PROGRESS) {
                    epicTask.setStatus(Status.IN_PROGRESS);
                }
            }
        } else {
            epicTask.setStatus(Status.DONE);
        }

    }

}
