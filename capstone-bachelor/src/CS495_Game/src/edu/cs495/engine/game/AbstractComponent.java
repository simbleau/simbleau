/**
 * 
 */
package edu.cs495.engine.game;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.Renderer;

/** A component for a game object. Components add niche functions
 * to certain game objects which can make them unique.
 * 
 * @author Spencer Imbleau
 * @version February 2019
 */
public abstract class AbstractComponent {
	
	/** The parent to this component */
	protected AbstractGameObject parent;
	
	/** Initialize an abstract component
	 * 
	 * @param parent - the component's parent
	 */
	public AbstractComponent(AbstractGameObject parent) {
		this.parent = parent;
	}
	
	/** Initialization of the component
	 * 
	 * @param gameDriver : the game driver for this component
	 */
	public abstract void init(GameDriver gameDriver);
	
	/** Run an update for this component
	 * 
	 * @param gameDriver : the game driver for this component
	 */
	public abstract void update(GameDriver gameDriver);
	
	/** This is the render procedure for this game component
	 * 
	 * @param gameDriver : the game driver for this component
	 * @param renderer : the renderer for this component
	 */
	public abstract void render(GameDriver gameDriver, Renderer renderer);
}
