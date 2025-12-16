package main;

import java.awt.*;


/**
 * A simple, visually distinct exit button.
 */
public class MasterExitButton
{
	private final Rectangle bounds;
	private final String label = "X";
	
	/**
	 * Constructor to initialize the exit button's position & size.
	 * @param x	- x-coordinate start of rectangle
	 * @param y - y-coordinate start of rectangle
	 * @param size - width and height of the square button
	 */
	// === CONSTRUCTOR ===
	public MasterExitButton(int x, int y, int size)
	{
		bounds = new Rectangle(x, y, size, size);
	}
	
	
	/**
	 * Renders the exit button onto the provided Graphics2D context.
	 * Button is drawn as a red square with a white "X" inside it.
	 * 
	 * @param g2 - Graphics2D context used for drawing
	 */
	public void draw(Graphics2D g2)
	{
		// draws the rectangle of the button
		g2.setColor(Color.RED);
		g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		
		// draws the "X" label inside the rectangle
		g2.setColor(Color.WHITE);
		g2.setFont(FontManager.PIXEL_FONT.deriveFont(16f));
		
		// Calculate position required to center the text perfectly within the bounds
		FontMetrics fm = g2.getFontMetrics();
		int tx = bounds.x + ((bounds.width  - fm.stringWidth(label)) / 2);		     // calculate x
		int ty = bounds.y + ((bounds.height - fm.getHeight()) / 2) + fm.getAscent(); // calculate y
		
		g2.drawString(label, tx, ty);
	}
	
	
	/**
	 * Checks if given mouse coordinates fall within button's bounds
	 * 
	 * @param mouseX - x coordinate of mouse's click
	 * @param mouseY - y coordinate of mouse's click
	 * @return True if the button was clicked, false otherwise
	 */
	public boolean isClicked(int mouseX, int mouseY)
	{
		return bounds.contains(mouseX, mouseY);
	}
}
