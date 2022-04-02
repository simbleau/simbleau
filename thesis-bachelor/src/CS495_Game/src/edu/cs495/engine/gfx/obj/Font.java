/**
 * 
 */
package edu.cs495.engine.gfx.obj;


/** A Font Object
 * @author Spencer
 *
 */
public class Font implements Renderable {
	
	/** The standard font.
	 * Big reveal: This is actually a copy of Comic Sans at 14px (lol) */
	public static final Font STANDARD = new Font("/fonts/big_bold.png");
	/** The standard small font */
	public static final Font SMALL = new Font("/fonts/small.png");
	/** The chat font */
	public static final Font CHAT = new Font("/fonts/chat.png");
	
	/** A pixel which denotes the start of a character in a font image */
	private static final int BLUE_PIXEL = 0xff0000ff;
	/** A pixel which denotes the end of a character in a font image */
	private static final int YELLOW_PIXEL = 0xffffff00;
	
	/** The unicode offset to start mapping our character array. Since space
	 * is the first alphanumeric, and space = 32, we subtract 32 from characters
	 * to map the characters correctly to a pixel array.
	 * 
	 * @see https://unicode-table.com/en/
	 */
	public static final int UNICODE_OFFSET = -32;
	
	/** The maximum amount of characters supported in our font */
	private static final int MAX_CHARS = 95;
	/** How many characters this font has */
	private final int chars;
	
	/** The font image file */
	public final Image fontImage;
	/** Contains the amount of pixels from the origin (0) that the 
	 * sprite exists for every character in our font */
	public final int[] offsets;
	/** Contains the width of every character in our font */
	public final int[] widths;
	/** The size of the font - px height */
	public final int SIZE;
	
	/** Creates a font object given a path and amount of characters in that file
	 * 
	 * @param path - the path of the font image
	 * @param chars - the amount of characters in the font image
	 */
	private Font(String path, final int chars) {
		fontImage = new Image(path);
		
		this.SIZE = fontImage.height;
		offsets = new int[chars];
		widths = new int[chars];
		
		int unicode = 0;
		
		for (int i = 0; i < fontImage.getWidth(); i++) {
			//Search the top row of pixels in the font image for blue and yellow
			//pixels to denote the start/ends of characters.
			
			//Get offsets
			if (fontImage.getPixels()[i] == BLUE_PIXEL) {
				offsets[unicode] = i;
			}
			
			//Get widths
			if (fontImage.getPixels()[i] == YELLOW_PIXEL) {
				widths[unicode] = i - offsets[unicode];
				unicode++;
			}
			
			//Check bounds
			if (unicode >= chars) {
				break;
			}
		}
		
		this.chars = unicode;
	}
	
	/** Constructs a font by assuming {@link #MAX_CHARS} to be the amount
	 * of characters in the font image. 
	 * 
	 * @param path - the path of the font image
	 */
	private Font(String path) {
		this(path, MAX_CHARS);
	}

	/** Creates and returns an image of the text provided
	 * 
	 * @param text - the text to rasterize
	 * @param argb - an ARGB formatted color for the text color
	 * @return A new image of the string
	 */
	public Image getStringImage(String text, int argb) {
		// Calculate the width this string will use
		int imgWidth = 0;
		int imgHeight = fontImage.getHeight();
		for (int i = 0; i < text.length(); i++) { 
			int charPosition = text.codePointAt(i) + Font.UNICODE_OFFSET;
			
			//Build the image width
			if (charPosition < 0 || charPosition > chars) {
				// If not a character in the font image, draw a box of relative
				// size instead
				imgWidth += (imgHeight / 2);
			} else { 
				// If the character exists, append its width
				imgWidth += widths[charPosition];
			}
		}

		// Draw the string
		int offset = 0;
		int[] pixelData = new int[imgWidth * imgHeight];
		for (int i = 0; i < text.length(); i++) {
			// Get character position
			int charPosition = text.codePointAt(i) + Font.UNICODE_OFFSET;

			// Check if character has a mapping
			if (charPosition < 0 || charPosition > chars) {
				// Draw a box, since we don't have a character map for this.
				// Vertical Bars
				int boxWidth = (imgHeight / 2);
				for (int y = 0; y < imgHeight; y++) {
					int yStart = y * imgWidth;
					pixelData[yStart + offset] = argb;
					pixelData[yStart + offset + boxWidth] = argb;
				}
				// Horizontal Bars
				int yFloorStart = (imgHeight - 1) * imgWidth;
				for (int x = 0; x <= boxWidth; x++) {
					pixelData[offset + x] = argb;
					pixelData[yFloorStart + offset + x] = argb;
				}
				offset += boxWidth;
			} else {
				// Draw the character as from the font image
				for (int y = 0; y < imgHeight; y++) {
					int yFontStart = y * fontImage.width;
					int yImgStart = y * imgWidth;
					for (int x = 0; x < widths[charPosition]; x++) {
						// Only render white pixels
						if (fontImage.getPixels()[(x + offsets[charPosition]) +
						                          yFontStart] == 0xffffffff) {
							pixelData[yImgStart + offset + x] = argb;
						}
					}
				}
				offset += widths[charPosition];
			}
		}

		return new Image(imgWidth, imgHeight, pixelData);
	}


	
}
