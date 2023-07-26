import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    // default size, used same as princeton queue implementation
    private static final int INIT_CAPACITY = 8;
    private Item[] rq;
    private int N;

    public RandomizedQueue() {
        rq = (Item[]) new Object[INIT_CAPACITY];
        N = 0;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("No item entered.");
        if (N == rq.length) resize(2 * rq.length);
        rq[N++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("RandomizedQueue is empty");
        int index = StdRandom.uniformInt(N); // 0 to N (exclusive)
        Item item = rq[index];
        if (index != N - 1) {
            rq[index] = rq[N - 1];
            rq[N - 1] = null;
        }
        else rq[index] = null;
        N--;
        if (N > 0 && N == rq.length / 4) resize(rq.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("RandomizedQueue is empty");
        return rq[StdRandom.uniformInt(N)];
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) copy[i] = rq[i];
        rq = copy;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        Item[] copy = (Item[]) new Object[N];
        for (int i = 0; i < N; i++) copy[i] = rq[i];
        StdRandom.shuffle(copy);
        return new RandomIterator(copy);
    }

    private class RandomIterator implements Iterator<Item> {
        private int i;
        private Item[] copy;

        public RandomIterator(Item[] copy) {
            i = N;
            this.copy = copy;
        }

        public boolean hasNext() {
            return i > 0;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more items");
            return copy[--i];
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove is unsupported");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);

        StdOut.println(queue.size());

        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }

        for (int i = 0; i < n; i++) {
            StdOut.println(queue.sample() + " " + queue.dequeue());
        }

        for (int i = 0; i < n; i++) {
            queue.enqueue(i);
        }

        StdOut.println();

        for (int i = 0; i < n; i++) {
            StdOut.println(queue.sample() + " " + queue.dequeue());
        }
    }
}
