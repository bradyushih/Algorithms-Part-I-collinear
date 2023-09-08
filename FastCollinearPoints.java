/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;

public class FastCollinearPoints {
    private final Point[] points;
    private LineSegment[] lineSegments;

    public FastCollinearPoints(
            Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null) throw new IllegalArgumentException("null points");
        checkForNullPoint(points);
        Point[] testPoint = Arrays.copyOf(points, points.length);
        Arrays.sort(testPoint);
        checkForDuplicatedPoint(testPoint);
        Double[] slopes = new Double[points.length];// 已確定使用斜率
        int n = testPoint.length;
        lineSegments = new LineSegment[n * n];
        for (int i = 0; i < n; i++) {
            Point p = testPoint[i];
            // System.out.println("p -> " + p);
            Point[] otherPoints = new Point[n - 1];
            int idx = 0;
            int slopesI = 0;
            // 創建一個不包含p的點數組otherPoints
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    otherPoints[idx++] = testPoint[j];
                }
            }

            // 根據與p的斜率對otherPoints進行排序
            Arrays.sort(otherPoints, p.slopeOrder());
            // for (Point otherPoint : otherPoints) {
            //     System.out.println(otherPoint);
            // }
            int count = 1;
            Point lastPoint = null;
            for (int j = 1; j < n - 1; j++) {
                if (p.slopeTo(otherPoints[j]) == p.slopeTo(otherPoints[j - 1])) {
                    count++;
                    lastPoint = otherPoints[j];
                    if (count >= 4 && j == n - 2) {
                        lineSegments[i] = new LineSegment(p, lastPoint);
                        if (!checkSlopeExist(slopes, p.slopeTo(lastPoint))) {
                            slopes[slopesI++] = p.slopeTo(lastPoint);
                        }
                    }
                }
                else {
                    // 檢查是否找到了共線點的線段
                    if (count >= 4) {
                        lineSegments[i] = new LineSegment(p, lastPoint);
                        if (!checkSlopeExist(slopes, p.slopeTo(lastPoint))) {
                            slopes[slopesI++] = p.slopeTo(lastPoint);
                        }
                    }
                    count = 1;
                }
            }
        }
        this.points = Arrays.copyOf(testPoint, testPoint.length);
    }

    public int numberOfSegments()        // the number of line segments
    {
        return lineSegments.length;
    }

    public LineSegment[] segments()                // the line segments
    {
        return Arrays.copyOf(lineSegments, lineSegments.length);
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

    private boolean checkSlopeExist(Double[] slopes, double checkSlope) {
        for (int i = 0; i < slopes.length; i++) {
            if (slopes[i] == null) return false;
            if (checkSlope == slopes[i]) return true;
        }
        return false;
    }

    public static void main(String[] args) {

        Point a = new Point(1, 2);
        Point b = new Point(2, 3);
        Point c = new Point(3, 4);
        Point e = new Point(4, 5);
        Point f = new Point(5, 6);
        Point[] points = new Point[] { a, b, c, e, f };
        Arrays.sort(points);
        Arrays.sort(points, a.slopeOrder());
        // for (Point point : points) {
        //     System.out.println(point);
        // }
        FastCollinearPoints p = new FastCollinearPoints(points);
        System.out.println(p.numberOfSegments());
        for (LineSegment lineSegment : p.lineSegments) {
            if (lineSegment != null)
                System.out.println(lineSegment.toString());
        }
    }
}
/*
遍歷各點
建立不含該點之陣列
建立空線段陣列
取得該點與參考點斜率並排序
存儲線段第一點
斜率相同的點加入線段陣列
遇不同線率則重設線段第一點並存儲前一線段
 */

