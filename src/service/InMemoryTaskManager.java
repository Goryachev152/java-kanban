package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import model.TaskType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;


public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks;
    private Map<Integer, EpicTask> epics;
    private Map<Integer, SubTask> subTasks;
    private HistoryManager historyManager;
    private TreeSet<Task> priorityTasks;
    private int id;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.priorityTasks = new TreeSet<>();
        this.historyManager = historyManager;
    }

    public List<Task> getPrioritizedTasks() {
        return priorityTasks.stream().toList();
    }

    protected void addPriorityTask(Task task) {
        if (task.getStartTime() != null & !priorityTasks.contains(task)) {
            priorityTasks.add(task);
        } else {
            System.out.println("Задача уже содержится в списке или остутствует время старта задачи");
        }
    }

    protected Boolean isValidIntersection(Task task1, Task task2) {
        return task1.getStartTime().isBefore(task2.getEndTime()) && task1.getEndTime().isAfter(task2.getStartTime());
    }

    protected Boolean isValidTask(Task newTask) {
        Optional<Task> currentTask = getPrioritizedTasks().stream()
                .filter(task -> task.getId() == (newTask.getId()))
                .findFirst();
        if (currentTask.isPresent()) {
            priorityTasks.remove(currentTask.get());
            if (getPrioritizedTasks().stream().anyMatch(task1 -> isValidIntersection(newTask, task1))) {
                addPriorityTask(currentTask.get());
                return true;
            }
        }
        return getPrioritizedTasks().stream().anyMatch(task1 -> isValidIntersection(newTask, task1));
    }

    protected Map<Integer, Task> getTasks() {
        return tasks;
    }

    protected Map<Integer, EpicTask> getEpics() {
        return epics;
    }

    protected Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    protected void setId(int id) {
        this.id = id;
    }

    private int generateId() {
        return ++id;
    }

    @Override
    public Task createTask(Task task) {
        if (isValidTask(task)) {
            System.out.println("Время выполнения задачи пересекается с другими задачами");
            return null;
        }
        task.setId(generateId());
        tasks.put(task.getId(), task);
        addPriorityTask(task);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        if (isValidTask(task)) {
            System.out.println("Время выполнения задачи пересекается с другими задачами");
            return tasks.get(task.getId());
        } else {
            tasks.put(task.getId(), task);
            addPriorityTask(task);
            return task;
        }
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeAllTask() {
        priorityTasks.removeIf(task -> task.getTaskType().equals(TaskType.TASK));
        tasks.clear();
    }

    @Override
    public Task getTask(int id) {
        if (tasks.get(id) == null) {
            return null;
        } else {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
    }

    @Override
    public void removeTask(int id) {
        priorityTasks.remove(tasks.remove(id));
        historyManager.remove(id);
    }

    @Override
    public EpicTask createEpic(EpicTask epicTask) {
        epicTask.setId(generateId());
        epics.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    @Override
    public EpicTask getEpicTask(int id) {
        if (epics.get(id) == null) {
            return null;
        } else {
            historyManager.add(epics.get(id));
            return epics.get(id);
        }
    }

    @Override
    public List<SubTask> getSubTaskEpicTask(EpicTask epicTask) {
        return new ArrayList<>(epicTask.getSubTasks());
    }

    @Override
    public void removeEpicTask(int id) {
        EpicTask epicTaskCopy = epics.remove(id);
        historyManager.remove(id);
        List<SubTask> subTask = epicTaskCopy.getSubTasks();
        for (SubTask subTaskCopy : subTask) {
            subTasks.remove(subTaskCopy.getId());
            historyManager.remove(subTaskCopy.getId());
        }
        subTask.clear();
    }

    @Override
    public List<EpicTask> getAllEpicTask() {
        return epics.values().stream().toList();
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
    public List<SubTask> getAllSubTaskEpic(EpicTask epicTask) {
        return epicTask.getSubTasks();
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        if (isValidTask(subTask)) {
            System.out.println("Время выполнения задачи пересекается с другими задачами");
            return null;
        }
        EpicTask epic = epics.get(subTask.getEpic().getId());
        epic.addSubTask(subTask);
        subTask.setId(generateId());
        updateStatus(epic);
        subTasks.put(subTask.getId(), subTask);
        epic.getStartTime();
        epic.getEndTime();
        epic.getDuration();
        addPriorityTask(subTask);
        return subTask;
    }

    @Override
    public SubTask getSubTask(int id) {
        if (subTasks.get(id) == null) {
            return null;
        } else {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        }
    }

    @Override
    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void removeSubTask(int id) {
        SubTask removeSubTask = subTasks.remove(id);
        priorityTasks.remove(removeSubTask);
        EpicTask epicTask = removeSubTask.getEpic();
        epicTask.getSubTasks().remove(removeSubTask);
        historyManager.remove(id);
        updateStatus(epicTask);
        epicTask.getStartTime();
        epicTask.getEndTime();
        epicTask.getDuration();
    }

    @Override
    public void removeAllSubTask() {
        priorityTasks.removeIf(task -> task.getTaskType().equals(TaskType.SUB_TASK));
        subTasks.clear();
        Collection<EpicTask> epicTasks = epics.values();
        for (EpicTask epicTaskCopy : epicTasks) {
            epicTaskCopy.removeSubTask();
            updateStatus(epicTaskCopy);
            epicTaskCopy.getStartTime();
            epicTaskCopy.getEndTime();
            epicTaskCopy.getDuration();
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (isValidTask(subTask)) {
            System.out.println("Время выполнения задачи пересекается с другими задачами");
        } else {
            if (subTasks.containsKey(subTask.getId())) {
                updateStatus(subTask.getEpic());
                subTasks.put(subTask.getId(), subTask);
                priorityTasks.add(subTask);
            }
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
