package entity;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.LogisticFunction;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Brush extends Entity
{
	// - - - Main Declarations/Initializations - - -
	GamePanel gamePanel;			// initialize game panel
	main.KeyHandler keyH;			// gets keyH variable for event listener
	private BufferedImage brushPNG;	// initialize default brush image
	
	final int SCALE = 2;			// scale up canvas and pixel sizes
	
	private int actualBrushWidth;
	private int actualBrushHeight;
	
	// movement fields in double data type to avoid "jittery" movement
	private double posX, posY;
	
	
	final   int BRUSH_SIZE = 12;		// base size of brush
	private int currentBrushSize;
	
	
	// - - - Initialize Logistic Function fields - - -
	// +- for speed --------------------------+
	private double speedTimer = 0;
	private final double TIMER_INCREMENT = 0.1;
	private final double BASE_SPEED = 2.0;
	private LogisticFunction brushSpeedLogisticFunc;
	// +--------------------------------------+
	
	// +- for brush --------------------------+
	private double sizeTimer = 0;
	private final double SIZE_TIMER_INCREMENT = 0.1;
	private final int BASE_BRUSH_SIZE = 12;
	private LogisticFunction brushSizeLogisticFunc;
	// +--------------------------------------+
	
	
	// pen tip offset for paint accuracy
	private final int TIP_OFFSET_X = 10;
	private final int TIP_OFFSET_Y = 57;
	
	
	// = = = BRUSH CONSTRUCTOR = = =
	public Brush(GamePanel gamePanel, main.KeyHandler keyH)
	{
		this.gamePanel = gamePanel;
		this.keyH = keyH;
		
		getBrushPNG();
		setDefaultValues();
		
		// set values for Logistic function
		brushSpeedLogisticFunc = new LogisticFunction(
				6.0,	// L  : max speed of brush
				1.0,	// k  : growth rate of brush speed
				10.0	// x0 : midpoint
		);
		
		brushSizeLogisticFunc = new LogisticFunction(
				12.0,	// L  : max side added
				0.5,	// k  : growth rate of brush size
				8.0		// x0 : midpoint
		);
	}
	
	
	public void draw(Graphics2D g2)
	{
		// draw brush sprite
		g2.drawImage(brushPNG, (int)posX, (int)posY,
					 actualBrushWidth, actualBrushHeight,
					 null);
		
		// draw preview of brush sprite
		if (keyH.spacePressed) drawPaintPreview(g2, currentBrushSize);
	}
	
	
	// - - - detect & validate movement - - -
	private Vector2D validateMovement()
	{
		int x_direction = 0;
		int y_direction = 0;
		
		if (keyH.upPressed) 	y_direction -= 1;
		if (keyH.downPressed) 	y_direction += 1;
		if (keyH.leftPressed) 	x_direction -= 1;
		if (keyH.rightPressed) 	x_direction += 1;
		
		return new Vector2D(x_direction, y_direction);
	}
	
	
	public void getBrushPNG()
	{
		try {
			brushPNG = ImageIO.read(getClass().getResourceAsStream("/brush_assets/brush.png"));
		
			actualBrushWidth  = brushPNG.getWidth()  * SCALE;
			actualBrushHeight = brushPNG.getHeight() * SCALE;
		}
		
		catch (IOException e) {
			e.printStackTrace();
			actualBrushWidth = actualBrushHeight = BRUSH_SIZE;
		}
	}
	
	public void setDefaultValues()
	{
		x = GamePanel.CANVAS_X + (GamePanel.CANVAS_WIDTH  - actualBrushWidth)  / 2;
		y = GamePanel.CANVAS_Y + (GamePanel.CANVAS_HEIGHT - actualBrushHeight) / 2;
		speed = 4;
	}
	
	// - - - method for Vector Normalization - - -
	private Vector2D normalizeVector(Vector2D v)
	{
		double hypotenuse = Math.sqrt(v.x * v.x + v.y * v.y);
		
		if (hypotenuse != 0) return new Vector2D(
				v.x / hypotenuse,
				v.y / hypotenuse
		);
		return new Vector2D(0, 0);
	}
	
	private void drawPaintPreview(Graphics2D g2, int brushSize)
	{
		int paintRadius = brushSize / 2;
		
		g2.setColor(new Color(255, 0, 55, 80));
		g2.fillRect(
				getPaintX() - paintRadius,
				getPaintY() - paintRadius,
				brushSize,
				brushSize
		);
	}
	
	
	// - - - helper methods to get brush center - - -
	private int getPaintX() { return (int)(posX + TIP_OFFSET_X  / 2);}
	private int getPaintY() { return (int)(posY + TIP_OFFSET_Y  / 2);}
	
	
	public void update()
	{
		// get direction from keys
		Vector2D input = validateMovement();
		boolean moving = (input.x != 0 || input.y != 0);
		
		// Logistic function speed timer
		if (moving) speedTimer += TIMER_INCREMENT;
		else 		speedTimer = 0;
		
		double dynamicSpeed = BASE_SPEED + brushSpeedLogisticFunc.logiFunc(speedTimer);
		
		// apply vector normalization
		Vector2D direction = normalizeVector(input);
		
		// apply to movement
		posX += direction.x * dynamicSpeed;
		posY += direction.y * dynamicSpeed;		
		
		// - - - Brush movement restriction - - -
		double minX = GamePanel.CANVAS_X;
		double minY = GamePanel.CANVAS_Y;
		
		double maxX = GamePanel.CANVAS_X + GamePanel.CANVAS_WIDTH  - TIP_OFFSET_X;
		double maxY = GamePanel.CANVAS_Y + GamePanel.CANVAS_HEIGHT - TIP_OFFSET_Y;
		
		if (posX < minX) posX = minX;
		if (posX > maxX) posX = maxX;
		
		if (posY < minY) posY = minY;
		if (posY > maxY) posY = maxY;
		
		// painting logic
		if (keyH.spacePressed) sizeTimer += SIZE_TIMER_INCREMENT;
		else sizeTimer = 0;
		
		currentBrushSize = BASE_BRUSH_SIZE + (int)brushSizeLogisticFunc.logiFunc(sizeTimer);
		if (keyH.spacePressed) gamePanel.paintGrid.paintPixel(getPaintX(), getPaintY(), currentBrushSize);
		
	}
}
