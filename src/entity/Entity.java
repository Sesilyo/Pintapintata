package entity;

import java.awt.image.BufferedImage;

/**
 * A base class representing an entity within the game.
 * Holds fundamental properties like position, speed, and visuals.
 */
public class Entity
{
	// coordinates for entity's position
	public int x, y;
	
	// speed at which the entity moves
	public int speed;
	
	// image used to render the entity visually
	public BufferedImage brushPNG;
	
	// direction the entity is facing
	public String direction;
}
