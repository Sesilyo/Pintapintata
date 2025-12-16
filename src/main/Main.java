package main;

// === IMPORTS =========================
import javax.swing.*;		// for GUI
import java.awt.*;			// for layout managers & Image objects

/**
 * Main class that starts the Java Swing application
 * entry point for the Pintapintata application.
 */
public class Main
{
	/**
	 * Actual entry point of the application.
	 * @param args - command line arguments
	 */
	public static void main(String[] args)
	{
		// Create a window
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// close operation behavior
		window.getContentPane().setLayout(new FlowLayout());	// use simple layout manager
		window.setUndecorated(true);	// removes native OS window border
		
		// - - - for icon - - -
		Image icon = new ImageIcon(
				Main.class.getResource("/brush_assets/brush.png")
		).getImage();
		window.setIconImage(icon);
		
		// initialize and add the main content panel
		GamePanel gamePanel = new GamePanel();
		window.getContentPane().add(gamePanel);
		
		window.pack();	// finalize window display
		
		window.setLocationRelativeTo(null);	// center window on screen
		window.setVisible(true);			// makes window visible to user
		
		// start the game loop/rendering thread
		gamePanel.startGameThread();
	}
}