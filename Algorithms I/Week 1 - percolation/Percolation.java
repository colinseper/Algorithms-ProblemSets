import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// Note: This is the ideal percolation solution, without backwash
// keep in mind, the wqu could be improved to a wqu w/ path compression
public class Percolation {
    // holds node states, closed (000), open (100), top connected + open (110)
    // bottom connected and open (101), and open connected to both - perc (111)
    private byte[] state;
    private int length; // keeps track of n
    private int count = 0;
    private boolean percolates = false;
    private WeightedQuickUnionUF uf;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n is out of bounds");

        // the virtual node ideas makes it impossible to deal with backwash,
        // so we will not include them here
        this.uf = new WeightedQuickUnionUF(n * n);
        this.state = new byte[n * n]; // all states = 0
        this.length = n;
    }

    public void open(int row, int col) {
        throw_excep(row, col);
        if (isOpen(row, col)) return; // if already open
        byte status; // temp variable to compare states of other sites
        int n = rowcol_to_n(row, col);

        // need to set this first before all comparisons, so status can never
        // be overridden after an OR (|) statements
        if (row == 1) {
            if (length == 1) status = 7;
            else status = 6;
        }
        else if (row == length) status = 5; // 101
        else status = 4; // = 100 (not 100), sets site to open

        // Note: | is a bitwise or which basically means if either bit is 1
        // then that bit becomes one, ex: 010 | 110 = 110
        if (row != 1) {
            if (isOpen(row - 1, col)) {
                status = (byte) (status | state[uf.find(rowcol_to_n(row - 1, col))]);
                uf.union(n, rowcol_to_n(row - 1, col));
            }
        }

        if (row != length) {
            if (isOpen(row + 1, col)) {
                status = (byte) (status | state[uf.find(rowcol_to_n(row + 1, col))]);
                uf.union(n, rowcol_to_n(row + 1, col));
            }
        }

        if (col != 1) {
            if (isOpen(row, col - 1)) {
                status = (byte) (status | state[uf.find(rowcol_to_n(row, col - 1))]);
                uf.union(n, rowcol_to_n(row, col - 1));
            }
        }

        if (col != length) {
            if (isOpen(row, col + 1)) {
                status = (byte) (status | state[uf.find(rowcol_to_n(row, col + 1))]);
                uf.union(n, rowcol_to_n(row, col + 1));
            }
        }

        if (status == 7) percolates = true; // update percolates

        // updates root of component to identify whether each node is full
        // status will at least be 4, so works for id-ing whether its open
        state[uf.find(n)] = state[n] = status;
        count++;
    }

    public boolean isOpen(int row, int col) {
        throw_excep(row, col);
        return (state[rowcol_to_n(row, col)] != 0);
    }

    public boolean isFull(int row, int col) {
        throw_excep(row, col);
        return (state[uf.find(rowcol_to_n(row, col))] >= 6);
    }

    public int numberOfOpenSites() {
        return count;
    }

    public boolean percolates() {
        return percolates;
    }

    private int rowcol_to_n(int row, int col) {
        return (row - 1) * length + col - 1; // - 1's bc uf indexed at 0
    }

    private void throw_excep(int row, int col) {
        if (row <= 0 || row > length || col <= 0 || col > length) {
            throw new IllegalArgumentException("row or col is out of bounds");
        }
    }
}
