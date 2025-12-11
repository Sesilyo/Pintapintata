package entity;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.LogisticFunction;

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
	
	
	final int BRUSH_SIZE = 12;		// size of brush
	
	
	// - - - Initialize Logistic Function fields - - -
	private double speedTimer = 0;
	private final double TIMER_INCREMENT = 0.1;
	private final double BASE_SPEED = 2.0;
	
	private LogisticFunction logisticFunc;
	
	
	// = = = BRUSH CONSTRUCTOR = = =
	public Brush(GamePanel gamePanel, main.KeyHandler keyH)
	{
		this.gamePanel = gamePanel;
		this.keyH = keyH;
		
		setDefaultValues();
		getBrushPNG();
		
		// set values for Logistic function
		logisticFunc = new LogisticFunction(
				6.0,	// L  : max speed of brush
				1.0,	// k  : growth rate of brush speed
				10.0	// x0 : midpoint
		);
	}
	
	
	public void draw(Graphics2D g2)
	{
		g2.drawImage(brushPNG, (int)posX, (int)posY,
					 actualBrushWidth, actualBrushHeight,
					 null);
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
	
	
	public void update()
	{
		// get direction from keys
		Vector2D input = validateMovement();
		boolean moving = (input.x != 0 || input.y != 0);
		
		// Logistic function speed timer
		if (moving) speedTimer += TIMER_INCREMENT;
		else 		speedTimer = 0;
		
		double dynamicSpeed = BASE_SPEED + logisticFunc.logiFunc(speedTimer);
		
		// apply vector normalization
		Vector2D direction = normalizeVector(input);
		
		// apply to movement
		posX += direction.x * dynamicSpeed;
		posY += direction.y * dynamicSpeed;		
		
		// - - - Brush movement restriction - - -
		double minX = GamePanel.CANVAS_X - 16;
		double minY = GamePanel.CANVAS_Y - 16;
		
		double maxX = GamePanel.CANVAS_X + GamePanel.CANVAS_WIDTH  - actualBrushWidth  + 10;
		double maxY = GamePanel.CANVAS_Y + GamePanel.CANVAS_HEIGHT - actualBrushHeight + 10;
		
		if (posX < minX) posX = minX;
		if (posX > maxX) posX = maxX;
		
		if (posY < minY) posY = minY;
		if (posY > maxY) posY = maxY;
		
		// painting logic
		if (keyH.spacePressed) {
			int centerX = (int) (posX + actualBrushWidth  / 2);
			int centerY = (int) (posY + actualBrushHeight / 2);
			
			gamePanel.paintGrid.paintPixel(centerX, centerY, BRUSH_SIZE);
		}
	}
}
