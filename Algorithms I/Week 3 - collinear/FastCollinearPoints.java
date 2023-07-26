import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] segments;
    private int N;

    public FastCollinearPoints(Point[] points) {
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

        // Create a copy of the array to sort
        Point[] copy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            copy[i] = points[i];
        }

        N = 0;
        ArrayList<LineSegment> lscopy = new ArrayList<>();

        for (int i = 0; i < copy.length; i++) {
            // keeps relative order (and insures iteration through all points)
            Arrays.sort(copy);
            Point p = copy[i];

            // same idea as line 19
            Arrays.sort(copy, p.slopeOrder());

            // represents segments - 1 that have equal slopes when count != 0
            // for loop checks 2 segments to start
            int count = 0;
            Point start = null; // start of the observed equal slope segment (excluding p)

            for (int j = 1; j < copy.length - 1; j++) {
                // if p -> q and p -> r have the same slope
                if (p.slopeTo(copy[j]) == p.slopeTo(copy[j + 1])) {
                    // reset the start of an observed equal slope segment
                    if (count == 0) {
                        start = copy[j];
                    }
                    count++;
                }
                else {
                    if (start != null) {
                        // checks that p is less than the start of the segment
                        // since you don't include p itself (since it's always at the start)
                        // for the maximal segment p should be less than whatever the "start"
                        // of the observed segment is
                        if (p.compareTo(start) < 0 && count >= 2) {
                            lscopy.add(new LineSegment(p, copy[j]));
                            N++;
                        }
                    }
                    count = 0;
                }

                // covers case where j + 1 is part of the segment
                if (j == copy.length - 2) {
                    if (start != null) {
                        if (p.compareTo(start) < 0 && count >= 2) {
                            lscopy.add(new LineSegment(p, copy[j + 1]));
                            N++;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
