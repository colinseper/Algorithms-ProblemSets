import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // made it a double so any math including it returns a double
    private double[] nums_to_percolate;
    private int trials;

    public PercolationStats(int n, int trials) {
        throw_except(n, trials);
        this.nums_to_percolate = new double[trials];
        this.trials = trials;

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                int row = StdRandom.uniformInt(n) + 1;
                int col = StdRandom.uniformInt(n) + 1;

                while (percolation.isOpen(row, col)) {
                    row = StdRandom.uniformInt(n) + 1;
                    col = StdRandom.uniformInt(n) + 1;
                }
                percolation.open(row, col);
            }
            nums_to_percolate[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    public double mean() {
        return StdStats.mean(nums_to_percolate);
    }

    public double stddev() {
        if (trials == 1) return Double.NaN;
        return StdStats.stddev(nums_to_percolate);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = 0, trials = 0;
        if (args.length > 0 && args.length < 3) {
            n = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);
        }

        if (n != 0 && trials != 0) {
            PercolationStats ps = new PercolationStats(n, trials);
            StdOut.printf("%-25s= %.16f\n", "mean", ps.mean());
            StdOut.printf("%-25s= %.16f\n", "stddev", ps.stddev());
            StdOut.printf("%-25s= [%.16f, %.16f]\n", "95% confidence interval",
                          ps.confidenceLo(), ps.confidenceHi());
        }
    }

    private void throw_except(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Invalid n or trials");
        }
    }
}
