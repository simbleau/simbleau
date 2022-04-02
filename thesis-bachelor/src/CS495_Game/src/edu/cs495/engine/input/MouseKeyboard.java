/**
 * 
 */
package edu.cs495.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;

/** The mouse and keyboard input listener interface
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class MouseKeyboard implements 
Input, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	
	/** The game driver which is needed to retrieve the canvas */
	private GameDriver gameDriver;

	/** The array of booleans which contains the states of keyboard keys */
	private boolean[] keys = new boolean[Input.NUM_KEYS];
	/** An array of booleans which contains the states of the last update's
	 * keys */
	private boolean[] keysLast = new boolean[Input.NUM_KEYS];

	/** The number of mouse buttons being used */
	private final int NUM_BUTTONS = 5;
	/** The array of booleans which contains the states of mouse buttons */
	private boolean[] buttons = new boolean[NUM_BUTTONS];
	/** The array of booleans which contains the states of last update's
	 *  mouse buttons */
	private boolean[] buttonsLast = new boolean[NUM_BUTTONS];

	/** The position X coordinate of our mouse */
	private int mouseX;
	/** The position Y coordinate of our mouse */
	private int mouseY;
	/** The scroll direction */
	private int scroll;

	/** Initializes the mouse n keyboard input	
	 * @param gameDriver The game driver to bound input to */
	public MouseKeyboard(GameDriver gameDriver) {
		this.gameDriver = gameDriver;
		
		mouseX = 0;
		mouseY = 0;
		scroll = 0;

		gameDriver.getWindow().getCanvas().addKeyListener(this);
		gameDriver.getWindow().getCanvas().addMouseListener(this);
		gameDriver.getWindow().getCanvas().addMouseMotionListener(this);
		gameDriver.getWindow().getCanvas().addMouseWheelListener(this);
		//This is required for tab to pull up the chat
		gameDriver.getWindow().getCanvas().setFocusTraversalKeysEnabled(false);
	}

	/** Update the key and mouse buffers */
	public void update() {
		scroll = 0;
	
		for(int i = 0; i < NUM_KEYS; i++) {
			keysLast[i] = keys[i];
		}
		
		for(int i = 0; i < NUM_BUTTONS; i++) {
			buttonsLast[i] = buttons[i];
		}
	}
	
	/** Return whether a key is held down
	 * 
	 * @param keyCode - the specific key's code
	 * @return true if the key is held down, false otherwise
	 */
	public boolean isKeyActive(int keyCode) {
		return keys[keyCode];
	}
	
	/** Return whether a key is being released
	 * 
	 * @param keyCode - the specific key's code
	 * @return true if the key is being released, false otherwise
	 */
	public boolean isKeyUp(int keyCode) {
		return !keys[keyCode] && keysLast[keyCode];
	}
	
	/** Return whether a key is being pressed
	 * 
	 * @param keyCode - the specific key's code
	 * @return true if the key is being pressed, false otherwise
	 */
	public boolean isKeyDown(int keyCode) {
		return keys[keyCode] && !keysLast[keyCode];
	}
	
	/** Return whether a mouse button is held down
	 * 
	 * @param keyCode - the specific key's code
	 * @return true if the mouse button is held down, false otherwise
	 */
	public boolean isButtonActive(int button) {
		return buttons[button];
	}
	
	/** Return whether a mouse button is being released
	 * 
	 * @param keyCode - the specific key's code
	 * @return true if the mouse button is being released, false otherwise
	 */
	public boolean isButtonUp(int button) {
		return !buttons[button] && buttonsLast[button];
	}
	
	/** Return whether a mouse button is being pressed
	 * 
	 * @param keyCode - the specific key's code
	 * @return true if the mouse button is being pressed, false otherwise
	 */
	public boolean isButtonDown(int button) {
		return buttons[button] && !buttonsLast[button];
	}
	
	/** Return the X coordinate of the mouse (in world coordinates)
	 * 
	 * @return the X coordinate of the mouse (in world coordinates)
	 */
	public int getPosX() {
		return mouseX / (Math.max(1, (int) gameDriver.getScale()))
				+ ((int) gameDriver.getGame().getCamera().getCamX());
	}

	/** Return the Y coordinate of the mouse (in world coordinates)
	 * 
	 * @return the Y coordinate of the mouse (in world coordinates)
	 */
	public int getPosY() {
		return mouseY / (Math.max(1, (int) gameDriver.getScale()))
				+ ((int) gameDriver.getGame().getCamera().getCamY());
	}

	/** Return the X coordinate of the mouse (in screen coordinates)
	 * 
	 * @return the X coordinate of the mouse (in screen coordinates)
	 */
	public int getRelativeX() {
		return  Math.min(gameDriver.getGameWidth(), (mouseX / (Math.max(1, (int) gameDriver.getScale()))));
	}

	/** Return the Y coordinate of the mouse (in screen coordinates)
	 * 
	 * @return the Y coordinate of the mouse (in screen coordinates)
	 */
	public int getRelativeY() {
		return Math.min(gameDriver.getGameHeight(), (mouseY / (Math.max(1, (int) gameDriver.getScale()))));
	}

	
	/** Return the direction of the scroll
	 * 
	 * @return the direction of the scroll
	 */
	public int getScroll() {
		return scroll;
	}

	
	// =============================================================
	// ========MOUSE AND KEY EVENTS BELOW===========================
	// =============================================================
	
	
	/** Update the mouse scroll value when the scroll moves */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		this.scroll = e.getWheelRotation();
		DeveloperLog.printLog("Wheel rotated: " + e.getWheelRotation());
	}

	/** Update the mouse coordinates when the mouse is dragged */
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	/** Update the mouse coordinates when the mouse is moved */
	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	/** Consume this event / Not used */
	@Override
	public void mouseClicked(MouseEvent e) {
		//Nothing to do here
		e.consume();
	}

	/** Update the mouse button boolean when the mouse is pressed */
	@Override
	public void mousePressed(MouseEvent e) {
		try {
		buttons[e.getButton()] = true;
		} catch (Exception ex) {
			//Do nothing
		}
	}

	/** Update the mouse button boolean when the mouse is released */
	@Override
	public void mouseReleased(MouseEvent e) {
		try {
			buttons[e.getButton()] = false;
			DeveloperLog.printLog("Mouse[" + e.getButton() + "] " +
			"pressed at (" + getRelativeX() + ", "  + getRelativeY() + ")");
		} catch (Exception ex) {
			//Do nothing
		}
	}

	/** Log when the user focuses the client */
	@Override
	public void mouseEntered(MouseEvent e) {
		DeveloperLog.printLog("Mouse entered screen");
	}

	/** Log when the user exits the client */
	@Override
	public void mouseExited(MouseEvent e) {
		DeveloperLog.printLog("Mouse exited screen");

	}

	/** Consume this event / Not used */
	@Override
	public void keyTyped(KeyEvent e) {
		//Nothing to do here
		e.consume();
	}

	/** Update the key pressed boolean when a key is pressed */
	@Override
	public void keyPressed(KeyEvent e) {
		try {
			keys[e.getKeyCode()] = true;
		} catch (Exception ex) {
			//Do nothing
		}
	}

	/** Update the key pressed boolean when a key is released */
	@Override
	public void keyReleased(KeyEvent e) {
		try {
			keys[e.getKeyCode()] = false;
			DeveloperLog.printLog("Key[" + e.getKeyCode() + "]" +
			"released: " + e.getKeyChar());
		} catch (Exception ex) {
			//Do nothing
		}
	}


	
	

}
