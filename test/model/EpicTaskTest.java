package model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

class EpicTaskTest {
    protected TaskManager taskManager = Managers.getDefault();

    @Test
    void AllSubTaskStatusNew() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW,
                TaskType.EPIC_TASK));
        SubTask subTask1 = taskManager.createSubTask(new SubTask(epicTask, "SubTask1", "Description",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 15:00", 20));
        SubTask subTask2 = taskManager.createSubTask(new SubTask(epicTask, "SubTask2", "Description",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 16:00", 20));
        Assertions.assertEquals(epicTask.getStatus(), Status.NEW, "Статус епика не проходит");
    }

    @Test
    void AllSubTaskStatusDone() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW,
                TaskType.EPIC_TASK));
        SubTask subTask1 = taskManager.createSubTask(new SubTask(epicTask, "SubTask1", "Description",
                Status.DONE, TaskType.SUB_TASK, "12.10.2024 15:00", 20));
        SubTask subTask2 = taskManager.createSubTask(new SubTask(epicTask, "SubTask2", "Description",
                Status.DONE, TaskType.SUB_TASK, "12.10.2024 16:00", 20));
        Assertions.assertEquals(epicTask.getStatus(), Status.DONE, "Статус епика не проходит");
    }

    @Test
    void AllSubTaskStatusInProgress() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW,
                TaskType.EPIC_TASK));
        SubTask subTask1 = taskManager.createSubTask(new SubTask(epicTask, "SubTask1", "Description",
                Status.IN_PROGRESS, TaskType.SUB_TASK, "12.10.2024 15:00", 20));
        SubTask subTask2 = taskManager.createSubTask(new SubTask(epicTask, "SubTask2", "Description",
                Status.IN_PROGRESS, TaskType.SUB_TASK, "12.10.2024 16:00", 20));
        Assertions.assertEquals(epicTask.getStatus(), Status.IN_PROGRESS, "Статус епика не проходит");
    }

    @Test
    void AllSubTaskStatusNewAndDone() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW,
                TaskType.EPIC_TASK));
        SubTask subTask1 = taskManager.createSubTask(new SubTask(epicTask, "SubTask1", "Description",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 15:00", 20));
        SubTask subTask2 = taskManager.createSubTask(new SubTask(epicTask, "SubTask2", "Description",
                Status.DONE, TaskType.SUB_TASK, "12.10.2024 16:00", 20));
        Assertions.assertEquals(epicTask.getStatus(), Status.NEW, "Статус епика не проходит");
    }
}