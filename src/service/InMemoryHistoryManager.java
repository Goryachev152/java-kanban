package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    HashMap<Integer, Node> historyMap = new HashMap<>();
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

    private ArrayList<Task> getTask() {
        ArrayList<Task> history = new ArrayList<>();
        Node current = first;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    private void removeNode(Node node) {
        if (node != null) {
            if (node.next == null) {
                node.prev.next = null;
                last = node.prev;
            } else {
                node.next.prev = node.prev;
            }
            if (node.prev == null) {
                node.next.prev = null;
                first = node.next;
            } else {
                node.prev.next = node.next;
            }
            historyMap.remove(node.task.getId());
        }
    }

    @Override
    public void remove(int id) {
        Node remove = historyMap.get(id);
        removeNode(remove);
    }

    @Override
    public void add(Task task) {
        Node node = historyMap.get(task.getId());
        removeNode(node);
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTask();
    }
}
