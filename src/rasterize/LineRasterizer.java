package rasterize;

import model.Line;
import model.Point;

import java.awt.*;

public abstract class LineRasterizer {
    Raster raster;
    Color color;

    public LineRasterizer(Raster raster){
        this.raster = raster;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setColor(int color) {
        this.color = new Color(color);
    }

    public void rasterize(Line line) {
        //TODO
    }

    public void rasterize(int x1, int y1, int x2, int y2, Color color) {
        //TODO
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
    }

    public void drawLine(Point startPoint, Point currentPoint) {
    }
    public void drawLine(Line line) {

    }
}
