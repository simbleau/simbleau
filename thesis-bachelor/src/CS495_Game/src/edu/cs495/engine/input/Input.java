/**
 * 
 */
package edu.cs495.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

/** The Input interface prototypes the methods which should be used by the user
 * when requesting input information during game updates. 
 * @version 10/01/2018
 * @author Spencer Imbleau
 * @see Credit: Majoolwip for his Input class which provides concepts for this interface
 * located on Youtube : https://www.youtube.com/watch?v=bNq1UxL2cmE
 */
public interface Input extends KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	
	/** The amount of keys on a standard keyboard */
	public static final int NUM_KEYS = 256;
	
	static class KEYS{
		/** The movement key for going up is W */
		public static final int UP = KeyEvent.VK_W;
		/** The movement key for going left is A */
		public static final int LEFT = KeyEvent.VK_A;
		/** The movement key for going down is S */
		public static final int DOWN = KeyEvent.VK_S;
		/** The movement key for going right is D */
		public static final int RIGHT = KeyEvent.VK_D;
	}
	
	static class BUTTONS{
		/** The button code for a left click */
		public static final int LEFT_CLICK = 1;
		/** The button code for a wheel click */
		public static final int WHEEL_CLICK = 2;
		/** The button code for a right click */
		public static final int RIGHT_CLICK = 3;
		
		/** The primary spell mouse button is the left click */
		public static final int PRIMARY_SPELL = 1;
		/** The secondary spell mouse button is the right click */
		public static final int SECONDARY_SPELL = 3;
	}
	
	static enum ATTACKS{
		/** The enum literal for the player's primary spell */ 
		PRIMARY_SPELL,
		/** The enum literal for the player's secondary spell */ 
		SECONDARY_SPELL,
		/** The enum literal for the player's primary and secondary spell */ 
		BOTH;
		
	}
	
	static class SCROLL{
		/** The value analogous to scrolling up */ 
		public static final int UP = -1;
		/** The value analogous to not scrolling */ 
		public static final int IDLE = 0;
		/** The value analogous to scrolling down */ 
		public static final int DOWN = 1;
	}
	
	/**  Update the input */
	public void update();
	
	/** @return X position of the cursor */
	public int getPosX();
	/** @return Y position of the cursor */
	public int getPosY();
	/** @return X position of the cursor relative to the screen */
	public int getRelativeX();
	/** @return Y position of the cursor relative to the screen */
	public int getRelativeY();
	
	/** @param keyCode the code of the key to evaluate
	 *  @return true if the given key is held, false otherwise */
	public boolean isKeyActive(int keyCode);
	/** @param keyCode the code of the key to evaluate
	 *  @return true if the given key was just released, false otherwise */
	public boolean isKeyUp(int keyCode);
	/** @param keyCode the code of the key to evaluate
	 *  @return true if the given key was just pressed, false otherwise */
	public boolean isKeyDown(int keyCode);
	
	/** @param buttonCode the type of button 
	 *  @return true if the button is held, false otherwise */
	public boolean isButtonActive(int buttonCode);
	/** @param buttonCode the type of button 
	 *  @return true if the button is held, false otherwise */
	public boolean isButtonUp(int buttonCode);	
	/** @param buttonCode the type of button 
	 *  @return true if the button is held, false otherwise */
	public boolean isButtonDown(int buttonCode);
	
	/** @return Scroll direction */
	public int getScroll();


}
