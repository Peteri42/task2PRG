import fill.SeedFill;
import model.Line;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerBold;
import rasterize.LineRasterizerGraphics;
import rasterize.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

/**
 * trida pro kresleni na platno: vyuzita tridy RasterBufferedImage
 * 
 * @author PGRF FIM UHK
 * @version 2020
 */

public class CanvasRasterBufferedImage {

	private final JPanel panel;
	private RasterBufferedImage raster;
	private Point startPoint = new Point(-1, -1);
	private Point currentPoint = new Point(-1, -1);
	private Point selectedPoint = new Point(-1, -1);
	private LineRasterizer rasterizer;
	private SeedFill seedFill;
	private final Vector<Line> lines = new Vector<>();
	private final ArrayList<Polygon> polygons = new ArrayList<>();
	private boolean shiftMode = false;
	private boolean boldMode = false;

	public CanvasRasterBufferedImage(int width, int height) {
		JFrame frame = new JFrame();

		frame.setLayout(new BorderLayout());

		frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
		frame.setResizable(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		raster = new RasterBufferedImage(width, height);
		rasterizer = new LineRasterizerGraphics(raster);
		seedFill = new SeedFill(raster);
		panel = new JPanel() {
			@Serial
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				present(g);
			}
		};
		panel.setPreferredSize(new Dimension(width, height));

		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_SHIFT:
						shiftMode = true;
						break;
					case KeyEvent.VK_C:
						start();
						break;
					case KeyEvent.VK_X:
						if(!boldMode) {
							rasterizer = new LineRasterizerBold(raster);
							boldMode = true;
						}
						else if(boldMode) {
							rasterizer = new LineRasterizerGraphics(raster);
							boldMode = false;
						}
						update();
						break;
				}
				panel.repaint();
			}
			@Override
			public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT)
                    shiftMode = false;
			}
		});
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					Point rawPoint = new Point(e.getX(), e.getY());
					startPoint = getNearestPoint(rawPoint);
					currentPoint = new Point(e.getX(), e.getY());
					rasterizer.drawLine(startPoint,currentPoint);
				}
				if(e.getButton() == MouseEvent.BUTTON2) {
					seedFill.setStartingPoint(e.getX(), e.getY());
					seedFill.fill();
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					selectedPoint = getNearestPoint(new Point(e.getX(),e.getY()));
				}
				panel.repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON1) {
					lines.add(new Line(startPoint,currentPoint,0xff0000));
					startPoint = new Point(-1, -1);
					currentPoint = new Point(-1, -1);
				}
				if (e.getButton() == MouseEvent.BUTTON3)
					selectedPoint = new Point(-1, -1);
			}
		});

		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				panel.repaint();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				update();
				currentPoint = new Point(e.getX(), e.getY());
				//pokus o editaci bodu
				if(selectedPoint.x != -1 && selectedPoint.y != -1) {
					selectedPoint.x = e.getX();
					selectedPoint.y = e.getY();
				}
				else
				{
					if(shiftMode)
					{
						Point rawPoint = getAlignedPoint(startPoint, currentPoint);
						currentPoint = getNearestPoint(rawPoint);
					}
					else
					{
						Point rawPoint = new Point(e.getX(), e.getY());
						currentPoint = getNearestPoint(rawPoint);
					}
					if(startPoint.x != -1 && startPoint.y != -1) {
						if(startPoint.x > raster.getWidth())
							startPoint.x = raster.getWidth();
						else if(startPoint.x < 0)
							startPoint.x = 0;
						if(startPoint.y > raster.getHeight())
							startPoint.y = raster.getHeight();
						else if(startPoint.y < 0)
							startPoint.y = 0;
						rasterizer.drawLine(startPoint,currentPoint);
					}
				}

			}
		});

		panel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (panel.getWidth()<1 || panel.getHeight()<1)
					return;
				if (panel.getWidth()<=raster.getWidth() && panel.getHeight()<=raster.getHeight()) //no resize if new one is smaller
					return;
				RasterBufferedImage newRaster = new RasterBufferedImage(panel.getWidth(), panel.getHeight());

				newRaster.draw(raster);
				raster = newRaster;
				rasterizer = new LineRasterizerGraphics(raster);
			}
		});
	}

	public void clear(int color) {
		raster.setClearColor(color);
		raster.clear();
	}
	private Point getAlignedPoint(Point start, Point current) {
		int dx = current.x - start.x;
		int dy = current.y - start.y;

		if (dx == 0 && dy == 0) {
			return current;
		}

		double angle = Math.toDegrees(Math.atan2(dy, dx));
		if (angle < 0) angle += 360;


		int[] angles = {0, 45, 90, 135, 180, 225, 270, 315};
		int closestAngle = angles[0];
		double minDifference = 360;

		for (int a : angles) {
			double difference = Math.abs(angle - a);
			if (difference < minDifference) {
				minDifference = difference;
				closestAngle = a;
			}
		}

		double rad = Math.toRadians(closestAngle);
		double length = Math.sqrt(dx * dx + dy * dy);
		int alignedX = (int) Math.round(start.x + length * Math.cos(rad));
		int alignedY = (int) Math.round(start.y + length * Math.sin(rad));

		return new Point(alignedX, alignedY);
	}

	private Point getNearestPoint(Point current) {
		for (Line line : lines) {
			if (distance(line.getStartPoint(), current) <= 25) {
				return line.getStartPoint();
			}
			if (distance(line.getEndPoint(), current) <= 25) {
				return line.getEndPoint();
			}
		}
		return current;
	}

	private double distance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}
	public void update() {
		clear(0xaaaaaa);
		raster.getGraphics().drawString("Use mouse buttons and try resize the window", 5, 15);
		for (Line line : lines) {
			rasterizer.drawLine(line);
		}
		panel.repaint();
	}
	public void present(Graphics graphics) {
		raster.repaint(graphics);
	}

	public void start() {
		clear(0xaaaaaa);
		lines.clear();
		raster.getGraphics().drawString("Use mouse buttons and try resize the window", 5, 15);
		panel.repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new CanvasRasterBufferedImage(800, 600).start());
	}
}
