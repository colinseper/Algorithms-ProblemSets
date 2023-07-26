import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {

    private int[] puzzle;
    private int N;

    public Board(int[][] tiles) {
        N = tiles.length; // rows = columns bc nxn
        puzzle = new int[N * N];

        int count = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                puzzle[count++] = tiles[i][j];
            }
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");

        for (int i = 0; i < N * N; i++) {
            s.append(String.format("%d ", puzzle[i]));

            if ((i + 1) % N == 0) s.append("\n");
        }

        return s.toString();
    }

    public int dimension() {
        return N;
    }

    public int hamming() {
        int ham_dist = 0;

        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] != 0 && puzzle[i] != i + 1) ham_dist++;
        }

        return ham_dist;
    }

    public int manhattan() {
        int man_dist = 0;

        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] != 0 && puzzle[i] != i + 1) {
                man_dist += Math.abs(((puzzle[i] - 1) / N) - (i / N)); // vertical adjustment
                man_dist += Math.abs(((puzzle[i] - 1) % N) - (i % N)); // horizontal adjustment
            }
        }

        return man_dist;
    }

    public boolean isGoal() {
        int[] goalboard = new int[N * N]; // all instantiated at 0
        for (int i = 0; i < N * N - 1; i++) goalboard[i] = i + 1;

        return Arrays.equals(puzzle, goalboard);
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        return this.toString().equals(y.toString());
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> boards = new Stack<>();

        // used to create new boards
        Board tmpb;
        int[][] tmpt = new int[N][N];
        int[] row_col = copy_find_zero(tmpt);
        int zrow = row_col[0]; // row of empty tile
        int zcol = row_col[1]; // column of empty tile

        if (zrow != 0) {
            tmpt[zrow][zcol] = tmpt[zrow - 1][zcol];
            tmpt[zrow - 1][zcol] = 0;

            tmpb = new Board(tmpt);
            boards.push(tmpb);

            tmpt[zrow - 1][zcol] = tmpt[zrow][zcol];
            tmpt[zrow][zcol] = 0;
        }

        // N - 1 because 0-indexed, N = greatest index + 1
        if (zrow != (N - 1)) {
            tmpt[zrow][zcol] = tmpt[zrow + 1][zcol];
            tmpt[zrow + 1][zcol] = 0;

            tmpb = new Board(tmpt);
            boards.push(tmpb);

            tmpt[zrow + 1][zcol] = tmpt[zrow][zcol];
            tmpt[zrow][zcol] = 0;
        }

        if (zcol != 0) {
            tmpt[zrow][zcol] = tmpt[zrow][zcol - 1];
            tmpt[zrow][zcol - 1] = 0;

            tmpb = new Board(tmpt);
            boards.push(tmpb);

            tmpt[zrow][zcol - 1] = tmpt[zrow][zcol];
            tmpt[zrow][zcol] = 0;
        }

        // same as for zrow
        if (zcol != (N - 1)) {
            tmpt[zrow][zcol] = tmpt[zrow][zcol + 1];
            tmpt[zrow][zcol + 1] = 0;

            tmpb = new Board(tmpt);
            boards.push(tmpb);

            tmpt[zrow][zcol + 1] = tmpt[zrow][zcol];
            tmpt[zrow][zcol] = 0;
        }

        return boards;
    }

    // a board that is obtained by exchanging any pair of non-empty horizontal tiles
    public Board twin() {
        int[][] tmp_arr = new int[N][N];
        int[] row_col = copy_find_zero(tmp_arr); // row_col are 0 indexed
        int row = row_col[0]; // same idea as neighbors
        int col = row_col[1];

        // note row no longer represents where the 0 is, same for col later
        if (row != (N - 1)) row += 1;
        else row -= 1;

        if (col != (N - 1)) {
            int tmp_tile = tmp_arr[row][col];
            tmp_arr[row][col] = tmp_arr[row][col + 1];
            tmp_arr[row][col + 1] = tmp_tile;
        }
        else {
            int tmp_tile = tmp_arr[row][col];
            tmp_arr[row][col] = tmp_arr[row][col - 1];
            tmp_arr[row][col - 1] = tmp_tile;
        }

        return new Board(tmp_arr);
    }

    // copy puzzle into 2d array and find index of 0
    private int[] copy_find_zero(int[][] tmp) {
        int[] row_col = new int[2];
        int count = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (puzzle[count] == 0) {
                    row_col[0] = i;
                    row_col[1] = j;
                }

                tmp[i][j] = puzzle[count++];
            }
        }

        return row_col;
    }

    // unit testing
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        System.out.println(initial.toString());

        int[][] test = new int[n][n];
        int count = 0;

        // Goal Board to check .equals
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (count == n * n - 1) test[i][j] = 0;
                else test[i][j] = ++count;
            }
        }

        Board tester = new Board(test);

        System.out.println("Dimension: " + initial.dimension());
        System.out.println("Hamming Dist: " + initial.hamming());
        System.out.println("Manhattan Dist: " + initial.manhattan());
        System.out.println("Goal Board? " + initial.isGoal());
        System.out.println("Equal? " + initial.equals(tester));
        System.out.println("Neighbors: \n");

        for (Board el : initial.neighbors()) {
            System.out.println(el.toString());
        }

        Board twin = initial.twin();
        System.out.println("Twin Board: ");
        System.out.println(twin.toString());
    }
}
