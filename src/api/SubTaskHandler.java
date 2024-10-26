package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager taskManager;

    public SubTaskHandler(TaskManager taskManager) {
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
                    text = gson.toJson(taskManager.getAllSubTask());
                    sendText(exchange, text);
                    break;
                case GET_TASK:
                    Optional<Integer> getId = getPostId(exchange);
                    if (getId.isEmpty()) {
                        sendNotFound(exchange);
                    }
                    id = getId.get();
                    SubTask subTask = taskManager.getSubTask(id);
                    if (subTask != null) {
                        text = gson.toJson(subTask, SubTask.class);
                        sendText(exchange, text);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case POST_TASK:
                    String bodyTask = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    SubTask newTask = gson.fromJson(bodyTask, SubTask.class);
                    if (taskManager.isValidTask(newTask)) {
                        sendHasInteractions(exchange);
                    } else {
                        taskManager.createSubTask(newTask);
                        text = "Задача добавлена";
                        sendPost(exchange, text);
                    }
                    break;
                case POST_UPDATE:
                    String bodyUpdateTask = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    SubTask updateTask = taskManager.updateSubTask(gson.fromJson(bodyUpdateTask, SubTask.class));
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
                    SubTask remove = taskManager.removeSubTask(id);
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
            e.printStackTrace();
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

