package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == 10) {
            history.removeFirst();
            history.add(task);
        } else {
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
