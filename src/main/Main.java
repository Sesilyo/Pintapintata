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

public class Main
{
	public static void main(String[] args)
	{
		// Create a window
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Pintapintata");
		window.setLayout(new FlowLayout());
		
		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);
		
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gamePanel.startGameThread();
	}
}