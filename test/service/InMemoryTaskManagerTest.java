package service;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest {

    private InMemoryHistoryManager historyManager;
    private InMemoryTaskManager memoryTaskManager;
    private Task task;
    private EpicTask epicTask;
    private SubTask subTask;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        memoryTaskManager = new InMemoryTaskManager(historyManager);
        task = memoryTaskManager.createTask(new Task("name", "description", Status.NEW, TaskType.TASK));
        epicTask = memoryTaskManager.createEpic(new EpicTask("nameEpic", "epicDescription", Status.NEW, TaskType.EPIC_TASK));
        subTask = memoryTaskManager.createSubTask(new SubTask(epicTask, "nameSubTask", "subTaskDescription", Status.NEW, TaskType.SUB_TASK));
    }

    @Test
    void createTaskNotNull() {
        Assertions.assertNotNull(memoryTaskManager.getTask(task.getId()));
    }

    @Test
    void taskEqualsId() {
        Task task1 = new Task(task.getId(), "name1", "description", Status.DONE, TaskType.TASK);
        Assertions.assertEquals(task, task1);
    }

    @Test
    void subTaskEqualsId() {
        EpicTask epicTask1 = new EpicTask("nameEpic2", "descriptionEpic", Status.NEW, TaskType.EPIC_TASK);
        SubTask subTask1 = new SubTask(epicTask1, "nameCopy", "descriptionCopy", Status.DONE, TaskType.SUB_TASK);
        subTask1.setId(subTask.getId());
        Assertions.assertEquals(subTask, subTask1);
    }

    @Test
    void epicTaskEqualsId() {
        EpicTask epicTask1 = new EpicTask("nameEpic2", "descriptionEpic", Status.NEW, TaskType.EPIC_TASK);
        epicTask1.setId(epicTask.getId());
        SubTask subTask1 = new SubTask(epicTask1, "nameCopy", "descriptionCopy", Status.DONE, TaskType.SUB_TASK);
        Assertions.assertEquals(epicTask1, epicTask);
    }


}