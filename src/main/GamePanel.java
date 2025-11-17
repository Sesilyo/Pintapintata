package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

// === OWN CLASSES ======================================
import main.KeyHandler;
import entity.Brush;

public class GamePanel extends JPanel implements Runnable
{
	// === SCREEN SETTINGS ==============================
	final int scrnWidth  = 950;
	final int scrnHeight = 950;
	// - - - CANVAS SETTINGS - - -
	public final static int CANVAS_SIZE = 800;
	public final static int CANVAS_X = 150;
	public final static int CANVAS_Y = 0;
	
	// === set FPS ======================================
	final int FPS = 60;
	
	// === for MOVEMENT =================================
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	
	// === for ENTITIES =================================
	Brush brush;
	
	// === MAIN CONSTRUCTOR =============================
	public GamePanel()
	{
		this.setPreferredSize(new Dimension(scrnWidth, scrnHeight));
		this.setBackground(Color.gray);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		
		brush = new Brush(this, keyH);	// instantiate Brush here
	}
	
	public void startGameThread()
	{
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run()
	{
		double drwInterval = 1000000000 / FPS;
		double delta  = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while (gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drwInterval;
			lastTime = currentTime;
			
			if (delta >= 1) {
				update();
				repaint();
				delta--;
			}
		}
	}
	
	public void update()
	{
		brush.update();	// keeps brush moving with key inputs
	}
	
	// === DRAW | the "pencil" per se ==========================
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
				
		// - - - draw canvas area - - -
		g2.setColor(Color.WHITE);
		// note:::	fillRect parameters:::
		// (x-cord of top left corner of rectangle, y-cord of the top-left corner of rectangle...)
		// (..., width of rectangle, height of rectangle)
		g2.fillRect(CANVAS_X, CANVAS_Y, CANVAS_SIZE, CANVAS_SIZE);
		
		// - - - draw canvas border - - -
		g2.setColor(Color.BLACK);
		g2.drawRect(CANVAS_X, CANVAS_Y, CANVAS_SIZE, CANVAS_SIZE);
		
		// - - - draw brush - - -
		brush.draw(g2);
		
		g2.dispose();
	}
}
