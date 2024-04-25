package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_HISTORY = 10;
    List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == MAX_HISTORY) {
            history.removeFirst();
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
