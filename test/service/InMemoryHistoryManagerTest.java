package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


class InMemoryHistoryManagerTest {

    private static InMemoryHistoryManager historyManager;
    private static InMemoryTaskManager memoryTaskManager;
    private static Task task;
    private static EpicTask epicTask;
    private static SubTask subTask;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        memoryTaskManager = new InMemoryTaskManager(historyManager);
        task = memoryTaskManager.createTask(new Task("name", "description", Status.NEW));
        epicTask = memoryTaskManager.createEpic(new EpicTask("nameEpic", "epicDescription"));
        subTask = memoryTaskManager.createSubTask(new SubTask(epicTask, "nameSubTask", "subTaskDescription", Status.NEW));
    }

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
        Assertions.assertEquals(copyHistory.getFirst(), historyManager.getHistory().getFirst());
        Assertions.assertEquals(historyManager.getHistory().size(), copyHistory.size());
    }

    @Test
    void checkRemoveNode() {
        memoryTaskManager.getTask(task.getId());
        memoryTaskManager.getEpicTask(epicTask.getId());
        historyManager.remove(task.getId());
        Assertions.assertEquals(memoryTaskManager.getAllEpicTask(),historyManager.getHistory());
    }

}