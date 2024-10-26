package API;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.TaskType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class EpicHandlerTest {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.gson;

    public EpicHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.removeAllTask();
        manager.removeAllSubTask();
        manager.removeAllEpicTask();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddEpicTask() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Test 2", "Testing task 2",
                Status.NEW, TaskType.EPIC_TASK);
        String taskJson = gson.toJson(epicTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<EpicTask> tasksFromManager = manager.getAllEpicTask();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetAllEpicTask() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Test 2", "Testing task 2",
                Status.NEW, TaskType.EPIC_TASK);
        EpicTask epicTask1 = new EpicTask("Test 3", "Testing task 3",
                Status.NEW, TaskType.EPIC_TASK);
        manager.createEpic(epicTask);
        manager.createEpic(epicTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        class EpicTaskListTypetoken extends TypeToken<List<EpicTask>> {
        }
        List<EpicTask> tasksFromManager = gson.fromJson(response.body(), new EpicTaskListTypetoken().getType());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals("Test 3", tasksFromManager.get(1).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetEpicTaskId() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Test 2", "Testing task 2",
                Status.NEW, TaskType.EPIC_TASK);
        EpicTask acteaulTask = manager.createEpic(epicTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        EpicTask jsonTask = gson.fromJson(response.body(), EpicTask.class);
        assertNotNull(jsonTask);
        assertEquals(acteaulTask, jsonTask);
    }

    @Test
    public void testUpdateEpicTask() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Test 2", "Testing task 2",
                Status.NEW, TaskType.EPIC_TASK);
        manager.createEpic(epicTask);
        epicTask.setName("Test 1");
        String updateTask = gson.toJson(manager.updateEpicTask(epicTask), EpicTask.class);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                POST(HttpRequest.BodyPublishers.ofString(updateTask)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotEquals("Test 2", manager.getEpicTask(1).getName(), "Задача не обновилась");
    }

    @Test
    public void testDeleteEpicTask() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Test 2", "Testing task 2",
                Status.NEW, TaskType.EPIC_TASK);
        manager.createEpic(epicTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(manager.getEpicTask(1), "Задача не удалилась");
    }

    @Test
    public void testGetEpicSubTasks() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Test 2", "Testing task 2",
                Status.NEW, TaskType.EPIC_TASK);
        manager.createEpic(epicTask);
        SubTask subTask = new SubTask(epicTask,"Test 3", "Testing subtask 2",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 15:00", 30);
        manager.createSubTask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        class SubTaskListTypetoken extends TypeToken<List<SubTask>> {
        }
        List<SubTask> tasksFromManager = gson.fromJson(response.body(), new SubTaskListTypetoken().getType());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals("Test 3", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }
}
