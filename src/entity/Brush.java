package entity;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.LogisticFunction;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


/**
 * Represents the player-controlled brush in-game.
 * Manages movement, visual representation, brush size & speed dynamics,
 * and interaction with the PaintGrid.
 */
public class Brush extends Entity
{
	// - - - Main Declarations/Initializations - - -
	private final GamePanel gamePanel;	 // initialize game panel
	private final main.KeyHandler keyH;	 // gets keyH variable for event listener
	private BufferedImage brushPNG;		 // initialize default brush image
	
	final int SCALE = 2;			// scale factor for scaling
	
	private int actualBrushWidth;
	private int actualBrushHeight;
	
	// movement fields in double data type to avoid "jittery" movement
	private double posX, posY;
	
	
	final   int BRUSH_SIZE = 12;	// base size of brush
	private int currentBrushSize;	// dynamic size used for painting
	
	
	// - - - Initialize Logistic Function fields - - -
	// +- for speed --------------------------+
	private double speedTimer = 0;					 // timer for how long a key is held
	private final double TIMER_INCREMENT = 0.1;		 // constant rate which the size timer increases
	private final double BASE_SPEED = 2.0;			 // constant speed, minimum speed when not moving
	private LogisticFunction brushSpeedLogisticFunc; // function to calculate speed based on speedTimer
	// +--------------------------------------+
	
	// +- for brush --------------------------+
	private double sizeTimer = 0;					 // timer how long space key is held
	private final double SIZE_TIMER_INCREMENT = 0.1; // constant rate which the size timer increases
	private final int BASE_BRUSH_SIZE = 12;			 // constant size, minimum of paint area
	private LogisticFunction brushSizeLogisticFunc;	 // function to calculate brush size based on sizeTimer
	// +--------------------------------------+
	
	
	// pen tip offset for paint accuracy
	private final int TIP_OFFSET_X = 2;
	private final int TIP_OFFSET_Y = 35;
	
	
	/**
	 * Constructor. 
	 * 
	 * @param gamePanel	- main game panel instance.
	 * @param keyH		- key input handler instance
	 */
	public Brush(GamePanel gamePanel, main.KeyHandler keyH)
	{
		this.gamePanel = gamePanel;
		this.keyH = keyH;
		
		getBrushPNG();		// load image and set dimensions
		setDefaultValues();	// set initial position and speed
		
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
	
	
	/**
	 * Renders brush sprite and paint preview overlay.
	 * 
	 * @param g2 - Graphics2D context for drawing
	 */
	public void draw(Graphics2D g2)
	{
		// draw brush sprite
		g2.drawImage(brushPNG, (int)posX, (int)posY,
					 actualBrushWidth, actualBrushHeight,
					 null);
		
		// draw preview of brush sprite
		if (keyH.spacePressed) drawPaintPreview(g2, currentBrushSize);
	}
	
	
	/**
	 * Determines directional vector based on currently pressed movement keys.
	 * 
	 * @return Vector2D representing the desired movement direction
	 */
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
	
	
	/**
	 * Loads brush image resource from the classpath.
	 */
	public void getBrushPNG()
	{
		try {
			// load image using resource stream method for stability
			brushPNG = ImageIO.read(getClass().getResourceAsStream("/brush_assets/brush.png"));
		
			// calculate scaled brush dimension
			actualBrushWidth  = brushPNG.getWidth()  * SCALE;
			actualBrushHeight = brushPNG.getHeight() * SCALE;
		}
		
		catch (IOException e) {
			e.printStackTrace();
			actualBrushWidth = actualBrushHeight = BRUSH_SIZE;
		}
	}
	
	
	/**
	 * Resets brush position and state to default (before game start).
	 */
	public void setDefaultValues()
	{
		// center bush within canvas area
		x = GamePanel.CANVAS_X + (GamePanel.CANVAS_WIDTH  - actualBrushWidth)  / 2;
		y = GamePanel.CANVAS_Y + (GamePanel.CANVAS_HEIGHT - actualBrushHeight) / 2;
		speed = 4;
	}
	
	
	/**
	 * Normalizes vector to a unit vector for consistent diagonal movement speed.
	 * 
	 * @param v	- input movement vector
	 * @return normalized vector (or (0, 0))
	 */
	private Vector2D normalizeVector(Vector2D v)
	{
		double hypotenuse = Math.sqrt(v.x * v.x + v.y * v.y);
		
		if (hypotenuse != 0) return new Vector2D(
				v.x / hypotenuse,
				v.y / hypotenuse
		);
		return new Vector2D(0, 0);
	}
	
	
	/**
	 * Draws a semi-transparent preview of the paint area.
	 * 
	 * @param g2		- graphics context
	 * @param brushSize	- diameter of paint preview
	 */
	private void drawPaintPreview(Graphics2D g2, int brushSize)
	{
		int paintRadius = brushSize / 2;
		
		// reddish, translucent overlay
		g2.setColor(new Color(255, 0, 55, 80));
		// draw rectangle centered around calculated paint tip coordinates
		g2.fillRect(
				getPaintX() - paintRadius,
				getPaintY() - paintRadius,
				brushSize,
				brushSize
		);
	}
	
	
	// - - - helper methods to get brush offset - - -
	private int getPaintX() { return (int)(posX + TIP_OFFSET_X);}
	private int getPaintY() { return (int)(posY + TIP_OFFSET_Y);}
	
	
	/**
	 * Called every game tick (or update loop) to move brush, handle timers,
	 * positions in-canvas, and apply paint.
	 */
	public void update()
	{
		// get direction from keys
		Vector2D input = validateMovement();
		boolean moving = (input.x != 0 || input.y != 0);
		
		// Logistic function speed timer
		if (moving) speedTimer += TIMER_INCREMENT;
		else 		speedTimer = 0;	// resets timer if movement stops
		
		double dynamicSpeed = BASE_SPEED + brushSpeedLogisticFunc.logiFunc(speedTimer);
		
		// apply vector normalization
		Vector2D direction = normalizeVector(input);
		
		// apply to movement
		posX += direction.x * dynamicSpeed;
		posY += direction.y * dynamicSpeed;		
		
		// - - - Brush movement restriction - - -
		int maxBrushRadius = (BASE_BRUSH_SIZE + (int) brushSizeLogisticFunc.getMaxVal()) / 2;
		double minX = GamePanel.CANVAS_X - TIP_OFFSET_X + maxBrushRadius;
		double minY = GamePanel.CANVAS_Y - TIP_OFFSET_Y + maxBrushRadius;
		
		double maxX = GamePanel.CANVAS_X + GamePanel.CANVAS_WIDTH  - TIP_OFFSET_X - maxBrushRadius;
		double maxY = GamePanel.CANVAS_Y + GamePanel.CANVAS_HEIGHT - TIP_OFFSET_Y - maxBrushRadius;
		
		// clamp posX and posY to stay within valid canvas boundaries
		if (posX < minX) posX = minX;
		if (posX > maxX) posX = maxX;
		
		if (posY < minY) posY = minY;
		if (posY > maxY) posY = maxY;
		
		// painting logic
		if (keyH.spacePressed) sizeTimer += SIZE_TIMER_INCREMENT;
		else sizeTimer = 0;
		
		// calculating dynamic brush size
		currentBrushSize = BASE_BRUSH_SIZE + (int) brushSizeLogisticFunc.logiFunc(sizeTimer);
		
		// apply paint to grid if space key is held
		if (keyH.spacePressed) {
			gamePanel.paintGrid.paintPixel(getPaintX(), getPaintY(), currentBrushSize);
		}

		
	}
}
