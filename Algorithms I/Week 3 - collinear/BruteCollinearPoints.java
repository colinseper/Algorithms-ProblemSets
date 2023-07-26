import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private int N;
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
        // Corner Cases
        if (points == null) throw new IllegalArgumentException("null argument");

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("null element");
        }

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("duplicate element");
            }
        }

        // NOTE: There will be no cases where there are 5 or more collinear points
        N = 0;
        ArrayList<LineSegment> lscopy = new ArrayList<>();

        Point[] copy = new Point[points.length];
        for (int w = 0; w < points.length; w++) {
            copy[w] = points[w];
        }

        // Ensures smallest is before biggest -> maximal segment always
        Arrays.sort(copy);

        // Iterate through every possible combination and check each
        for (int i = 0; i < copy.length; i++) {
            for (int j = i + 1; j < copy.length; j++) {
                for (int k = j + 1; k < copy.length; k++) {
                    for (int l = k + 1; l < copy.length; l++) {
                        double s1 = copy[i].slopeTo(copy[j]);
                        double s2 = copy[i].slopeTo(copy[k]);

                        if (s1 == s2) {
                            double s3 = copy[i].slopeTo(copy[l]);

                            if (s2 == s3) {
                                lscopy.add(new LineSegment(copy[i], copy[l]));
                                N++;
                            }
                        }
                    }
                }
            }
        }

        // ensures segments is immutable -> unchanged object
        segments = new LineSegment[N];
        for (int i = 0; i < lscopy.size(); i++) segments[i] = lscopy.get(i);
    }

    public int numberOfSegments() {
        return N;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(segments, N);
    }

    public static void main(String[] args) {
        // Use input5.txt (input6 without 5 points) or input 8.txt
        // since they have only a max of 4 collinear points
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
