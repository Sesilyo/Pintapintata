package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Manages display of the Game-Over screen overlay.
 * Includes the display of final score/time and restart/exit options.
 */
public class GameOverScreen
{
	private boolean visible;
	private double timeFinish;
	
	// === for Game Over screen dimensions ===
	private final int scrnWidth;
	private final int scrnHeight;
	
	/**
	 * Initializes the Game Over screen manager with dimensions of
	 * display area.
	 * @param scrnWidth  - width of game panel
	 * @param scrnHeight - height of game panel
	 */
	public GameOverScreen(int scrnWidth, int scrnHeight)
	{
		this.scrnWidth  = scrnWidth;
		this.scrnHeight = scrnHeight;
		this.visible 	= false;		// screen is hidden by default
	}
	
	
	/**
	 * Displays Game Over screen & records final time of player.
	 * @param time - player's final time in seconds
	 */
	public void show(double time)
	{
		this.timeFinish = time;
		this.visible = true;
	}
	
	
	/**
	 * Hides the Game Over screen.
	 */
	public void hide()
	{
		this.visible = false;
	}
	
	
	/**
	 * Checks if the Game Over screen is currently visible.
	 * @return - True if visible, false otherwise
	 */
	public boolean isVisible()
	{
		return visible;
	}
	
	
	/**
	 * Renders Game Over screen onto provided Graphics2D context.
	 * @param g2 - Graphics2D context used for drawing
	 */
	public void draw(Graphics2D g2)
	{
		if (!visible) return;
		
		// disable anti-aliasing for better pixel text
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
		);
		
		// set translucent overlay over GamePanel
		g2.setColor(new Color(0, 0, 0, 180));
		g2.fillRect(0, 0, scrnWidth, scrnHeight);
		
		// game over text
		g2.setColor(Color.WHITE);
		g2.setFont(FontManager.PIXEL_FONT.deriveFont(48f));
		String gameOverText = "GAME OVER!";
		int textWidth = g2.getFontMetrics().stringWidth(gameOverText);
		g2.drawString(gameOverText, (scrnWidth - textWidth) / 2, 250);
		
		// display the overall-time of player
		g2.setFont(FontManager.PIXEL_FONT.deriveFont(32f));
		String timeText = String.format("Time: %.2f s", timeFinish);
		textWidth = g2.getFontMetrics().stringWidth(timeText);
		g2.drawString(timeText, (scrnWidth - textWidth) / 2, 320);
		
		// extra displays
		// +--- for restart ---+
		g2.setFont(FontManager.PIXEL_FONT.deriveFont(20f));
		String restartText = "Press R to restart";
		textWidth = g2.getFontMetrics().stringWidth(restartText);
		g2.drawString(restartText, (scrnWidth - textWidth) / 2, 400);
		
		// +--- for exit ---+
		g2.setFont(FontManager.PIXEL_FONT.deriveFont(20f));
		String quitText = "Press ESC to quit";
		textWidth = g2.getFontMetrics().stringWidth(quitText);
		g2.drawString(quitText, (scrnWidth - textWidth) / 2, 440);
		
		// re-enable anti-aliasing for other elements
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		);
	}
}
