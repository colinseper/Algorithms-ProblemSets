import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> randq = new RandomizedQueue<>();
        if (args.length > 0) {
            int k = Integer.parseInt(args[0]);
            for (int i = 0; !StdIn.isEmpty(); i++) {
                if (i >= k) {
                    int num = StdRandom.uniformInt(i + 1);
                    if (num < k) {
                        randq.dequeue();
                        randq.enqueue(StdIn.readString());
                    }
                    else StdIn.readString();
                }
                else randq.enqueue(StdIn.readString());
            }
            for (int i = 0; i < k; i++) StdOut.println(randq.dequeue());
        }
    }
}
