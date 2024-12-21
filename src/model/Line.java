package model;
public class Line {

    private final int x1, x2, y1, y2;
    private final Point startPoint;
    private final Point endPoint;
    private final int color;

    public Line(int x1, int y1, int x2, int y2, int color) {
        this.color = color;
        startPoint = new Point(x1, y1);
        endPoint = new Point(x2, y2);
        this.x1 = startPoint.x;
        this.y1 = startPoint.y;
        this.x2 = endPoint.x;
        this.y2 = endPoint.y;
    }

    public Line(Point p1, Point p2, int color) {
        this.x1 = p1.x;
        this.y1 = p1.y;
        this.x2 = p2.x;
        this.y2 = p2.y;
        startPoint = new Point(x1, y1);
        endPoint = new Point(x2, y2);
        this.color = color;
    }
    //TODO
    public int getX1() {
        return x1;
    }
    public int getY1() {
        return y1;
    }
    public int getX2() {
        return x2;
    }
    public int getY2() {
        return y2;
    }
    public int getColor() {
        return color;
    }
    public Point getStartPoint() {
        return startPoint;
    }
    public Point getEndPoint() {
        return endPoint;
    }
}
