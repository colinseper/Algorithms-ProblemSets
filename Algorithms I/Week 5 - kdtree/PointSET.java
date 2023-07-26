import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

public class PointSET {
    private SET<Point2D> LLRB; // left leaning red-black bst ST implementation

    public PointSET() {
        LLRB = new SET<>();
    }

    public boolean isEmpty() {
        return LLRB.isEmpty();
    }

    public int size() {
        return LLRB.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument is null");
        LLRB.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument is null");
        return LLRB.contains(p);
    }

    public void draw() {
        for (Point2D p : LLRB) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Argument is null");

        Stack<Point2D> points = new Stack<>();

        for (Point2D p : LLRB) {
            if (rect.contains(p)) points.push(p);
        }

        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument is null");
        else if (isEmpty()) return null;

        Point2D nearest = null;
        double dist = Double.POSITIVE_INFINITY;

        for (Point2D p2 : LLRB) {
            if (p.distanceTo(p2) < dist) {
                nearest = p2;
                dist = p.distanceTo(p2);
            }
        }

        return nearest;
    }
}
