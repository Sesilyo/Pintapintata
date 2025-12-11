/*
 * ===================================== 
 * Main.java
 * =====================================
 *
 */
package main;

// === IMPORTS =========================
import javax.swing.*;		// for GUI
import java.awt.*;			// for layout managers
import java.awt.event.*;	// for event handling
import java.awt.color.*;	// for color
import java.awt.Font;		// for font

public class Main
{
	public static void main(String[] args)
	{
		// Create a window
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Pintapintata");
		window.getContentPane().setLayout(new FlowLayout());
		
		// - - - for icon - - -
		ImageIcon icon = new ImageIcon("C:\\Users\\Seth\\eclipse-workspace\\Pintapintata\\res\\brush_assets\\brush.png");
		window.setIconImage(icon.getImage());
		
		GamePanel gamePanel = new GamePanel();
		window.getContentPane().add(gamePanel);
		
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gamePanel.startGameThread();
	}
}