package entity;

/**
 * A simple, immutable class representing a 2-dimensional vector or a point
 * in a 2D coordinate grid.
 */
public class Vector2D
{
	// x component of vector
	public double x;
	// y component of vector
	public double y;
	
	/**
	 * Constructs a new Vector2D object with specified coordinates
	 * 
	 * @param x - horizontal component
	 * @param y	- vertical component
	 */
	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
}
