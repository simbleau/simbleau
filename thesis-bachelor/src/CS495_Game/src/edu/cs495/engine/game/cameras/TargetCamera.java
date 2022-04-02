/**
 * 
 */
package edu.cs495.engine.game.cameras;


import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.game.AbstractCamera;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.game.AbstractLevel;

/** A camera which follows a target based on a tag
 * 
 * @author Spencer Imbleau
 * @version February 2019
 */
public class TargetCamera extends AbstractCamera {
	
	/** A fixed follow speed for exponential type cameras */
	private static final int EXPONENTIAL_SPEED = 3;
	
	/** Types of following behaviors for a Target camera */
	public static enum TYPE {
		
		/** Exact will make the camera follow a tag precisely,
		 * even if it traverses out of map boundaries. This is mostly for debug. */
		EXACT, 
		
		/** Springarm will make the camera follow a tag precisely
		 * and obey the map boundaries */
		SPRINGARM, 
		
		/** Exponential will make the camera follow a tag exponentially
		 * and obey the map boundaries */
		EXPONENTIAL;
	}
	
	/** The following behavior {@link TYPE} for this camera */
	private TYPE type;
	
	/** The tag of the game object we will target */
	private String targetTag;
	
	/** The target that the camera is following (could be null) */
	private AbstractGameObject target;
	
	/** Initialize the target camera with a default behavior
	 * 
	 * @param tag - the tag of the game object the camera should target
	 */
	public TargetCamera(String tag) {
		this(tag, TYPE.EXPONENTIAL);
	}
	
	/** Initialize the target camera
	 * 
	 * @param tag - the tag of the game object the camera should target
 	 * @param type - the {@link TYPE}, i.e. targeting behavior
	 */
	public TargetCamera(String tag, TYPE type) {
		setType(type);
		this.targetTag = tag;
		this.target = null;
	}
	

	@Override
	public void update(GameDriver gameDriver) {
		if (target == null) {
			setTarget(gameDriver.getLevel(), this.targetTag);
			if (target == null) {
				return;
			}
		}

		float targetX = target.getPosX()- (gameDriver.getGameWidth() / 2); //Screen center
		
		float targetY = target.getPosY()- (gameDriver.getGameHeight() / 2);
		
		switch (type) {
		case EXACT:
			//Set position to target without clipping
			this.camX = targetX;
			this.camY = targetY;
			break;
		case EXPONENTIAL:
			//Get DeltaX and apply it
			float deltaX = GameDriver.UPDATE_DT * (camX - targetX) * EXPONENTIAL_SPEED;
			targetX = camX - deltaX;
			
			//Get DeltaY and apply it
			float deltaY = GameDriver.UPDATE_DT * (camY - targetY) * EXPONENTIAL_SPEED;
			targetY = camY - deltaY;
			
			//Do not break! we want to fall into the boundary checking of SPRINGARM
		case SPRINGARM:
			//Clipping
			//Boundary checking the +X boundary
			targetX = Math.min(targetX, gameDriver.getLevel().getWidth() - gameDriver.getGameWidth());
			//Boundary checking the -X boundary
			targetX = Math.max(targetX, 0);
			//Boundary checking the +Y boundary
			targetY = Math.min(targetY, gameDriver.getLevel().getHeight() - gameDriver.getGameHeight());
			//Boundary checking the -Y boundary
			targetY = Math.max(targetY, 0);
			
			//Set new target
			this.camX = targetX;
			this.camY = targetY;
			break;
		default:
			return;
		}
	}
	
	/** Set a new camera behavior
	 * 
	 * @param type - a new camera behavior
	 * @see TYPE
	 */
	public void setType(TYPE type) {
		DeveloperLog.printLog("Camera behavior set: " + type.toString());
		this.type = type;
	}
	
	/** Get what type of camera behavior this camera uses
	 * 
	 * @return the {@link TYPE} which controls this camera's behavior
	 */
	public TYPE getType() {
		return this.type;
	}
	
	/** Set a new tag that the camera will try to find and target
	 * 
	 * @param level - the game level searched to find the object
	 * @param tag - the tag of the object the camera will locate
	 */
	public void setTarget(AbstractLevel level, String tag) {
		target = level.getObject(targetTag);
		if (target != null) {
			DeveloperLog.printLog("Camera target set: " + tag);
		} else {
			DeveloperLog.errLog("Camera: target '" + tag + "' not found.");
		}
	}
	
}
