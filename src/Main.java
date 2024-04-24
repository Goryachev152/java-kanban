import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import service.HistoryManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);


        Task task = taskManager.createTask(new Task("Трекер задач", "Написать код", Status.NEW));
        System.out.println(taskManager.getTask(task.getId()));
        System.out.println();
        System.out.println();
        task.setDescription("Код написан");
        task.setStatus(Status.DONE);
        taskManager.updateTask(task);
        System.out.println(taskManager.getTask(task.getId()));
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println();
        taskManager.removeTask(task.getId());
        System.out.println(taskManager.getTask(task.getId()));
        System.out.println();
        System.out.println();
        System.out.println(taskManager.getAllTask());
        System.out.println();
        System.out.println();




        EpicTask epicTask = taskManager.createEpic(new EpicTask("Провервка работы", "отправить работу на проверку"));
        SubTask subTask = taskManager.createSubTask(new SubTask(epicTask, "Трекер задач", "Написать код", Status.NEW));
        SubTask subTask1 = taskManager.createSubTask(new SubTask(epicTask, "Тест", "Проверить работу кода",Status.IN_PROGRESS));
        SubTask subTask2 = taskManager.createSubTask(new SubTask(epicTask, "Сдача работы", "Загрузить код на github", Status.DONE));
        System.out.println(taskManager.getEpicTask(epicTask.getId()));
        System.out.println();
        System.out.println();
        System.out.println(taskManager.getSubTask(subTask.getId()));
        System.out.println(taskManager.getSubTask(subTask1.getId()));
        System.out.println(taskManager.getSubTask(subTask2.getId()));
        System.out.println();
        System.out.println();
        System.out.println(taskManager.getSubTaskEpicTask(epicTask));
        System.out.println();
        System.out.println();
        subTask.setStatus(Status.DONE);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);
        System.out.println(taskManager.getEpicTask(epicTask.getId()));
        System.out.println();
        System.out.println();
        taskManager.removeSubTask(subTask.getId());
        System.out.println(taskManager.getEpicTask(epicTask.getId()));
        System.out.println();
        System.out.println();
        taskManager.removeAllSubTask();
        System.out.println(taskManager.getEpicTask(epicTask.getId()));
        System.out.println();
        System.out.println();
        System.out.println(taskManager.getAllTask());

        EpicTask epicTask1 = taskManager.createEpic(new EpicTask("Проверка", "Удалить эпик"));
        SubTask subTask3 = taskManager.createSubTask(new SubTask(epicTask1, "Подзадача", "Удалить эпик", Status.NEW));

        taskManager.removeEpicTask(epicTask1.getId());
        System.out.println(taskManager.getAllEpicTask());
        System.out.println(taskManager.getAllTask());






    }
}
