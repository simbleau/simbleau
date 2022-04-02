/**
 * 
 */
package edu.cs495.game.objects.projectiles;

import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.game.AbstractPhysicalGameObject;
import edu.cs495.engine.game.satcollision.CollisionEvent;
import edu.cs495.game.objects.player.Player;

/**
 * @author Spencer
 *
 */
public abstract class AbstractProjectile extends AbstractPhysicalGameObject {
	
	private Player sender;

	/**
	 * @param tag
	 * @param posX
	 * @param posY
	 * @param width
	 * @param height
	 */
	public AbstractProjectile(String tag, float posX, float posY, int width, int height, int depth, Player sender) {
		super(tag, posX, posY, width, height, depth);
		this.sender = sender;
	}

	public Player getSender() {
		return this.sender;
	}
	

	/** Handle an abstract blast physics */
	@Override
	public void handleCollision(CollisionEvent collisionEvent) {
		if (collisionEvent.getForce() != sender) {
			DeveloperLog.print("Projectile collision on " +this.getClass().getSimpleName() + " from " + collisionEvent.getForce().getClass().getSimpleName());
			kill();
		}
	}
	

}
