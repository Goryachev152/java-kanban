package API;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Status;
import model.Task;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PrioritizedHandlerTest {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.gson;

    public PrioritizedHandlerTest() throws IOException {
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
    public void testGetPrioritized() throws IOException, InterruptedException {
        Task task1 = new Task("Test 1", "Testing task 1",
                Status.NEW, TaskType.TASK, "12.10.2024 15:00", 30);
        Task task2 = new Task("Test 2", "Testing task 2",
                Status.NEW, TaskType.TASK, "12.10.2024 16:00", 30);
        Task task3 = new Task("Test 3", "Testing task 3",
                Status.NEW, TaskType.TASK, "12.10.2024 17:00", 30);
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.getTask(0);
        manager.getTask(1);
        manager.getTask(2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        class TaskListTypetoken extends TypeToken<List<Task>> {
        }
        List<Task> tasks = gson.fromJson(response.body(), new TaskListTypetoken().getType());
        assertNotNull(tasks);
        assertEquals(3, tasks.size());
        assertEquals("Test 1", tasks.get(0).getName());
        assertEquals("Test 2", tasks.get(1).getName());
        assertEquals("Test 3", tasks.get(2).getName());
    }
}
