/**
 * 
 */
package edu.cs495.engine.gfx.obj;

import java.util.ArrayList;

import edu.cs495.engine.gfx.obj.filters.AbstractFilter;

/** A {@link SpriteSheet} graphics object which will support filters
 * 
 * @version February 2019
 * @see AbstractFilter
 * @author Spencer Imbleau
 */
public class FilteredSpriteSheet extends SpriteSheet implements Renderable {

	/** Contains the filtered pixels of the sprite sheet */
	private int[][] filteredPixels;

	/** A list of {@link AbstractFilter}s for this sprite sheet */
	private ArrayList<AbstractFilter> filters = new ArrayList<>();

	/** Initializes a filtered sprite sheet with a path
	 * 
	 * @param path - path of the sprite sheet
	 * @param tileWidth - The width of a sprite
	 * @param tileHeight - The height of a sprite
	 */
	public FilteredSpriteSheet(String path, int tileWidth, int tileHeight) {
		super(path, tileWidth, tileHeight);
		this.filteredPixels = sprites.clone();
	}
	
	/** Initializes a filtered sprite sheet with pixel data
	 * 
	 * @param path - path of the sprite sheet
	 * @param tileWidth - The width of a sprite
	 * @param tileHeight - The height of a sprite
	 */
	public FilteredSpriteSheet(int[] pixels, int sheetWidth, int sheetHeight, int tileWidth, int tileHeight) {
		super(pixels, sheetWidth, sheetHeight, tileWidth, tileHeight);
		this.filteredPixels = sprites.clone();
	}

	/** Add a filter to the sprite sheet without checking for duplicates
	 * 
	 * @param filter - The filter to add
	 */
	public void addFilter(AbstractFilter filter) {
		filters.add(filter);
	}
	
	/** Clear all filters belonging to this sprite sheet */
	public void clearFilters() {
		filters.clear();
	}
	
	/** Returns the pixel data of a sprite
	 * 
	 * @param tileX - the index of the tile's x coordinate
	 * @param tileY - the index of the tile's y coordinate
	 * @return the pixel data of the desired sprite
	 */
	@Override
	public int[] get(int tileX, int tileY) {
		return filteredPixels[tileX + tileY * rowSize];
	}
	
	/** Applies all filters to a specific sprite. This does not update
	 * all of the sprites.
	 * 
	 * @param tileX - the index of the tile's x coordinate
	 * @param tileY - the index of the tile's y coordinate
	 */
	public void build(int tileX, int tileY) {
		//Define the starting position of our row in a one dimensional container
		int yRowStart = tileY * rowSize;
		//Convert X,Y sprite to an Image
		FilteredImage filteredSprite = new FilteredImage(
				tileWidth, 
				tileHeight, 
				filteredPixels[tileX + yRowStart]);
		
		//Apply all filters to that sprite
		for (int i = 0; i < filters.size(); i++) {	
			filteredSprite.bake(filters.get(i));
		}

		//Override the filtered pixels with the new filtered sprite
		filteredPixels[tileX + yRowStart] = filteredSprite.getPixels();
	}
	
	/** Applies all filters to all sprites */
	public void buildAll() {
		for (int tileY = 0; tileY < columnSize; tileY++) {
			for (int tileX = 0; tileX < rowSize; tileX++) {
				build(tileX, tileY);
			}
		}
	}
	
	/** Apply a specific filter to just one sprite
	 * 
	 * @param tileX - the sprite's x position
	 * @param tileY - the sprite's y position
	 * @param filter - the filter to apply
	 */
	public void bake(int tileX, int tileY, AbstractFilter filter) {
		//Define the starting position of our row in a one dimensional container
		int yRowStart = tileY * rowSize;
		
		//Clone the sprite into an image which we can filter on
		FilteredImage filteredSprite = new FilteredImage(
				tileWidth, 
				tileHeight, 
				filteredPixels[tileX + yRowStart]);
		filteredSprite.bake(filter);
		
		//Override the filtered pixels with the new filtered sprite
		filteredPixels[tileX + yRowStart] = filteredSprite.getPixels();
	}
	
	public void bakeAll(AbstractFilter filter) {
		for (int tileY = 0; tileY < columnSize; tileY++) {
			for (int tileX = 0; tileX < rowSize; tileX++) {
				bake(tileX, tileY, filter);
			}
		}
	}

	
	/** Helper methods resets the pixel data of one sprite to its original data
	 * 
	 * @param tileX - the index of the tile's x coordinate
	 * @param tileY - the index of the tile's y coordinate
	 */
	public void clean(int tileX, int tileY) {
		filteredPixels[tileX + tileY * rowSize] = 
				sprites[tileX + tileY * rowSize].clone();
	}
	
	/** Resets the pixel data of all sprites to their original data */
	public void cleanAll() {
		filteredPixels = sprites.clone();
	}
}
