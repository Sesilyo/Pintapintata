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
		int radius = brushSize / 2;
		
		// determine relative neighbor tile bounds to check
		int minTileX = Math.floorDiv(px - radius - originX, tileSize);
		int maxTileX = Math.floorDiv(px + radius - originX, tileSize);
		int minTileY = Math.floorDiv(py - radius - originY, tileSize);
		int maxTileY = Math.floorDiv(py + radius - originY, tileSize);
		
		minTileX = Math.max(0, minTileX);
		minTileY = Math.max(0, minTileY);
		maxTileX = Math.min(width  - 1, maxTileX);
		maxTileY = Math.min(height - 1, maxTileY);
		
		for (int tx = minTileX; tx <= maxTileX; tx++) {
			for (int ty = minTileY; ty <= maxTileY; ty++) {
				if (tx < 0 || ty < 0 || tx >= width || ty >= height) continue;
				
				// tile center
				int cx = originX + tx * tileSize + tileSize / 2;
				int cy = originY + ty * tileSize + tileSize / 2;
				
				int dx = cx - px;
				int dy = cy - py;
				
				if (dx * dx + dy * dy <= radius * radius) {
					painted[tx][ty] = true;
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
