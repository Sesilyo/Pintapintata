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
		if (keyH.upPressed == true) {
			direction = "up";
			y -= speed;
		}
		else if (keyH.downPressed == true) {
			direction = "down";
			y += speed;
		}
		else if (keyH.leftPressed == true) {
			direction = "left";
			x -= speed;
		}
		else if (keyH.rightPressed == true) {
			direction = "right";
			x += speed;
		}
		
		// - - - Brush movement restriction - - -
		int minX = GamePanel.CANVAS_X;
		int minY = GamePanel.CANVAS_Y;
		int maxX = GamePanel.CANVAS_X + GamePanel.CANVAS_SIZE - BRUSH_SIZE;
		int maxY = GamePanel.CANVAS_Y + GamePanel.CANVAS_SIZE - BRUSH_SIZE;
		
		if (x < minX) x = minX;
		if (y < minX) y = minY;
		if (x > maxX) x = maxX;
		if (y > maxX) y = maxX;
	}
}
