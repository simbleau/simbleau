/**
 * 
 */
package edu.cs495.engine.gfx.obj;

import java.util.ArrayList;

import edu.cs495.engine.gfx.obj.filters.AbstractFilter;

/** An {@link Image} graphics object which supports filters
 * 
 * @version February 2019
 * @see AbstractFilter
 * @author Spencer Imbleau
 */
public class FilteredImage extends Image implements Renderable {
	
	/** Contains the filtered pixels of the image */
	private int[] filteredPixels;
	
	/** A list of {@link AbstractFilter}s for this image */
	private ArrayList<AbstractFilter> filters = new ArrayList<>();

	/** Creates a filtered image object with pixel data
	 * 
	 * @param width - the width of the image
	 * @param height - the height of the image
	 * @param pixels - the pixels of the image
	 */
	public FilteredImage(int width, int height, int[] pixels) {
		super(width, height, pixels);
		filteredPixels = pixels.clone();
	}

	/** Creates a filtered image object with a path to an image
	 * 
	 * @param path - the path of the image
	 */
	public FilteredImage(String path) {
		super(path);
		filteredPixels = clampedPixels.clone();
	}

	/** Add a filter to the Image without checking for duplicates
	 * 
	 * @param filter - The filter to add
	 */
	public void addFilter(AbstractFilter filter) {
		filters.add(filter);
	}
	
	/** Clear all filters belonging to this Image */
	public void clearFilters() {
		filters.clear();
	}
	
	/** Return the filtered pixels of this Image object
	 * 
	 * @return the filtered pixels
	 * @see {@link #filteredPixels}
	 */
	@Override
	public int[] getPixels() {
		return filteredPixels;
	}
	
	/** Apply one filter and update {@link #filteredPixels} 
	 * 
	 * @param filter - the filter to apply
	 */
	public void bake(AbstractFilter filter) {
		filter.setImage(this);
		filteredPixels = filter.apply();
	}
	
	/** Bake all filters and update {@link #filteredPixels} */
	public void build() {		
		for (int i = 0; i < filters.size(); i++) {
			bake(filters.get(i));
		}		
	}
	
	/** Resets the filtered pixels back to original pixels before any filters
	 * were applied*/
	public void clean() {
		filteredPixels = clampedPixels.clone();
	}
	
}
