package service;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {

    private InMemoryHistoryManager historyManager;
    private InMemoryTaskManager memoryTaskManager;
    private Task task;
    private EpicTask epicTask;
    private SubTask subTask;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        memoryTaskManager = new InMemoryTaskManager(historyManager);
        task = memoryTaskManager.createTask(new Task("name", "description", Status.NEW, TaskType.TASK, "13.10.2024 18:00", 150));
        epicTask = memoryTaskManager.createEpic(new EpicTask("nameEpic", "epicDescription", Status.NEW, TaskType.EPIC_TASK));
        subTask = memoryTaskManager.createSubTask(new SubTask(epicTask, "nameSubTask", "subTaskDescription", Status.NEW, TaskType.SUB_TASK, "14.10.2024 15:00", 120));
    }

    @Test
    void taskEqualsId() {
        Task task1 = new Task(task.getId(), "name1", "description", Status.DONE, TaskType.TASK, "15.10.2024 16:00", 120);
        Assertions.assertEquals(task, task1);
    }

    @Test
    void subTaskEqualsId() {
        EpicTask epicTask1 = new EpicTask("nameEpic2", "descriptionEpic", Status.NEW, TaskType.EPIC_TASK);
        SubTask subTask1 = new SubTask(epicTask1, "nameCopy", "descriptionCopy", Status.DONE, TaskType.SUB_TASK, "17.10.2024 15:00", 120);
        subTask1.setId(subTask.getId());
        Assertions.assertEquals(subTask, subTask1);
    }

    @Test
    void epicTaskEqualsId() {
        EpicTask epicTask1 = new EpicTask("nameEpic2", "descriptionEpic", Status.NEW, TaskType.EPIC_TASK);
        epicTask1.setId(epicTask.getId());
        SubTask subTask1 = new SubTask(epicTask1, "nameCopy", "descriptionCopy", Status.DONE, TaskType.SUB_TASK, "17.10.2024 15:00", 120);
        Assertions.assertEquals(epicTask1, epicTask);
    }

    @Test
    void isValidTaskNotAdd() {
        Task task1 = memoryTaskManager.createTask(new Task("name1", "Description", Status.NEW, TaskType.TASK, "13.10.2024 19:00", 30));
        Assertions.assertEquals(memoryTaskManager.getPrioritizedTasks().size(), 2, "Задача пересекается временем с другой задачей");
    }

    @Test
    void getPrioritizedTasksEquals() {
        List<Task> copy = List.of(task, subTask);
        Assertions.assertEquals(memoryTaskManager.getPrioritizedTasks(), copy);
    }
}