package main;

// === IMPORTS =========================
import javax.swing.*;		// for GUI
import java.awt.*;			// for layout managers

/**
 * main class that starts the Java Swing application
 * entry point for the Pintapintata application
 */
public class Main
{
	/**
	 * actual entry point of the application
	 * @param args - command line arguments
	 */
	public static void main(String[] args)
	{
		// Create a window
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(new FlowLayout());
		window.setUndecorated(true);
		
		// - - - for icon - - -
		Image icon = new ImageIcon(
				Main.class.getResource("/brush_assets/brush.png")
		).getImage();
		window.setIconImage(icon);
		
		GamePanel gamePanel = new GamePanel();
		window.getContentPane().add(gamePanel);
		
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gamePanel.startGameThread();
	}
}