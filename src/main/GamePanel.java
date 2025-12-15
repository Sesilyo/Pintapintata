package main;

import java.awt.Color;
import java.util.Random;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// === OWN CLASSES ======================================
import entity.Brush;

public class GamePanel extends JPanel implements Runnable
{
	// === SCREEN SETTINGS ==============================
	// - - - SCREEN SIZE - - -
	public final static int SCRN_WIDTH  = 1000;
	public final static int SCRN_HEIGHT = 700;
	// - - - CANVAS SETTINGS - - -
	public final static int CANVAS_WIDTH  = 700;
	public final static int CANVAS_HEIGHT = 620;
	// - - - MARGINS (for canvas from panel edge) - - -
	public final static int MARGIN_LEFT  = 288;
	public final static int MARGIN_RIGHT = 12;
	// - - - PROGRESS BAR AREA HEIGHT - - -
	public final static int PROGRESSBAR_HEIGHT = 30;
	public final static int PROGRESSBAR_TOP_MARGIN = 10;
	// - - - CANVAS POSITION - - -
	// - - - position for canvas on the x-axis
	public final static int CANVAS_X = MARGIN_LEFT;
	// - - - position for canvas on the y-axis
	public final static int CANVAS_Y = PROGRESSBAR_TOP_MARGIN + PROGRESSBAR_HEIGHT + 10;
	// - - - ONE BILLION - - -
	public final static double ONE_BILLION = 1_000_000_000.0;
	
	
	// === for COLOR RANDOMNESS =========================
	private final Color[] colorPool = {
			Color.RED,
			Color.BLUE,
			Color.MAGENTA,
			Color.ORANGE,
			Color.CYAN,
			new Color(128, 0, 128) // Purple
	};
	
	private Random random = new Random();
	
	// === for LOGO =====================================
	private java.awt.Image gameLogo;
	
	
	// === set FPS ======================================
	final int FPS = 60;
	
	
	// === for MOVEMENT =================================
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	
	
	// === for ENTITIES =================================
	Brush brush;
	
	
	// === for TILE SIZE ================================
	public static final int TILE_SIZE = 10;
	
	
	// === for TIMER LOGIC ==============================
	private long startTime;
	private double elapsedSeconds;
	private boolean timerStarted = false;
	private boolean timerStopped = false;
	
	
	// === for PAINT LOGIC ==============================
	public PaintGrid paintGrid;
	
	
	// === for GAMEOVER SCREEN ==========================
	private GameOverScreen gameOverScreen;
	
	
	// === for MASTER EXIT BUTTON =======================
	private MasterExitButton masterExitButton;
	private final int EXIT_BUTTON_SIZE = 50;
	private final int EXIT_BUTTON_X = 10;
	private final int EXIT_BUTTON_Y = SCRN_HEIGHT - EXIT_BUTTON_SIZE - 10;
	
	
	// === CONSTRUCTOR ==================================
	public GamePanel()
	{
		this.setPreferredSize(new Dimension(SCRN_WIDTH, SCRN_HEIGHT));
		this.setBackground(Color.gray);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		
		gameLogo = new javax.swing.ImageIcon(
				getClass().getResource("/brush_assets/brush.png")
		).getImage();
		
		paintGrid = new PaintGrid(
				CANVAS_X, CANVAS_Y,
				CANVAS_WIDTH, CANVAS_HEIGHT,
				TILE_SIZE
		);
		
		brush = new Brush(this, keyH);	// instantiate Brush here
		startTime = System.nanoTime();
		setRandomPaintColor();			// choose a random color from color pool		
		
		// master exit button
		masterExitButton = new MasterExitButton(EXIT_BUTTON_X, EXIT_BUTTON_Y, EXIT_BUTTON_SIZE);
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (masterExitButton.isClicked(e.getX(), e.getY())) {
					System.exit(0);
				}
			}
		});
		
		// initialize game over screen
		gameOverScreen = new GameOverScreen(SCRN_WIDTH, SCRN_HEIGHT);
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
		// handle game over inputs
		if (gameOverScreen.isVisible()) {
			if (keyH.rPressed) {
				resetGame();
				gameOverScreen.hide();
			}
			
			if (keyH.escPressed) {
				System.exit(0);
			}
			
			return;
		}
		
		
		brush.update();	// keeps brush moving with key inputs
		double progress = paintGrid.getPaintProgress();	// paint progress
		
		
		// start timer when SPACE pressed
		if (!timerStarted && keyH.spacePressed) {
			timerStarted = true;
			startTime = System.nanoTime();
		}
		
		// update timer
		if (timerStarted && !timerStopped) {
			long now = System.nanoTime();
			elapsedSeconds = (now - startTime) / ONE_BILLION;			
		}
		
		// stop timer if all grid is painted
		if (progress >= 100.00) {
			timerStopped = true;
			gameOverScreen.show(elapsedSeconds);
		}
	}
	
	// helper method to pick a random color
	private void setRandomPaintColor()
	{
		int randomIndex = random.nextInt(colorPool.length);
		Color selectedColor = colorPool[randomIndex];
		paintGrid.setPaintColor(selectedColor);
	}

	
	// helper methods for game over screen
	private void resetGame()
	{
		paintGrid = new PaintGrid(
				CANVAS_X, CANVAS_Y,
				CANVAS_WIDTH, CANVAS_HEIGHT,
				TILE_SIZE
		);
		
		brush.setDefaultValues();
		timerStarted = false;
		timerStopped = false;
		elapsedSeconds = 0;
		startTime = System.nanoTime();
	}
	
	// === DRAW | the "pencil" per se ==========================
	/**
	 * renders all visual elements of the panel, including:
	 * -	canvas area
	 * -	border
	 * -	progress bar
	 * -	current brush state
	 * @param g the Graphics context provided by Swing for rendering;
	 * 			automatically supplied during repaint operations
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
				
		// - - - draw canvas area - - -
		g2.setColor(Color.WHITE);
		// fillRect(x-axis position, y-axis position, width, height)
		g2.fillRect(CANVAS_X, CANVAS_Y, CANVAS_WIDTH, CANVAS_HEIGHT);
		
		
		// - - - draw canvas border - - -
		g2.setColor(Color.BLACK);
		g2.drawRect(CANVAS_X, CANVAS_Y, CANVAS_WIDTH, CANVAS_HEIGHT);
		
		// === PROGRESS BAR SECTION ==========================================
		// - - - draw progress bar area - - -
		g2.setColor(Color.GREEN);
		
		
		int progressBarX = MARGIN_LEFT;
		int progressBarY = PROGRESSBAR_TOP_MARGIN;
		int progressBarW = SCRN_WIDTH - (MARGIN_LEFT + MARGIN_RIGHT);
		
		
		// fillRect(left, top, width, height)
		g2.fillRect(progressBarX, progressBarY, progressBarW, PROGRESSBAR_HEIGHT);
		
		
		// dynamic progress bar
		double progress = paintGrid.getPaintProgress();
		int maxWidth = SCRN_WIDTH - CANVAS_X - MARGIN_RIGHT;
		
		
		int fillWidth = (int)(maxWidth * progress / 100.00);
		
		
		// progress bar background
		g2.setColor(Color.DARK_GRAY);
		g2.fillRect(CANVAS_X, PROGRESSBAR_TOP_MARGIN, maxWidth, PROGRESSBAR_HEIGHT);
		
		
		// fill
		g2.setColor(Color.GREEN);
		g2.fillRect(CANVAS_X, PROGRESSBAR_TOP_MARGIN, fillWidth, PROGRESSBAR_HEIGHT);
		
		
		// borders
		g2.setColor(Color.BLACK);
		g2.drawRect(CANVAS_X, PROGRESSBAR_TOP_MARGIN, maxWidth, PROGRESSBAR_HEIGHT);
		// === PROGRESS BAR SECTION ================================================
		
		
		// logo & title
		int logoSize = 24;
		int titleX 	 = 20;
		int titleY 	 = 22;
		
		g2.drawImage(gameLogo, titleX, titleY - logoSize + 4,
				logoSize, logoSize, null
		);
		
		
		g2.setFont(FontManager.PIXEL_FONT.deriveFont(18f));
		g2.setColor(Color.WHITE);
		// parameters of drawString("text", x-cord start point, y-cord start point)
		g2.drawString("Pintapintata", titleX + logoSize + 8, titleY);
		
		
		// - - - draw clock - - -
		g2.setFont(FontManager.PIXEL_FONT.deriveFont(20f));
		g2.drawString("Time: " + String.format("%.1f s", elapsedSeconds),
					  20, 70
		);
		
		
		// - - - draw numerical progress tracker - - -
		g2.setFont(FontManager.PIXEL_FONT.deriveFont(15f));
		String progressText = String.format("Progress: %.1f%%", progress);
		g2.drawString(progressText, CANVAS_X + 250, PROGRESSBAR_HEIGHT + 3);
		
		
		// - - - draw brush - - -
		paintGrid.draw(g2);
		brush.draw(g2);
		masterExitButton.draw(g2);
		
		gameOverScreen.draw(g2);
		
		g2.dispose();
	}
}
