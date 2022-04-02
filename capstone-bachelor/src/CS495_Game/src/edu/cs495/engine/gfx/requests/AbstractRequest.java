/**
 * 
 */
package edu.cs495.engine.gfx.requests;


/** A render request is a comparable object sent to the renderer for 
 * processing (and sorting) before drawing graphics to the screen.
 * 
 * @version February 2019
 * @see edu.cs495.engine.gfx.requests a directory of concrete requests
 * @author Spencer Imbleau
 */
public abstract class AbstractRequest implements Comparable<AbstractRequest>{
	
	/** Comparable variable used for sorting render requests */
	public final int renderOrder;
	
	/** Ensures initialization of yDepth
	 * 
	 * @param renderOrder - the comparable order this request will take 
	 */
	public AbstractRequest(int renderOrder) {
		this.renderOrder = renderOrder;
	}
	
	/** Compare this render request to another render request.
	 * This is done by comparing the renderOrder variable.
	 * 
	 * @param o - another {@link AbstractRequest} to compare to
	 */
	@Override
	public int compareTo(AbstractRequest o) {
		if (renderOrder < o.renderOrder) {
			return -1;
		} else if (renderOrder > o.renderOrder) {
			return 1;
		} else {
			return 0;
		}
	}
}

