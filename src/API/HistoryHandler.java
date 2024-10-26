package API;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.HistoryManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    HistoryManager historyManager;

    public HistoryHandler(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equals("GET")) {
                String text = gson.toJson(historyManager.getHistory());
                sendText(exchange, text);
            } else {
                sendNotEndpoint(exchange);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendInternalServerError(exchange);
        }
    }
}
