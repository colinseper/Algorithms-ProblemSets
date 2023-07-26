import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode solution;
    private boolean solvable;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Argument is null");
        MinPQ<SearchNode> A_star_alg = new MinPQ<>();
        MinPQ<SearchNode> A_star_twin = new MinPQ<>();

        // initial search node has 0 moves and no prev reference
        SearchNode sn = new SearchNode(initial, 0, null);
        A_star_alg.insert(sn);

        // twin search node
        SearchNode sn_twin = new SearchNode(initial.twin(), 0, null);
        A_star_twin.insert(sn_twin);

        /* A* Search Algorithm -> delete min from pq (initial), insert the
           neighbors of the board as search nodes and continue until solved.
           Note that we test if something is unsolvable by testing whether it
           or it's twin can be solved (one of these must be solvable)
        */

        while (true) {
            SearchNode min = A_star_alg.delMin();
            SearchNode twin_min = A_star_twin.delMin();

            if (min.getBoard().isGoal()) {
                solvable = true;
                solution = min;
                break;
            }
            else if (twin_min.getBoard().isGoal()) {
                solvable = false;
                break;
            }

            for (Board i : min.getBoard().neighbors()) {
                SearchNode neighbor = new SearchNode(i, min.getMoves() + 1, min);
                if (min.getMoves() == 0 || !neighbor.getBoard().equals(min.getPrev().getBoard())) {
                    A_star_alg.insert(neighbor);
                }
            }

            for (Board j : twin_min.getBoard().neighbors()) {
                SearchNode neighbor = new SearchNode(j, twin_min.getMoves() + 1, twin_min);
                if (twin_min.getMoves() == 0 || !neighbor.getBoard()
                                                         .equals(twin_min.getPrev().getBoard())) {
                    A_star_twin.insert(neighbor);
                }
            }
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    // returns -1 if unsolvable
    public int moves() {
        if (!solvable) return -1;
        else return solution.getMoves();
    }

    // returns null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) return null;
        else {
            Queue<Board> path_to_sol = new Queue<>();
            // makes a hard copy of solution
            SearchNode tmp = new SearchNode(solution.getBoard(), solution.getMoves(),
                                            solution.getPrev());

            while (tmp.getPrev() != null) {
                path_to_sol.enqueue(tmp.getBoard());
                tmp = tmp.getPrev();
            }
            path_to_sol.enqueue(tmp.getBoard());

            Stack<Board> sol_iter = new Stack<>();

            for (Board x : path_to_sol) {
                sol_iter.push(path_to_sol.dequeue());
            }
            return sol_iter;
        }
    }

    // test client
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    // private search node class (for min_pq) -> includes board,
    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode prev;
        private final int manhattan;

        public SearchNode(Board brd, int mvs, SearchNode sn) {
            this.board = brd;
            this.moves = mvs;
            this.prev = sn;
            this.manhattan = this.board.manhattan();
        }

        public int compareTo(SearchNode node) {
            // makes the boards be compared by the value of their priority
            return Integer.compare(this.getManhattan() + this.getMoves(),
                                   node.getManhattan() + node.getMoves());
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public SearchNode getPrev() {
            return prev;
        }

        public int getManhattan() {
            return manhattan;
        }
    }
}
