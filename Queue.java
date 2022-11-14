public class Queue<E> {
    ListNode<E> head;
    ListNode<E> tail;
    public int size;

    public Queue() {
    }
    public int getSize() {
        return this.size;
    }

    public Queue(E value) {
    }

    private class ListNode<E> {
        public E value;
        public ListNode<E> next;
        public ListNode<E> previous;

        public ListNode() {
        }
        public ListNode(E value) {
            this.value=value;
        }
    }

    public void enqueue(E value) {
        ListNode<E> temp = new ListNode<>(value);
        if (head == null) {
            head = temp;
            tail = temp;
            return;
        }
        tail.next = temp;
        tail = temp;
    }

    public E dequeue() {
        if (head==null) {
            return null;
        }
        E node = head.value;
        head = head.next;
        this.size--;
        return node;
    }


    public boolean isEmpty() {
        if (head==null) {
            return true;
        }
        return false;
    }
}