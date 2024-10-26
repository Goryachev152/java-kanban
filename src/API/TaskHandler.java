package API;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String requestMethod = exchange.getRequestMethod();
            String requestPath = exchange.getRequestURI().getPath();
            Endpoint endpoint = getEndpoint(requestPath, requestMethod);
            String text;
            int id;
            switch (endpoint) {
                case GET_ALL_TASK:
                    text = gson.toJson(taskManager.getAllTask());
                    sendText(exchange, text);
                    break;
                case GET_TASK:
                    Optional<Integer> getId = getPostId(exchange);
                    if (getId.isEmpty()) {
                        sendNotFound(exchange);
                    }
                    id = getId.get();
                    Task task = taskManager.getTask(id);
                    if (task != null) {
                        text = gson.toJson(task, Task.class);
                        sendText(exchange, text);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case POST_TASK:
                    String bodyTask = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Task newTask = gson.fromJson(bodyTask, Task.class);
                    if (taskManager.isValidTask(newTask)) {
                        sendHasInteractions(exchange);
                    } else {
                        taskManager.createTask(newTask);
                        text = "Задача добавлена";
                        sendPost(exchange, text);
                    }
                    break;
                case POST_UPDATE:
                    String bodyUpdateTask = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Task updateTask = taskManager.updateTask(gson.fromJson(bodyUpdateTask, Task.class));
                    if (updateTask != null) {
                        if (taskManager.isValidTask(updateTask)) {
                            sendHasInteractions(exchange);
                        } else {
                            text = "Задача обновлена";
                            sendPost(exchange, text);
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case DELETE:
                    Optional<Integer> deleteId = getPostId(exchange);
                    if (deleteId.isEmpty()) {
                        sendNotFound(exchange);
                    }
                    id = deleteId.get();
                    Task remove = taskManager.removeTask(id);
                    if (remove != null) {
                        sendDelete(exchange);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case UNKNOWN:
                    sendNotEndpoint(exchange);
            }
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private Optional<Integer> getPostId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }
}
