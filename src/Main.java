import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import model.TaskType;
import service.FileBackedTaskManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;

import java.io.File;


public class Main {

    public static void main(String[] args) {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);

        Task task1 = new Task("NameTask1", "DescriptionTask1", Status.NEW, TaskType.TASK, "12.12.2024 15:00", 30);
        Task task2 = new Task("NameTask2", "DescriptionTask2", Status.DONE, TaskType.TASK, "12.12.2024 16:00", 30);
        EpicTask epicTask1 = new EpicTask("NameEpicTask1", "DescriptionEpicTask1", Status.NEW, TaskType.EPIC_TASK);
        SubTask subTask1 = new SubTask(epicTask1, "NameSubTask1", "DescriptionSubTask1", Status.DONE, TaskType.SUB_TASK, "12.12.2024 17:00", 30);
        SubTask subTask2 = new SubTask(epicTask1, "NameSubTask2", "DescriptionSubTask2", Status.DONE, TaskType.SUB_TASK, "12.12.2024 18:00", 30);
        SubTask subTask3 = new SubTask(epicTask1, "NameSubTask3", "DescriptionSubTask3", Status.DONE, TaskType.SUB_TASK, "12.12.2024 19:00", 30);
        EpicTask epicTask2 = new EpicTask("NameEpicTask2", "DescriptionEpicTask2", Status.NEW, TaskType.EPIC_TASK);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epicTask1);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        taskManager.createEpic(epicTask2);


        taskManager.getTask(task2.getId());
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println();
        taskManager.getEpicTask(epicTask2.getId());
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println();
        taskManager.getSubTask(subTask1.getId());
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println();
        taskManager.getSubTask(subTask3.getId());
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println();
        taskManager.getEpicTask(epicTask1.getId());
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println();
        taskManager.getSubTask(subTask2.getId());
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println();
        taskManager.getTask(task1.getId());
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println();
        taskManager.getSubTask(subTask1.getId());
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println();
        taskManager.getTask(task2.getId());
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println();

        taskManager.removeTask(task2.getId());
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println();

        taskManager.removeEpicTask(epicTask1.getId());
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println();


        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("history.csv", Managers.getDefaultHistory());
        fileBackedTaskManager.createTask(new Task("Task1", "Description", Status.NEW, TaskType.TASK, "12.10.2024 15:00", 130));
        EpicTask epicTask = fileBackedTaskManager.createEpic(new EpicTask("EpicTask1", "DescriptionEpicTask", Status.NEW, TaskType.EPIC_TASK));
        SubTask subTask = fileBackedTaskManager.createSubTask(new SubTask(epicTask, "SubTask", "DescriptionSubTask", Status.NEW, TaskType.SUB_TASK, "12.10.2024 19:00", 140));
        fileBackedTaskManager.removeAllTask();
        fileBackedTaskManager.removeSubTask(subTask.getId());
        System.out.println(fileBackedTaskManager.getAllTask());
        System.out.println(fileBackedTaskManager.getAllEpicTask());
        System.out.println(fileBackedTaskManager.getAllSubTask());
        System.out.println(fileBackedTaskManager.getAllEpicTask());
        System.out.println(fileBackedTaskManager.getPrioritizedTasks());
        System.out.println();
        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(new File("history.csv"));
        System.out.println(fileBackedTaskManager1.getAllTask());
        System.out.println(fileBackedTaskManager1.getAllEpicTask());
        System.out.println(fileBackedTaskManager1.getAllSubTask());
        System.out.println(fileBackedTaskManager1.getAllEpicTask());
    }
}
