package main;

import java.awt.*;

public class MasterExitButton
{
	private final Rectangle bounds;
	private final String label = "X";
	
	// === CONSTRUCTOR ===
	public MasterExitButton(int x, int y, int size)
	{
		bounds = new Rectangle(x, y, size, size);
	}
	
	
	public void draw(Graphics2D g2)
	{
		// draws the rectangle of the button
		g2.setColor(Color.RED);
		g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		
		// draws the "X" label inside the rectangle
		g2.setColor(Color.WHITE);
		g2.setFont(FontManager.PIXEL_FONT.deriveFont(16f));
		
		// 
		FontMetrics fm = g2.getFontMetrics();
		int tx = bounds.x + ((bounds.width  - fm.stringWidth(label)) / 2);
		int ty = bounds.y + ((bounds.height - fm.getHeight()) / 2) + fm.getAscent();
		
		g2.drawString(label, tx, ty);
	}
	
	
	public boolean isClicked(int mouseX, int mouseY)
	{
		return bounds.contains(mouseX, mouseY);
	}
}
