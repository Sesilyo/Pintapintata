package entity;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Brush extends Entity
{
	GamePanel gamePanel;
	main.KeyHandler keyH;
	private BufferedImage brushPNG;
	
	final int SCALE = 2;
	
	final int BRUSH_SIZE = 12;
	
	public Brush(GamePanel gamePanel, main.KeyHandler keyH)
	{
		this.gamePanel = gamePanel;
		this.keyH = keyH;
		
		setDefaultValues();
		getBrushPNG();
	}
	
	public void setDefaultValues()
	{
		x = GamePanel.CANVAS_X + (GamePanel.CANVAS_SIZE / 2);
	    y = GamePanel.CANVAS_Y + (GamePanel.CANVAS_SIZE / 2);
		speed = 4;
	}
	
	public void draw(Graphics2D g2)
	{
		g2.drawImage(brushPNG, x, y,
				brushPNG.getWidth()  * SCALE,
				brushPNG.getHeight() * SCALE,
				null);
	}
	
	public void getBrushPNG()
	{
		try {
			brushPNG = ImageIO.read(getClass().getResourceAsStream("/brush_assets/brush.png"));
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update()
	{
		int x_direction = 0;
		int y_direction = 0;
		
		if (keyH.upPressed)		y_direction -= 1;
		if (keyH.downPressed)	y_direction += 1;
		if (keyH.leftPressed)	x_direction -= 1;
		if (keyH.rightPressed)	x_direction += 1;
		
		// - - - Normalize the Vector - - -
		double dx = x_direction;
		double dy = y_direction;
		double hypotenuse = Math.sqrt(dx * dx + dy * dy);
		
		if (hypotenuse != 0) {
			dx /= hypotenuse;
			dy /= hypotenuse;
		}
		
		// apply vector normalization
		x += dx * speed;
		y += dy * speed;
		
		// - - - Brush movement restriction - - -
		int minX = GamePanel.CANVAS_X;
		int minY = GamePanel.CANVAS_Y;
		int maxX = GamePanel.CANVAS_X + GamePanel.CANVAS_SIZE - BRUSH_SIZE;
		int maxY = GamePanel.CANVAS_Y + GamePanel.CANVAS_SIZE - BRUSH_SIZE;
		
		if (x < minX) x = minX;
		if (x > maxX) x = maxX;
		
		if (y < minY) y = minY;
		if (y > maxY) y = maxY;
	}
}
