/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

import java.util.Arrays;

public class FastCollinearPoints {
    private final Point[] points;
    private LineSegment[] lineSegments;
    private int numOfSegment;

    public FastCollinearPoints(
            Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null) throw new IllegalArgumentException("null points");
        checkForNullPoint(points);
        Point[] testPoint = Arrays.copyOf(points, points.length);
        Arrays.sort(testPoint);
        checkForDuplicatedPoint(testPoint);
        // 已確定使用斜率
        int n = testPoint.length;
        LineSegment[] tmpLineSegments = new LineSegment[n * n];
        // 斜率儲存陣列
        // 線段第一點位置
        numOfSegment = 0;
        for (int i = 0; i < n; i++) {
            Point refPoint = testPoint[i];
            // System.out.println("refPoint: " + refPoint);
            // 依斜率排序
            Point[] otherPoints = new Point[n - 1];
            int idx = 0;
            for (int j = 0; j < n; j++) {
                if (j != i)
                    otherPoints[idx++] = testPoint[j];
            }
            Arrays.sort(otherPoints, refPoint.slopeOrder());
            int countSlope = 1;
            double tmpSlope = 0;
            int segmentPointIdx = 0;
            Point[] tmpSegmentPointGroup = new Point[testPoint.length];
            for (int j = 0; j < otherPoints.length; j++) {
                Point checkPoint = otherPoints[j];
                double currSlope = refPoint.slopeTo(checkPoint);
                boolean isLine = countSlope >= 3;
                boolean isSameSlope = tmpSlope == currSlope;
                boolean isLastPoint = j == otherPoints.length - 1;
                boolean isLineWithLastPoint = countSlope >= 2 && isLastPoint;
                // if (currSlope == 0) {
                //     System.out.println("checkPoint: " + checkPoint);
                // }
                if ((isLine && !isSameSlope) || (isLine && isLastPoint)) {
                    if (isSameSlope)
                        tmpSegmentPointGroup[segmentPointIdx++] = checkPoint;
                    tmpSegmentPointGroup[segmentPointIdx++] = refPoint;
                    tmpSegmentPointGroup = Arrays.copyOf(tmpSegmentPointGroup, segmentPointIdx);
                    groupLineSegment(tmpLineSegments, tmpSegmentPointGroup);
                    // new
                    tmpSegmentPointGroup = new Point[n];
                    segmentPointIdx = 0;
                    tmpSegmentPointGroup[segmentPointIdx++] = checkPoint;
                    tmpSlope = currSlope;
                    countSlope = 1;
                }
                else if (!isSameSlope) {
                    segmentPointIdx = 0;
                    tmpSegmentPointGroup[segmentPointIdx++] = checkPoint;
                    tmpSlope = currSlope;
                    countSlope = 1;
                }
                else {
                    tmpSegmentPointGroup[segmentPointIdx++] = checkPoint;
                    countSlope++;
                    if (isLineWithLastPoint) {
                        tmpSegmentPointGroup[segmentPointIdx++] = refPoint;
                        tmpSegmentPointGroup = Arrays.copyOf(tmpSegmentPointGroup, segmentPointIdx);
                        groupLineSegment(tmpLineSegments, tmpSegmentPointGroup);
                    }
                }
            }
            //////////////////////////////
            //     Point p = testPoint[i];
            //     // System.out.println("p -> " + p);
            //     Point[] otherPoints = new Point[n - 1];
            //     int idx = 0;
            //     int slopesI = 0;
            //     // 創建一個不包含p的點數組otherPoints
            //     for (int j = 0; j < n; j++) {
            //         if (j != i) {
            //             otherPoints[idx++] = testPoint[j];
            //         }
            //     }
            //
            //     // 根據與p的斜率對otherPoints進行排序
            //     Arrays.sort(otherPoints, p.slopeOrder());
            //     // for (Point otherPoint : otherPoints) {
            //     //     System.out.println(otherPoint);
            //     // }
            //     int count = 1;
            //     Point lastPoint = null;
            //     for (int j = 1; j < n - 1; j++) {
            //         if (p.slopeTo(otherPoints[j]) == p.slopeTo(otherPoints[j - 1])) {
            //             count++;
            //             lastPoint = otherPoints[j];
            //             if (count >= 4 && j == n - 2) {
            //                 lineSegments[i] = new LineSegment(p, lastPoint);
            //                 if (!checkSlopeExist(slopes, p.slopeTo(lastPoint))) {
            //                     slopes[slopesI] = p.slopeTo(lastPoint);
            //                 }
            //             }
            //         }
            //         else {
            //             // 檢查是否找到了共線點的線段
            //             if (count >= 4) {
            //                 lineSegments[i] = new LineSegment(p, lastPoint);
            //                 if (!checkSlopeExist(slopes, p.slopeTo(lastPoint))) {
            //                     slopes[slopesI] = p.slopeTo(lastPoint);
            //                 }
            //             }
            //             count = 1;
            //         }
            //         slopesI++;
            //     }
            //////////////////////////////
        }
        lineSegments = Arrays.copyOf(tmpLineSegments, numOfSegment);
        this.points = Arrays.copyOf(testPoint, testPoint.length);
    }

    private void groupLineSegment(LineSegment[] tmpLineSegments, Point[] tmpSegmentPointGroup) {
        Arrays.sort(tmpSegmentPointGroup);
        LineSegment ls = new LineSegment(tmpSegmentPointGroup[0],
                                         tmpSegmentPointGroup[
                                                 tmpSegmentPointGroup.length
                                                         - 1]);
        for (LineSegment tmpLineSegment : tmpLineSegments) {
            if (tmpLineSegment == null) {
                tmpLineSegments[numOfSegment++] = new LineSegment(
                        tmpSegmentPointGroup[0],
                        tmpSegmentPointGroup[
                                tmpSegmentPointGroup.length
                                        - 1]);
                break;
            }
            else if (tmpLineSegment.toString().equals(ls.toString())) {
                break;
            }
        }
    }

    public int numberOfSegments()        // the number of line segments
    {
        return numOfSegment;
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

    // private boolean checkSlopeExist(Double[] slopes, double checkSlope) {
    //     // for (Double slope : slopes) {
    //     //     System.out.println(slope);
    //     // }
    //     for (int i = 0; i < slopes.length; i++) {
    //         if (slopes[i] == null) return false;
    //         if (checkSlope == slopes[i]) return true;
    //     }
    //     return false;
    // }

    public static void main(String[] args) {
        int num = StdIn.readInt();
        Point[] points = new Point[num];
        int i = 0;
        while (!StdIn.isEmpty()) {
            points[i++] = new Point(StdIn.readInt(), StdIn.readInt());
        }

        // Point[] points = new Point[] {
        //         new Point(9000, 9000),
        //         new Point(8000, 8000),
        //         new Point(7000, 7000),
        //         new Point(6000, 6000),
        //         new Point(5000, 5000),
        //         new Point(4000, 4000),
        //         new Point(3000, 3000),
        //         new Point(2000, 2000),
        //         new Point(1000, 1000),
        //         };


        // Arrays.sort(points, a.slopeOrder());
        // for (Point point : points) {
        //     System.out.println(a.slopeTo(point));
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

