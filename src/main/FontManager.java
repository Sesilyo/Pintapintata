package main;

import java.awt.Font;


/**
 * Manages loading and availability of custom fonts used within the application.
 */
public class FontManager
{
	public static Font PIXEL_FONT;
	
	/**
	 * Static Initializer Block - IIB.
	 * Runs once when the FontManager class.
	 */
	static {
		try {
			PIXEL_FONT = Font.createFont(
					Font.TRUETYPE_FONT,
					FontManager.class.getResourceAsStream("/fonts/PressStart2P-Regular.ttf")
			);
		} catch (Exception e) {
			e.printStackTrace();
			// fail safe if font won't load
			PIXEL_FONT = new Font("Monospaced", Font.BOLD, 18);
		}
	}
}
