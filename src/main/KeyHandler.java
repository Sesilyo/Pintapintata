package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles keyboard input events for the game.
 */
public class KeyHandler implements KeyListener
{
	// movement flags
	public boolean upPressed, downPressed, leftPressed, rightPressed;
	
	// action flags
	public boolean spacePressed, rPressed, escPressed;
	
	
	/**
	 * Unused in this program.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// pass...
	}

	
	/**
	 * Invoked when a key has been pressed down.
	 * Sets corresponding flags to true.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();	// get integer key code of corresponding key
		
		if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed    	  = true;
		if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed    = true;
		if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed    = true;
		if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed  = true;
		
		if (code == KeyEvent.VK_SPACE)  spacePressed = true;
		if (code == KeyEvent.VK_R) 		rPressed 	 = true;
		if (code == KeyEvent.VK_ESCAPE) escPressed 	 = true;
		
	}

	
	/**
	 * Invoked when key has been released.
	 * Resets corresponding flags to false.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed    	 = false;
		if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed   = false;
		if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed   = false;
		if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = false;
		
		if (code == KeyEvent.VK_SPACE)  spacePressed = false;
		if (code == KeyEvent.VK_R) 		rPressed 	 = false;
		if (code == KeyEvent.VK_ESCAPE) escPressed 	 = false;
	}

}