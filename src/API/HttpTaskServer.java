package API;

import com.sun.net.httpserver.HttpServer;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        HistoryManager historyManager = taskManager.getHistoryManager();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/subtasks", new SubTaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(historyManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void start() {
        httpServer.start();
        System.out.println("Server started");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Server stopped");
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();

        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
    }
}
