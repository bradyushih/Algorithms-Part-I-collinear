/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;

public class BruteCollinearPoints {
    private final Point[] points;
    private LineSegment[] segments;
    private int numberOfSegments;

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if (points == null) throw new IllegalArgumentException("null points");
        // if (points.length > 4)
        //     throw new UnsupportedOperationException("within 4 points would work");
        checkForNullPoint(points);
        Point[] testPoint = Arrays.copyOf(points, points.length);
        Arrays.sort(testPoint);
        checkForDuplicatedPoint(testPoint);
        int n = testPoint.length;
        segments = new LineSegment[n * n];
        numberOfSegments = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    for (int m = k + 1; m < n; m++) {
                        double slopePQ = testPoint[i].slopeTo(testPoint[j]);
                        double slopePR = testPoint[i].slopeTo(testPoint[k]);
                        double slopePS = testPoint[i].slopeTo(testPoint[m]);
                        if (slopePQ == slopePR && slopePQ == slopePS) {
                            Point[] collinearPoints = new Point[] {
                                    testPoint[i], testPoint[j], testPoint[k], testPoint[m]
                            };
                            Arrays.sort(collinearPoints);
                            segments[numberOfSegments++] = new LineSegment(collinearPoints[0],
                                                                           collinearPoints[3]);
                        }
                    }
                }
            }
        }
        this.points = Arrays.copyOf(testPoint, testPoint.length);
        // removeDuplicateSegments();
    }

    public int numberOfSegments()        // the number of line segments
    {
        return numberOfSegments;
    }

    public LineSegment[] segments()                // the line segments
    {
        LineSegment[] validSegments = new LineSegment[numberOfSegments];
        System.arraycopy(segments, 0, validSegments, 0, numberOfSegments);
        return validSegments;
    }

    private void checkForNullPoint(Point[] checkPoints) {
        for (Point point : checkPoints) {
            if (point == null) throw new IllegalArgumentException("null point");
        }
    }

    private void checkForDuplicatedPoint(Point[] checkPoints) {
        for (int j = 1; j < checkPoints.length; j++) {
            if (checkPoints[j].compareTo(checkPoints[j - 1]) == 0)
                throw new IllegalArgumentException("duplicate point");
        }
    }

    public static void main(String[] args) {
        // Point o = new Point(3, 0);
        Point a = new Point(19266, 1041);
        Point b = new Point(3205, 1041);
        Point c = new Point(10203, 1041);
        Point d = new Point(11088, 1041);
        // Point e = new Point(1, 3);
        // Point f = new Point(2, 5);
        // Point g = new Point(3, 7);
        // Point h = new Point(4, 9);
        Point[] points = new Point[] { a, b, c, d };
        // Arrays.sort(points, a.slopeOrder());
        // for (Point point : points) {
        //     System.out.println(point);
        // }
        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        // for (Point point : bcp.points) {
        //     System.out.println(point);
        // }
        System.out.println(bcp.segments().length);
        for (LineSegment segment : bcp.segments()) {
            if (segment == null) continue;
            System.out.println(segment.toString());
        }
    }
}
