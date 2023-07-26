import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

// Note all points are between 0 and 1
public class KdTree {
    private Node root;
    private int count;

    public KdTree() {
        this.count = 0;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument is null");
        if (contains(p)) return; // rejects duplicate points
        if (count == 0) root = new Node(p, new RectHV(0, 0, 1, 1));
        else {
            insert(root, p, true);
        }
        count++;
    }

    private void insert(Node node, Point2D p, boolean type) {
        if (type) {
            if (p.x() < node.getPoint().x()) {
                if (node.getL_ref() == null) {
                    RectHV rect = new RectHV(node.getRectangle().xmin(),
                                             node.getRectangle().ymin(),
                                             node.getPoint().x(),
                                             node.getRectangle().ymax());
                    node.setL_ref(new Node(p, rect));
                }
                else {
                    insert(node.getL_ref(), p, false);
                }
            }
            else {
                if (node.getR_ref() == null) {
                    RectHV rect = new RectHV(node.getPoint().x(),
                                             node.getRectangle().ymin(),
                                             node.getRectangle().xmax(),
                                             node.getRectangle().ymax());
                    node.setR_ref(new Node(p, rect));
                }
                else {
                    insert(node.getR_ref(), p, false);
                }
            }
        }
        else {
            if (p.y() < node.getPoint().y()) {
                if (node.getL_ref() == null) {
                    RectHV rect = new RectHV(node.getRectangle().xmin(),
                                             node.getRectangle().ymin(),
                                             node.getRectangle().xmax(),
                                             node.getPoint().y());
                    node.setL_ref(new Node(p, rect));
                }
                else {
                    insert(node.getL_ref(), p, true);
                }
            }
            else {
                if (node.getR_ref() == null) {
                    RectHV rect = new RectHV(node.getRectangle().xmin(),
                                             node.getPoint().y(),
                                             node.getRectangle().xmax(),
                                             node.getRectangle().ymax());
                    node.setR_ref(new Node(p, rect));
                }
                else {
                    insert(node.getR_ref(), p, true);
                }
            }
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument is null");

        return contains(root, p, true);
    }

    private boolean contains(Node search, Point2D p, boolean type) {
        if (search == null) {
            return false;
        }
        if (p.compareTo(search.getPoint()) == 0) {
            return true;
        }

        if (type) {
            if (p.x() < search.getPoint().x()) {
                return contains(search.getL_ref(), p, false);
            }
            else {
                return contains(search.getR_ref(), p, false);
            }
        }
        else {
            if (p.y() < search.getPoint().y()) {
                return contains(search.getL_ref(), p, true);
            }
            else {
                return contains(search.getR_ref(), p, true);
            }
        }
    }

    public void draw() {
        draw(root, true);
    }

    private void draw(Node node, boolean type) {
        if (node.getL_ref() != null) {
            draw(node.getL_ref(), !type);
        }

        StdDraw.setPenRadius();

        if (type) {
            StdDraw.setPenColor(StdDraw.RED);
            double x = node.getPoint().x();

            StdDraw.line(x, node.getRectangle().ymin(),
                         x, node.getRectangle().ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            double y = node.getPoint().y();

            StdDraw.line(node.getRectangle().xmin(), y,
                         node.getRectangle().xmax(), y);
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.getPoint().draw();

        if (node.getR_ref() != null) {
            draw(node.getR_ref(), !type);
        }
    }

    /*
        To find all points contained in a given query rectangle, start at the
        root and recursively search for points in both subtrees using the
        following pruning rule: if the query rectangle does not intersect the
        rectangle corresponding to a node, there is no need to explore that node
         (or its subtrees). A subtree is searched only if it might contain a
         point contained in the query rectangle.
     */

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Argument is null");
        Stack<Point2D> points = new Stack<>();
        range(points, root, rect);
        return points;
    }

    private void range(Stack<Point2D> points, Node node, RectHV rect) {
        if (node == null) {
            return;
        }

        if (node.getL_ref() != null && rect.intersects(node.getL_ref().getRectangle())) {
            range(points, node.getL_ref(), rect);
        }

        if (rect.contains(node.getPoint())) {
            points.push(node.getPoint());
        }

        if (node.getR_ref() != null && rect.intersects(node.getR_ref().getRectangle())) {
            range(points, node.getR_ref(), rect);
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument is null");
        if (root == null) return null;
        Node nearest = nearest(root, p, true, root);
        return nearest.getPoint();
    }

    /*
        To find a closest point to a given query point, start at the root and
        recursively search in both subtrees using the following pruning rule:
        if the closest point discovered so far is closer than the distance
        between the query point and the rectangle corresponding to a node,
        there is no need to explore that node (or its subtrees). That is, search
         a node only if it might contain a point that is closer than the
         best one found so far. The effectiveness of the pruning rule depends on
          quickly finding a nearby point. To do this, organize the recursive
          method so that when there are two possible subtrees to go down, you
          always choose the subtree that is on the same side of the splitting
          line as the query point as the first subtree to explore—the closest
          point found while exploring the first subtree may enable pruning of
          the second subtree.
    */

    private Node nearest(Node node, Point2D p, boolean type, Node nearest) {
        if (p.distanceTo(node.getPoint()) < p.distanceTo(nearest.getPoint())) {
            nearest = node;
        }

        if (type) {
            if (p.x() < node.getPoint().x()) {
                if (node.getL_ref() != null && p.distanceTo(nearest.getPoint())
                        > node.getRectangle().distanceTo(p)) {
                    nearest = nearest(node.getL_ref(), p, false, nearest);
                }
                if (node.getR_ref() != null && p.distanceTo(nearest.getPoint())
                        > node.getRectangle().distanceTo(p)) {
                    nearest = nearest(node.getR_ref(), p, false, nearest);
                }
            }
            else {
                if (node.getR_ref() != null && p.distanceTo(nearest.getPoint())
                        > node.getRectangle().distanceTo(p)) {
                    nearest = nearest(node.getR_ref(), p, false, nearest);
                }
                if (node.getL_ref() != null && p.distanceTo(nearest.getPoint())
                        > node.getRectangle().distanceTo(p)) {
                    nearest = nearest(node.getL_ref(), p, false, nearest);
                }
            }
        }
        else {
            if (p.y() < node.getPoint().y()) {
                if (node.getL_ref() != null && p.distanceTo(nearest.getPoint())
                        > node.getRectangle().distanceTo(p)) {
                    nearest = nearest(node.getL_ref(), p, true, nearest);
                }
                if (node.getR_ref() != null && p.distanceTo(nearest.getPoint())
                        > node.getRectangle().distanceTo(p)) {
                    nearest = nearest(node.getR_ref(), p, true, nearest);
                }
            }
            else {
                if (node.getR_ref() != null && p.distanceTo(nearest.getPoint())
                        > node.getRectangle().distanceTo(p)) {
                    nearest = nearest(node.getR_ref(), p, true, nearest);
                }
                if (node.getL_ref() != null && p.distanceTo(nearest.getPoint())
                        > node.getRectangle().distanceTo(p)) {
                    nearest = nearest(node.getL_ref(), p, true, nearest);
                }
            }
        }
        return nearest;
    }

    // Class for Kd tree nodes
    private static class Node {
        private final Point2D point;
        private final RectHV rectangle;
        private Node l_ref; // references left/bottom
        private Node r_ref; // references right/top

        public Node(Point2D p, RectHV rect) {
            this.point = p;
            this.rectangle = rect;
        }

        public Point2D getPoint() {
            return point;
        }

        public RectHV getRectangle() {
            return rectangle;
        }

        public Node getL_ref() {
            return l_ref;
        }

        public Node getR_ref() {
            return r_ref;
        }

        public void setL_ref(Node node) {
            l_ref = node;
        }

        public void setR_ref(Node node) {
            r_ref = node;
        }

    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        KdTree kdTree = new KdTree();
        int i = 1;

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            kdTree.insert(new Point2D(x, y));
            System.out.print(i++ + " ");
            System.out.println(new Point2D(x, y).toString());
            kdTree.draw();
        }

        System.out.println("Size " + kdTree.size());
    }
}
