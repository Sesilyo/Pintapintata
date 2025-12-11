package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PaintGrid
{	
	private final int tileSize;
	private final int width, height;
	private final boolean [][] painted;
	
	private final int originX, originY; // canvas position on JPanel
	
	public PaintGrid(int originX, int originY,
					 int canvasW, int canvasH,
					 int tileSize)
	{
		this.originX  = originX;
		this.originY  = originY;
		this.tileSize = tileSize;
		
		this.width  = canvasW / tileSize;
		this.height = canvasH / tileSize;
		
		painted = new boolean [width][height];
	}
	
	public void paintPixel(int px, int py, int brushSize)
	{
		int startX = (px - originX) / tileSize;
		int startY = (py - originY) / tileSize;
		
		int tileAcross = Math.max(1, brushSize / tileSize);
		
		for (int x = 0; x < tileAcross; x++) {
			for (int y = 0; y < tileAcross; y++) {
				
				int tx = startX + x;
				int ty = startY + y;
				
				if (tx >= 0 && tx < width && ty >= 0 && ty < height) {
					painted [tx][ty] = true;
				}
			}
		}
	}
	
	public void draw(Graphics2D g2)
	{
		g2.setColor(Color.BLACK);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (painted [x][y]) {
					int px = originX + x * tileSize;
					int py = originY + y * tileSize;
					g2.fillRect(px, py, tileSize, tileSize);
				}
			}
		}
	}
	
	public double getPaintProgress()
	{
		int paintedCount = 0;
		int total = width * height;
		
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (painted[x][y]) paintedCount++;
			}
		}
		return (paintedCount * 100.00) / total;
	}
	
}
