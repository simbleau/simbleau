/**
 * 
 */
package edu.cs495.engine.gfx.obj;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import edu.cs495.engine.developer.DeveloperLog;

/** A sprite sheet object
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class SpriteSheet implements Renderable {
	
	/** The width of a tile */
	protected int tileWidth;
	/** The height of a tile */
	protected int tileHeight;
	
	/** The amount of rows in the sprite sheet */
	protected int rowSize;
	/** The amount of columns in the sprite sheet */
	protected int columnSize;
	
	/** The width of the sprite sheet */
	protected int sheetWidth;
	/** The height of the sprite sheet */
	protected int sheetHeight;
	
	/** The untouched ("original") sprite sheet pixel data */
	protected int[] clampedPixels;
	/** The pixels of the sprites */
	protected int[][] sprites;

	/** Initializes a sprite sheet
	 * 
	 * @param path - the path of the sprite sheet
	 * @param tileWidth - the width of a tile
	 * @param tileHeight - the height of a tile
	 */
	public SpriteSheet(String path, int tileWidth, int tileHeight) {
		BufferedImage image = null;
		
		//Read sprite sheet file as a buffered image
		try {
			DeveloperLog.printLog("Loading " + path + "...");
			image = ImageIO.read(Image.class.getResourceAsStream(path));
		} catch (IOException e) {
			DeveloperLog.printLog(e.getStackTrace().toString());
		}
		
		//Set attributes
		this.sheetWidth = image.getWidth();
		this.sheetHeight = image.getHeight();
		
		// Load the sprite's pixel data
		this.clampedPixels = image.getRGB(
				0, //StartX
				0, //StartY
				this.sheetWidth, //Width
				this.sheetHeight, //Height
				null, //RGB Array
				0, //Offset
				this.sheetWidth); //Scan Size	
	
		/* Infer row size and column size (this may give weird results if the-
		 * image file received isn't a correctly-formatted spritesheet 
		 * (Literally only b2b sprite images with no border in rows and columns)
		 */
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.rowSize = (sheetWidth / tileWidth);
		this.columnSize = (sheetHeight / tileHeight);
		//Initialize buffer for our sprite data
		this.sprites = new int[rowSize * columnSize][tileWidth * tileHeight];
		
		//Transfer all sprite's pixel data into individual sprite buffers
		//i.e. sprites[i] = pixel data for that sprite.
		for (int tileY = 0; tileY < columnSize; tileY++) {
			int tileOffset = tileY * rowSize;
			for (int tileX = 0; tileX < rowSize; tileX++) {
				
				//Copy the pixels of a single sprite
				int xRowStart, yRowSize, yTileWidth;
				for (int y = 0; y < tileHeight; y++) {
					yTileWidth = y * tileWidth;
					xRowStart = tileX * tileWidth;
					yRowSize = (y + tileY * tileHeight) * sheetWidth;
					for (int x = 0; x < tileWidth; x++) {
						sprites[tileX + tileOffset][x + yTileWidth] =
								clampedPixels[(xRowStart + x) + yRowSize];	
					}
				} //End copying a sprite
				
			} 
		}//End copying sprites
		
		image.flush();
	}

	/** Initializes a sprite sheet given pixel data
	 * 
	 * @param pixels : the pixel data of the sprite sheet
	 * @param sheetWidth : the width of the sprite sheet
	 * @param sheetHeight : the height of the sprite sheet
	 * @param tileWidth : the width of a tile
	 * @param tileHeight : the height of a tile
	 */
	public SpriteSheet(int[] pixels, int sheetWidth, int sheetHeight, int tileWidth, int tileHeight) {
		// Load the pixel data
		this.clampedPixels = pixels.clone();
		
		//Set attributes
		this.sheetWidth = sheetWidth;
		this.sheetHeight = sheetHeight;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		/* Infer row size and column size (this may give weird results if the-
		 * image file received isn't a correctly-formatted spritesheet 
		 * (Literally only b2b sprite images with no border in rows and columns)
		 */
		this.rowSize = (sheetWidth / tileWidth);
		this.columnSize = (sheetHeight / tileHeight);
		
		//Initialize buffer for our sprite data
		this.sprites = new int[rowSize * columnSize][tileWidth * tileHeight];
		
		//Transfer all sprite's pixel data into individual sprite buffers
		//i.e. sprites[i] = pixel data for that sprite.
		for (int tileY = 0; tileY < columnSize; tileY++) {
			int tileOffset = tileY * rowSize;
			for (int tileX = 0; tileX < rowSize; tileX++) {
				
				//Copy the pixels of a single sprite
				int xRowStart, yRowSize, yTileWidth;
				for (int y = 0; y < tileHeight; y++) {
					yTileWidth = y * tileWidth;
					xRowStart = tileX * tileWidth;
					yRowSize = (y + tileY * tileHeight) * sheetWidth;
					for (int x = 0; x < tileWidth; x++) {
						sprites[tileX + tileOffset][x + yTileWidth] =
								clampedPixels[(xRowStart + x) + yRowSize];	
					}
				} //End copying a sprite
				
			} 
		}//End copying sprites
	}

	
	
	/** Returns the pixel data of a sprite from a given tile
	 * 
	 * @param tileX - the index of the sprite's X-position
	 * @param tileY - the index of the sprite's Y-position
	 * @return the pixel data of the sprite
	 */
	public int[] get(int tileX, int tileY) {
		return sprites[tileX + (tileY * rowSize)];
	}

	
	/** Return the width of a single tile
	 * 
	 * @return the tile width 
	 */
	public int getTileWidth() {
		return tileWidth;
	}

	/** Return the height of a single tile
	 * 
	 * @return the tile height 
	 */
	public int getTileHeight() {
		return tileHeight;
	}


	/** Return the width of the entire sprite sheet image
	 * 
	 *  @return the width of the sprite sheet
	 */
	public int getSheetWidth() {
		return sheetWidth;
	}


	/** Return the height of the entire sprite sheet image
	 * 
	 *  @return the height of the sprite sheet
	 */
	public int getSheetHeight() {
		return sheetHeight;
	}

	/** Return the pixel data for the entire sprite sheet image
	 * 
	 * @return the pixel data for the entire sprite sheet image
	 */
	public int[] getPixels() {
		return clampedPixels;
	}

	
}
