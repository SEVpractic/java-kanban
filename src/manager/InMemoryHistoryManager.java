package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private class CustomLinkedList<T extends Task > {
        private class Node<E> {
            public E data;
            public Node<E> next;
            public Node<E> prev;

            public Node(Node<E> prev, E data, Node<E> next) {
                this.data = data;
                this.next = next;
                this.prev = prev;
            }
        }

        private Node<T> head; //Указатель на первый элемент списка.
        private Node<T> tail; //Указатель на последний элемент списка.
        private int size; //Количество элементов в списке.
        private final HashMap<Integer, Node<T>> nodes; //ключ - ID, значение - Node.

        public CustomLinkedList() {
            this.nodes = new HashMap<>();
            this.size = 0;
        }

        public void add(T t) {
            if (nodes.containsKey(t.getIdNumber())) {
                removeNode(nodes.get(t.getIdNumber()));
                nodes.remove(t.getIdNumber());
            }
            linkLast(t);
            nodes.put(t.getIdNumber(), tail);
        }

        private void linkLast(T t) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, t, null);
            tail = newNode;

            if (oldTail == null) {
                head = newNode;
            }
            else {
                oldTail.next = newNode;
            }

            size++;
        }

        public List<T> getHistory() {
            List<T> history = new ArrayList<>();

            if (head == null) {
                return history;
            }
            Node<T> node = head;

            for (int i = 0; i < size; i++) {
                history.add(node.data);
                node = node.next;
            }

            return history;
        }

        public void remove(int i) {
            if (nodes.containsKey(i)) {
                removeNode(nodes.get(i));
                nodes.remove(i);
            }
        }

        private void removeNode (Node<T> node) {
            final Node<T> nextNode = node.next;
            final Node<T> prevNode = node.prev;

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

    private final CustomLinkedList<Task> history;

    public InMemoryHistoryManager() {
        history = new CustomLinkedList<>();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            history.add(task);
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
        history.remove(idNumber);
    }

    @Override
    public void remove(Integer[] idNumbers) {
        for (int idNumber : idNumbers) {
            remove(idNumber);
        }
    }

    @Override
    public List<Task> getInMemoryHistory() {
        return history.getHistory();
    }
}
