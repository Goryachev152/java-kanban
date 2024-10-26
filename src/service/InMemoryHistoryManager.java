package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    public class Node {
        Task task;
        Node next;
        Node prev;

        Node(Task task, Node next, Node prev) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }
    }

    Map<Integer, Node> historyMap = new HashMap<>();
    Node first;
    Node last;

    private void linkLast(Task task) {
        final Node lastNodeSave = last;
        final Node newNode = new Node(task, null, lastNodeSave);
        last = newNode;
        if (lastNodeSave == null) {
            first = newNode;
        } else {
            lastNodeSave.next = newNode;
        }
        historyMap.put(task.getId(), newNode);
    }

    private List<Task> getTask() {
        List<Task> history = new ArrayList<>();
        Node current = first;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node.next != null && node.prev != null) {
            node.next.prev = node.prev;
            node.prev.next = node.next;
        }
        if (node.next == null && node.prev != null) {
            node.prev.next = null;
            last = node.prev;
        }
        if (node.prev == null && node.next != null) {
            node.next.prev = null;
            first = node.next;
        }
        if (node.prev == null && node.next == null) {
            first = null;
            last = null;
        }
    }

    @Override
    public void remove(int id) {
        Node remove = historyMap.remove(id);
        removeNode(remove);
    }

    @Override
    public void add(Task task) {
        historyMap.get(task.getId());
        remove(task.getId());
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTask();
    }
}
