package fill;

import model.Point;
import rasterize.Raster;

import java.util.LinkedList;
import java.util.Queue;

public class SeedFill implements Filler {
    private final Raster raster;
    private int x,y;
    public SeedFill(Raster raster) {
        this.raster = raster;
    }
    public void setStartingPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public void fill() {
        // Ověření, jestli kliknutím nejsme mimo plátno
        if (x < 0 || x >= raster.getWidth() || y < 0 || y >= raster.getHeight()) {
            return;
        }

        // Barva místa, kam jsme klikli (kterou chceme nahradit)
        int targetColor = raster.getPixel(x, y);
        // Barva, kterou budeme vyplňovat
        int fillColor = 0x0000FF; // modrá

        // Pokud je cílová a vyplňovaná barva stejná, nedělej nic
        if (targetColor == fillColor) {
            return;
        }

        // Fronta pro BFS
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point p = queue.remove();
            int px = p.x;
            int py = p.y;

            // Kontrola, že jsme uvnitř plátna
            if (px < 0 || px >= raster.getWidth() || py < 0 || py >= raster.getHeight()) {
                continue;
            }

            // Kontrola, jestli je pixel stále targetColor
            if (raster.getPixel(px, py) != targetColor) {
                continue;
            }

            // Nastavíme novou barvu
            raster.setPixel(px, py, fillColor);

            // Přidáme sousedy (4-směrné; pro 8-směrné vyplňování bychom doplnili i diagonály)
            queue.add(new Point(px + 1, py));
            queue.add(new Point(px - 1, py));
            queue.add(new Point(px, py + 1));
            queue.add(new Point(px, py - 1));
        }

    }
}
