package service;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

class FileBackedTaskManagerTest extends TaskManagerTest<TaskManager> {

    private FileBackedTaskManager fileBackedTaskManager;

    private Task task1;

    private EpicTask epicTask1;

    @BeforeEach
    public void beforeEach() {
        fileBackedTaskManager = new FileBackedTaskManager("testHistory.csv", Managers.getDefaultHistory());
        task1 = fileBackedTaskManager.createTask(new Task("testName", "testDescription", Status.NEW, TaskType.TASK, "12.10.2024 15:00", 120));
        epicTask1 = fileBackedTaskManager.createEpic((new EpicTask("testNameEpic", "testDescriptionEpic", Status.NEW, TaskType.EPIC_TASK)));
    }

    @Test
    void savingTaskFile() {
        String task = task1.toCsvString();
        String epicTask = epicTask1.toCsvString();
        String[] list = new String[2];
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("testHistory.csv", StandardCharsets.UTF_8))) {
            bufferedReader.readLine();
            list[0] = bufferedReader.readLine();
            list[1] = bufferedReader.readLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалост загрузить файл");
        }
        Assertions.assertEquals(task, list[0], "Задачи не совпадают");
        Assertions.assertEquals(epicTask, list[1], "Задачи не совпадают");
    }

    @Test
    void loadingTaskFile() {
        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(new File("testHistory.csv"));
        Assertions.assertEquals(fileBackedTaskManager.getAllTask(), fileBackedTaskManager1.getAllTask(), "Задачи не совпадают");
        Assertions.assertEquals(fileBackedTaskManager.getAllEpicTask(), fileBackedTaskManager1.getAllEpicTask(), "Задачи не совпадают");
    }

    @Test
    void throwsExceptionLoadWrongFile() {
        Assertions.assertThrows(ManagerSaveException.class, () -> FileBackedTaskManager.loadFromFile(new File("fileWrong")));
    }
}
