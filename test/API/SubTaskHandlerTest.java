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

class SubTaskHandlerTest {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.gson;

    SubTaskHandlerTest() throws IOException {
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
    public void testAddSubTask() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Test 1", "Text", Status.NEW, TaskType.EPIC_TASK);
        manager.createEpic(epicTask);
        SubTask subTask = new SubTask(epicTask,"Test 2", "Testing subtask 2",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 15:00", 30);
        String subtaskJson = gson.toJson(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<SubTask> tasksFromManager = manager.getAllSubTask();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetAllSubTask() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Test 1", "Text", Status.NEW, TaskType.EPIC_TASK);
        manager.createEpic(epicTask);
        SubTask subTask = new SubTask(epicTask,"Test 2", "Testing subtask 2",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 15:00", 30);
        SubTask subTask1 = new SubTask(epicTask, "Test 3", "Testing subtask 3",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 16:00", 30);
        manager.createSubTask(subTask);
        manager.createSubTask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        class SubTaskListTypetoken extends TypeToken<List<SubTask>> {
        }
        List<SubTask> tasksFromManager = gson.fromJson(response.body(), new SubTaskListTypetoken().getType());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals("Test 3", tasksFromManager.get(1).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetSubTaskId() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Test 1", "Text", Status.NEW, TaskType.EPIC_TASK);
        manager.createEpic(epicTask);
        SubTask subTask = new SubTask(epicTask,"Test 2", "Testing subtask 2",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 15:00", 30);
        SubTask acteaulTask = manager.createSubTask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        SubTask jsonTask = gson.fromJson(response.body(), SubTask.class);
        assertNotNull(jsonTask);
        assertEquals(acteaulTask, jsonTask);
    }

    @Test
    public void testUpdateSubTask() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Test 1", "Text", Status.NEW, TaskType.EPIC_TASK);
        manager.createEpic(epicTask);
        SubTask subTask = new SubTask(epicTask,"Test 2", "Testing subtask 2",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 15:00", 30);
        manager.createSubTask(subTask);
        subTask.setName("Test 3");
        String updateTask = gson.toJson(manager.updateSubTask(subTask), SubTask.class);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                POST(HttpRequest.BodyPublishers.ofString(updateTask)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotEquals("Test 2", manager.getSubTask(2).getName(), "Задача не обновилась");
    }

    @Test
    public void testDeleteSubTask() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Test 1", "Text", Status.NEW, TaskType.EPIC_TASK);
        manager.createEpic(epicTask);
        SubTask subTask = new SubTask(epicTask,"Test 2", "Testing subtask 2",
                Status.NEW, TaskType.SUB_TASK, "12.10.2024 15:00", 30);
        manager.createSubTask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(manager.getSubTask(2), "Задача не удалилась");
    }
}
