import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int N;

    private class Node {
        Item item;
        Node next;
        Node prev; // adds extra node to track what's behind it as well
    }

    public Deque() {
        first = null;
        last = null;
        N = 0;
    }

    public boolean isEmpty() {
        return (first == null); // N == 0 or last = null would work as well
    }

    public int size() {
        return N;
    }

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("No item entered.");
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        if (last == null) last = first; // if 1 node it should be first and last
        else {
            first.next = oldfirst;
            oldfirst.prev = first;
        }
        N++;
    }

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("No item entered.");
        Node oldlast = last;
        last = new Node();
        last.item = item;
        if (first == null) first = last; // same idea as line 36
        else {
            last.prev = oldlast;
            oldlast.next = last;
        }
        N++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        Item item = first.item;
        first = first.next;
        if (first != null) first.prev = null; // ensures garbage collection
        else last = null; // if there are no more nodes, ensure last is null
        N--;
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        Item item = last.item;
        last = last.prev;
        if (last != null) last.next = null; // same idea as 61
        else first = null; // same idea as 62
        N--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() { // iterate from 1 to n
            if (!hasNext()) throw new NoSuchElementException("No more items");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove is unsupported");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = 10;
        Deque<Integer> deque = new Deque<Integer>();

        for (int i = 1; i < n; i++) {
            deque.addFirst(i);
        }

        for (int i : deque) {
            StdOut.print(i + " ");
        }
        StdOut.println();

        for (int i = 1; i < n; i++) {
            StdOut.print(deque.removeFirst() + " ");
        }
        StdOut.println();

        for (int i = 1; i < n; i++) {
            deque.addLast(i);
        }

        for (int i : deque) {
            StdOut.print(i + " ");
        }
        StdOut.println();

        for (int i = 1; i < n; i++) {
            StdOut.print(deque.removeLast() + " ");
        }

        StdOut.println();
    }
}
