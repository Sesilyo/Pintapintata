package main;

import java.awt.Font;

public class FontManager
{
	public static Font PIXEL_FONT;
	
	// IIB
	static {
		try {
			PIXEL_FONT = Font.createFont(
					Font.TRUETYPE_FONT,
					FontManager.class.getResourceAsStream("/fonts/PressStart2P-Regular.ttf")
			);
		} catch (Exception e) {
			e.printStackTrace();
			PIXEL_FONT = new Font("Monospaced", Font.BOLD, 18);
		}
	}
}
