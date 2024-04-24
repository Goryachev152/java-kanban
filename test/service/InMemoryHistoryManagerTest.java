package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    InMemoryTaskManager memoryTaskManager = new InMemoryTaskManager(historyManager);
    Task task = memoryTaskManager.createTask(new Task("name", "description", Status.NEW));
    EpicTask epicTask = memoryTaskManager.createEpic(new EpicTask("nameEpic", "epicDescription"));
    SubTask subTask = memoryTaskManager.createSubTask(new SubTask(epicTask, "nameSubTask", "subTaskDescription", Status.NEW));

    @Test
    void savedAddHistory() {
        memoryTaskManager.getTask(task.getId());
        memoryTaskManager.getSubTask(subTask.getId());
        memoryTaskManager.getEpicTask(epicTask.getId());
        Assertions.assertNotNull(historyManager.getHistory());
    }

    @Test
    void equalsGetHistory() {
        List<Task> copyHistory = new ArrayList<>();
        memoryTaskManager.getTask(task.getId());
        memoryTaskManager.getSubTask(subTask.getId());
        memoryTaskManager.getEpicTask(epicTask.getId());
        copyHistory.add(task);
        copyHistory.add(subTask);
        copyHistory.add(epicTask);
        Assertions.assertEquals(copyHistory.get(0), historyManager.getHistory().get(0));
        Assertions.assertEquals(historyManager.getHistory().size(), copyHistory.size());
    }
}