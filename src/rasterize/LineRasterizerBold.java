package rasterize;

import model.Line;

public class LineRasterizerBold extends LineRasterizerGraphics {
    public LineRasterizerBold(Raster raster) {
        super(raster);
    }
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;
        while (true) {
            drawThickPixel(x1,y1, 5);
            if (x1 == x2 && y1 == y2) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }
    @Override
    public void drawLine(model.Point p1, model.Point p2) {
        drawLine(p1.x, p1.y, p2.x, p2.y);
    }
    @Override
    public void drawLine(Line line)
    {
        drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }
    //na debugging jsem si přidal metodu na změnu thickness ale nepůjde upravovat v programu
    private void drawThickPixel(int x, int y, int thickness) {
        int halfThickness = thickness / 2;
        for (int dx = -halfThickness; dx <= halfThickness; dx++) {
            for (int dy = -halfThickness; dy <= halfThickness; dy++) {
                raster.setPixel(x + dx, y + dy, 0xff0000);
            }
        }
    }
}
