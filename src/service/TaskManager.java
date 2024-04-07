package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, EpicTask> epics = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int id;
    private int generationId() {
        return ++id;
    }
    public Task creationTask(Task task) {
        task.setId(generationId());
        tasks.put(task.getId(), task);
        return task;
    }
    public Task updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }
    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }
    public void removeAllTask() {
        tasks.clear();
    }
    public Task getTask(int id) {
        return tasks.get(id);
    }
    public void removeTask(int id) {
        tasks.remove(id);
    }
    public EpicTask creationEpic(EpicTask epicTask) {
        epicTask.setId(generationId());
        epics.put(epicTask.getId(), epicTask);
        return epicTask;
    }
    public EpicTask getEpicTask(int id) {
        return epics.get(id);
    }
    public ArrayList<SubTask> getSubTaskEpicTask(EpicTask epicTask) {
        return new ArrayList<SubTask>(epicTask.getSubTasks());
    }
    public void removeEpicTask(int id) {
        EpicTask epicTaskCopy = epics.remove(id);
        ArrayList<SubTask> subTask = epicTaskCopy.getSubTasks();
        for (SubTask subTaskCopy : subTask) {
            subTasks.remove(subTaskCopy.getId());
        }
        subTask.clear();
    }
    public ArrayList<EpicTask> getAllEpicTask() {
        return new ArrayList<>(epics.values());
    }
    public void removeAllEpicTask() {
        removeAllSubTask();
        epics.clear();
    }
    public void updateEpicTask(EpicTask epicTask) {
            EpicTask epicTaskCopy = epics.get(epicTask.getId());
            epicTaskCopy.setName(epicTask.getName());
            epicTaskCopy.setDescription(epicTask.getDescription());
    }
    public ArrayList<SubTask> getAllSubTaskEpic(EpicTask epicTask) {
        return epicTask.getSubTasks();
    }
    public SubTask creationSubTask(SubTask subTask) {
        EpicTask epic = epics.get(subTask.getEpic().getId());
        epic.addSubTask(subTask);
        subTask.setId(generationId());
        updateStatus(epic);
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }
    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }
    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }
    public void removeSubTask(int id) {
        SubTask removeSubTask = subTasks.remove(id);
        EpicTask epicTask = removeSubTask.getEpic();
        epicTask.getSubTasks().remove(removeSubTask);
        updateStatus(epicTask);

    }
    public void removeAllSubTask() {
        subTasks.clear();
        Collection<EpicTask> epicTasks = epics.values();
        for (EpicTask epicTaskCopy : epicTasks) {
            epicTaskCopy.removeSubTask();
            updateStatus(epicTaskCopy);
        }
    }
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            updateStatus(subTask.getEpic());
            subTasks.put(subTask.getId(), subTask);
        }
    }
    private void updateStatus(EpicTask epicTask) {
        boolean isStatus = true;
        for (SubTask copyEpicSubTask : epicTask.getSubTasks()) {
            if (copyEpicSubTask.getStatus() != Status.DONE) {
                isStatus = false;
                break;
            }
        }
        if (!isStatus) {
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
