package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    InMemoryTaskManager memoryTaskManager = new InMemoryTaskManager(historyManager);
    Task task = memoryTaskManager.createTask(new Task("name", "description", Status.NEW));
    EpicTask epicTask = memoryTaskManager.createEpic(new EpicTask("nameEpic", "epicDescription"));
    SubTask subTask = memoryTaskManager.createSubTask(new SubTask(epicTask, "nameSubTask", "subTaskDescription", Status.NEW));
    @Test
    void createTaskNotNull() {
        Assertions.assertNotNull(memoryTaskManager.getTask(task.getId()));
    }

    @Test
    void taskEqualsId() {
        Task task1 = new Task(task.getId(), "name1", "description", Status.DONE);
        Assertions.assertEquals(task, task1);
    }

    @Test
    void subTaskEqualsId() {
        EpicTask epicTask1 = new EpicTask("nameEpic2", "descriptionEpic");
        SubTask subTask1 = new SubTask(epicTask1, "nameCopy", "descriptionCopy", Status.DONE);
        subTask1.setId(subTask.getId());
        Assertions.assertEquals(subTask, subTask1);
    }

    @Test
    void epicTaskEqualsId() {
        EpicTask epicTask1 = new EpicTask("nameEpic2", "descriptionEpic");
        epicTask1.setId(epicTask.getId());
        SubTask subTask1 = new SubTask(epicTask1, "nameCopy", "descriptionCopy", Status.DONE);
        Assertions.assertEquals(epicTask1, epicTask);
    }


}