package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head; //Указатель на первый элемент списка.
    private Node<Task> tail; //Указатель на последний элемент списка.
    private int size = 0; //Количество элементов в списке.
    private final HashMap<Integer, Node<Task>> inMemoryHistory;

    public InMemoryHistoryManager() {
        inMemoryHistory = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            if (inMemoryHistory.containsKey(task.getIdNumber())) {
                removeNode(inMemoryHistory.get(task.getIdNumber()));
                inMemoryHistory.remove(task.getIdNumber());
            }
            linkLast(task);
            inMemoryHistory.put(task.getIdNumber(), tail);
        }
    }

    @Override
    public void add(Task[] tasks) {
        for (Task task : tasks) {
            add(task);
        }
    }

    @Override
    public void remove(int idNumber) {
        if (inMemoryHistory.containsKey(idNumber)) {
            removeNode(inMemoryHistory.get(idNumber));
            inMemoryHistory.remove(idNumber);
        }
    }

    @Override
    public void remove(Integer[] idNumbers) {
        for (int idNumber : idNumbers) {
            remove(idNumber);
        }
    }

    @Override
    public List<Task> getInMemoryHistory() {
        List<Task> history = new ArrayList<>();

        if (head == null) return history;
        Node<Task> node = head;

        for (int i = 0; i < size; i++) {
            history.add(node.data);
            node = node.next;
        }

        return history;
    }

    private void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;

        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;

        size++;
    }

    private void removeNode (Node<Task> node) {
        final Node<Task> nextNode = node.next;
        final Node<Task> prevNode = node.prev;


        if (prevNode == null) {
            head = nextNode;
        } else {
            prevNode.next = node.next;
            node.prev = null;
        }

        if (nextNode == null) {
            tail = prevNode;
        } else {
            nextNode.prev = prevNode;
            node.prev = null;
        }

        node.data = null;
        size--;
    }
}
