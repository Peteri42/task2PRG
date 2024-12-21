package model;

import java.util.ArrayList;

public class Polygon {

	//TODO
    private final ArrayList<Point> points;
    private final ArrayList<Line> lines;

    public Polygon(ArrayList<Line> lines) {
        this.lines = lines;
        points = new ArrayList<>();
        for (Line line : lines) {
            points.add(line.getStartPoint());
            points.add(line.getEndPoint());
        }
    }
    public ArrayList<Point> getPoints() {
        return points;
    }
    public ArrayList<Line> getLines() {
        return lines;
    }
}
