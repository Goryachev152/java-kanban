package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.EpicTask;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
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
                    text = gson.toJson(taskManager.getAllEpicTask());
                    sendText(exchange, text);
                    break;
                case GET_TASK:
                    Optional<Integer> getId = getPostId(exchange);
                    if (getId.isEmpty()) {
                        sendNotFound(exchange);
                    }
                    id = getId.get();
                    EpicTask epicTask = taskManager.getEpicTask(id);
                    if (epicTask != null) {
                        text = gson.toJson(epicTask, EpicTask.class);
                        sendText(exchange, text);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case GET_EPIC_SUBTASK:
                    Optional<Integer> getEpicId = getPostId(exchange);
                    if (getEpicId.isEmpty()) {
                        sendNotFound(exchange);
                    }
                    id = getEpicId.get();
                    EpicTask epicTask1 = taskManager.getEpicTask(id);
                    if (epicTask1 != null) {
                        text = gson.toJson(taskManager.getSubTaskEpicTask(epicTask1));
                        sendText(exchange, text);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case POST_TASK:
                    String bodyTask = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    EpicTask newTask = gson.fromJson(bodyTask, EpicTask.class);
                    taskManager.createEpic(newTask);
                    text = "Задача добавлена";
                    sendPost(exchange, text);
                    break;
                case POST_UPDATE:
                    String bodyUpdateTask = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    EpicTask updateTask = taskManager.updateEpicTask(gson.fromJson(bodyUpdateTask, EpicTask.class));
                    if (updateTask != null) {
                        text = "Задача обновлена";
                        sendPost(exchange, text);
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
                    EpicTask remove = taskManager.removeEpicTask(id);
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

