package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import model.EpicTask;
import model.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseHttpHandler {

    protected static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(EpicTask.class, new EpicTaskAdapter())
            .create();

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendPost(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(201, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        String text = "объект не был найден";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(404, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        String text = "задача пересекается с уже существующими";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(406, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendInternalServerError(HttpExchange exchange) throws IOException {
        String text = "произошла ошибка при обработке запроса";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(500, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendDelete(HttpExchange exchange) throws IOException {
        String text = "Задача удалена";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotEndpoint(HttpExchange exchange) throws IOException {
        String text = "такого эндпоинта не сущетствует";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(404, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
            jsonWriter.value(localDateTime.format(TIME_FORMATTER));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), TIME_FORMATTER);
        }
    }

    static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
            jsonWriter.value(duration.toMinutes());
        }

        @Override
        public Duration read(final JsonReader jsonReader) throws IOException {
            return Duration.ofMinutes(Long.parseLong(jsonReader.nextString()));
        }
    }

    protected static Endpoint getEndpoint (String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (requestMethod.equals("GET") && pathParts.length < 3) {
            return Endpoint.GET_ALL_TASK;
        } else if (requestMethod.equals("GET") && pathParts.length == 3) {
            return Endpoint.GET_TASK;
        } else if (requestMethod.equals("GET") && pathParts.length == 4) {
            return Endpoint.GET_EPIC_SUBTASK;
        } else if (requestMethod.equals("POST") && pathParts.length < 3) {
            return Endpoint.POST_TASK;
        } else if (requestMethod.equals("POST") && pathParts.length == 3) {
            return Endpoint.POST_UPDATE;
        } else if (requestMethod.equals("DELETE") && pathParts.length == 3) {
            return Endpoint.DELETE;
        } else {
            return Endpoint.UNKNOWN;
        }
    }
}
