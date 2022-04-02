package edu.cs495.engine.game.satcollision;

import edu.cs495.engine.game.AbstractPhysicalGameObject;

/** This models the result of a collision and provides useful information to the invoked physical object
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class CollisionEvent {
	/** A collision event used to indicate there was no collision */
	public static final CollisionEvent NO_COLLISION = new CollisionEvent(null, 0, 0);
	
	/** The force which acted in the collision */
	protected AbstractPhysicalGameObject force;
	
	/** The angle theta (in radians) which the force strikes a collision */
	protected double pulseTheta;
	
	/** The magnitude of the force's pulse which created the collision */
	protected double pulseMagnitude;
	
	
	/** Create a collision event package for data sending
	 * 
	 * @param force - the object which caused the collision
	 * @param theta - the angle (in radians) of direction of the force
	 * @param magnitude - the power of the force
	 */
	public CollisionEvent(AbstractPhysicalGameObject force, double theta, double magnitude) {
		this.force = force;
		this.pulseTheta = theta;
		this.pulseMagnitude = magnitude;
	}

	/** Get the force which caused the collision
	 * 
	 * @return the physical object which invoked collision
	 */
	public AbstractPhysicalGameObject getForce() {
		return force;
	}

	/** Get the direction of collision
	 * 
	 * @return an angle theta in radians which corresponds to the direction of collision
	 */
	public double getPulseTheta() {
		return pulseTheta;
	}


	/** Return the magnitude of the collision
	 * 
	 * @return The magnitude of the collision
	 */
	public double getPulseMagnitude() {
		return pulseMagnitude;
	}
	
	
	
	
	
	
}
