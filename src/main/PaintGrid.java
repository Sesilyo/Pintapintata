package main;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Manage a grid-based painting surface.
 */
public class PaintGrid
{	
	private final int tileSize;
	private final int width, height;
	// 2D boolean array - represents the paint grid and its tiles
	private final boolean [][] painted;
	
	private final int originX, originY; 	// canvas position on JPanel
	private Color paintColor = Color.BLACK; // default color: black
	
	/**
	 * Constructor to initialize the painting grid and its dimensions.
	 * @param originX  - starting x coordinate on the canvas
	 * @param originY  - starting y coordinate on the canvas
	 * @param canvasW  - total width of the canvas
	 * @param canvasH  - total height of the canvas
	 * @param tileSize - size of grid cell
	 */
	public PaintGrid(int originX, int originY,
					 int canvasW, int canvasH,
					 int tileSize)
	{
		this.originX  = originX;
		this.originY  = originY;
		this.tileSize = tileSize;
		
		// calculate the number of tiles inside the canvas
		this.width  = canvasW / tileSize;
		this.height = canvasH / tileSize;
		
		// initialize boolean grid matrix
		painted = new boolean [width][height];
	}
	
	
	/**
	 * Sets current color used when painting.
	 * @param color - random color
	 */
	public void setPaintColor(Color color)
	{
		this.paintColor = color ;
	}
	
	
	/**
	 * Paint tiles that fall within the square brush area
	 * defined by the provided screen coordinates and brush size.
	 * @param px		- x coordinate of the center of the brush
	 * @param py		- y coordinate of the center of the brush
	 * @param brushSize	- diameter of the circular brush
	 */
	public void paintPixel(int px, int py, int brushSize)
	/* paints the pixels within the paint grid matrix,
	 * solves for live coordinates of the brush
	 */
	{
		int radius = brushSize / 2;		// solves for the brush size
		
		// determine relative neighbor tile bounds to check
		int minTileX = Math.floorDiv(px - radius - originX, tileSize);
		int maxTileX = Math.floorDiv(px + radius - originX, tileSize);
		int minTileY = Math.floorDiv(py - radius - originY, tileSize);
		int maxTileY = Math.floorDiv(py + radius - originY, tileSize);
		
		// clamp bounds to ensure actual grid dimensions are respected
		minTileX = Math.max(0, minTileX);
		minTileY = Math.max(0, minTileY);
		maxTileX = Math.min(width  - 1, maxTileX);
		maxTileY = Math.min(height - 1, maxTileY);
		
		// iterate through determined bounding box of tiles
		for (int tx = minTileX; tx <= maxTileX; tx++) {
			for (int ty = minTileY; ty <= maxTileY; ty++) {
				// safety check just in case out of bounds might happen
				if (tx < 0 || ty < 0 || tx >= width || ty >= height) continue;
				
				// tile center
				int cx = originX + tx * tileSize + tileSize / 2;
				int cy = originY + ty * tileSize + tileSize / 2;
				
				// distance from the brush center
				int dx = cx - px;
				int dy = cy - py;
				
				// uses distance formula squared to check if the tile is within brush area
				if (dx * dx + dy * dy <= radius * radius) {
					painted[tx][ty] = true;		// marks a tile as painted
				}
			}
		}
	}
	
	
	/**
	 * Renders current painted tiles onto provided Graphics2D context.
	 * @param g2 - Graphics2D context to draw upon
	 */
	public void draw(Graphics2D g2)
	{
		g2.setColor(paintColor);	// random color from color pool of GamePanel class
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (painted [x][y]) {
					// calculate top-left screen position for the painted tile
					int px = originX + x * tileSize;
					int py = originY + y * tileSize;
					g2.fillRect(px, py, tileSize, tileSize);
				}
			}
		}
	}
	
	
	/**
	 * Calculates current percentage of the grid that has been painted.
	 * @return the percentage of painted tiles as a double (0.00 - 100.00)
	 */
	public double getPaintProgress()
	{
		int paintedCount = 0;
		int total = width * height;
		
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (painted[x][y]) paintedCount++;
			}
		}
		// return percentage formatted to two decimal places
		return (paintedCount * 100.00) / total;
	}
	
}
