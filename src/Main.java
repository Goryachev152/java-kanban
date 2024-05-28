import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);

        Task task1 = new Task("NameTask1", "DescriptionTask1", Status.NEW);
        Task task2 = new Task("NameTask2", "DescriptionTask2", Status.DONE);
        EpicTask epicTask1 = new EpicTask("NameEpicTask1", "DescriptionEpicTask1");
        SubTask subTask1 = new SubTask(epicTask1, "NameSubTask1", "DescriptionSubTask1", Status.DONE);
        SubTask subTask2 = new SubTask(epicTask1, "NameSubTask2", "DescriptionSubTask2", Status.DONE);
        SubTask subTask3 = new SubTask(epicTask1, "NameSubTask3", "DescriptionSubTask3", Status.DONE);
        EpicTask epicTask2 = new EpicTask("NameEpicTask2", "DescriptionEpicTask2");
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
    }
}
