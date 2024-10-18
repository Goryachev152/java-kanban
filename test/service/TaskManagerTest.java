package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import model.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

abstract class TaskManagerTest<T extends TaskManager> {

    protected TaskManager taskManager = Managers.getDefault();

    @Test
    void createTask_notNull() {
        Task task = taskManager.createTask(new Task("Name", "Description", Status.NEW, TaskType.TASK, "12.10.2024 15:00", 20));
        Assertions.assertNotNull(taskManager.getTask(task.getId()), "Задачи не существует");
    }

    @Test
    void updateTask_true() {
        Task task = taskManager.createTask(new Task("Name", "Description", Status.NEW, TaskType.TASK, "12.10.2024 15:00", 20));
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
        Assertions.assertEquals(task.getStatus(), Status.IN_PROGRESS, "Задача не обновилась");
    }

    @Test
    void getAllTask_notNull() {
        Task task = taskManager.createTask(new Task("Name", "Description", Status.NEW, TaskType.TASK, "12.10.2024 15:00", 20));
        Assertions.assertNotNull(taskManager.getAllTask(), "Задачи не сущетсвует");
    }

    @Test
    void removeAllTask_listEmpty() {
        Task task = taskManager.createTask(new Task("Name", "Description", Status.NEW, TaskType.TASK, "12.10.2024 15:00", 20));
        taskManager.removeAllTask();
        Assertions.assertTrue(taskManager.getAllTask().isEmpty(), "Задача не удалилась");
    }

    @Test
    void getTask_notNull() {
        Task task = taskManager.createTask(new Task("Name", "Description", Status.NEW, TaskType.TASK, "12.10.2024 15:00", 20));
        Assertions.assertNotNull(taskManager.getTask(task.getId()), "Задачи не сущетсвует");
    }

    @Test
    void removeTask_taskNull() {
        Task task = taskManager.createTask(new Task("Name", "Description", Status.NEW, TaskType.TASK, "12.10.2024 15:00", 20));
        taskManager.removeTask(task.getId());
        Assertions.assertNull(taskManager.getTask(task.getId()), "Задача не удалилась");
    }

    @Test
    void createEpic_notNull() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW, TaskType.EPIC_TASK));
        Assertions.assertNotNull(taskManager.getEpicTask(epicTask.getId()));
    }

    @Test
    void getEpic_notNull() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW, TaskType.EPIC_TASK));
        Assertions.assertNotNull(taskManager.getEpicTask(epicTask.getId()),"Задачи не существует");
    }

    @Test
    void getSubTaskEpicTask_notNull() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW, TaskType.EPIC_TASK));
        SubTask subTask = taskManager.createSubTask(new SubTask(epicTask, "SubTask", "Descruption",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 18:00", 30));
        Assertions.assertNotNull(taskManager.getSubTaskEpicTask(epicTask), "Задачи не существует");
    }

    @Test
    void removeEpicTask_epicNull() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW, TaskType.EPIC_TASK));
        SubTask subTask = taskManager.createSubTask(new SubTask(epicTask, "SubTask", "Descruption",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 18:00", 30));
        taskManager.removeEpicTask(epicTask.getId());
        Assertions.assertNull(taskManager.getEpicTask(epicTask.getId()), "Задача не удалилась");
        Assertions.assertNull(taskManager.getSubTask(subTask.getId()), "Задача не удалилась");
    }

    @Test
    void getAllEpicTask_notNull() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW, TaskType.EPIC_TASK));
        Assertions.assertNotNull(taskManager.getAllEpicTask(), "Задачи не сущетствует");
    }

    @Test
    void removeAllEpicTask_epicNull() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW, TaskType.EPIC_TASK));
        SubTask subTask = taskManager.createSubTask(new SubTask(epicTask, "SubTask", "Descruption",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 18:00", 30));
        taskManager.removeEpicTask(epicTask.getId());
        taskManager.removeAllEpicTask();
        Assertions.assertNull(taskManager.getEpicTask(epicTask.getId()), "Задача не удалилась");
        Assertions.assertNull(taskManager.getSubTask(subTask.getId()), "Задача не удалилась");
    }

    @Test
    void updateEpicTask_true() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW, TaskType.EPIC_TASK));
        SubTask subTask = taskManager.createSubTask(new SubTask(epicTask, "SubTask", "Descruption",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 18:00", 30));
        subTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask);
        taskManager.updateEpicTask(epicTask);
        Assertions.assertEquals(epicTask.getStatus(), Status.IN_PROGRESS, "Задача не обновилась");
    }

    @Test
    void getAllSubTaskEpic_notNull() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW, TaskType.EPIC_TASK));
        SubTask subTask = taskManager.createSubTask(new SubTask(epicTask, "SubTask", "Descruption",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 18:00", 30));
        Assertions.assertNotNull(taskManager.getAllSubTaskEpic(epicTask), "Задачи не существует");
    }

    @Test
    void createSubTask_notNull() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW, TaskType.EPIC_TASK));
        SubTask subTask = taskManager.createSubTask(new SubTask(epicTask, "SubTask", "Descruption",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 18:00", 30));
        Assertions.assertNotNull(taskManager.getSubTask(subTask.getId()), "Задачи не существует");
    }

    @Test
    void getSubTask_notNull() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW, TaskType.EPIC_TASK));
        SubTask subTask = taskManager.createSubTask(new SubTask(epicTask, "SubTask", "Descruption",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 18:00", 30));
        Assertions.assertNotNull(taskManager.getSubTask(subTask.getId()), "Задачи не существует");
    }

    @Test
    void getAllSubTask_notNull() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW, TaskType.EPIC_TASK));
        SubTask subTask = taskManager.createSubTask(new SubTask(epicTask, "SubTask", "Descruption",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 18:00", 30));
        Assertions.assertNotNull(taskManager.getAllSubTask(), "Задачи не существует");
    }

    @Test
    void removeSubTask_subTaskNull() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW, TaskType.EPIC_TASK));
        SubTask subTask = taskManager.createSubTask(new SubTask(epicTask, "SubTask", "Descruption",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 18:00", 30));
        taskManager.removeSubTask(subTask.getId());
        Assertions.assertNull(taskManager.getSubTask(subTask.getId()), "Задачи не удалилась");
    }

    @Test
    void removeAllSubTask_subTaskNull() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW, TaskType.EPIC_TASK));
        SubTask subTask = taskManager.createSubTask(new SubTask(epicTask, "SubTask", "Descruption",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 18:00", 30));
        taskManager.removeAllSubTask();
        Assertions.assertNull(taskManager.getSubTask(subTask.getId()), "Задачи не удалилась");
    }

    @Test
    void updateSubTask_true() {
        EpicTask epicTask = taskManager.createEpic(new EpicTask("Epic", "Description", Status.NEW, TaskType.EPIC_TASK));
        SubTask subTask = taskManager.createSubTask(new SubTask(epicTask, "SubTask", "Descruption",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 18:00", 30));
        subTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask);
        Assertions.assertEquals(subTask.getStatus(), Status.IN_PROGRESS, "Задача не обновилась");
    }
}