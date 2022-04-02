/**
 * 
 */
package edu.cs495.engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import edu.cs495.engine.developer.DeveloperLog;

/** The GameWindow holds information about the frame buffer, canvas, and
 * how we display the graphics and frame it to the user.
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class GameWindow {
	
	/** The game driver for this window */
	private GameDriver gameDriver;
	
	/** Cursor path */
	private final String DEFAULT_CURSOR_PATH = "/cursors/default.png";
	
	/** The JFrame window */
	private JFrame frame;
	
	/** The buffered image which gets drawn onto the canvas */
	private BufferedImage image;
	
	/** The canvas which we are drawing graphics on */
	private Canvas canvas;
	
	/** The buffer strategy for our game, we use double buffering */
	private BufferStrategy bufferStrategy;
	
	/** The graphics object which draws our buffered image on the canvas */
	private Graphics graphics;
	
	/** Sets up the JFrame, buffered image, buffering strategy,  canvas,
	 * and graphics object for our game
	 * @param gameDriver : the game driver
	 */ 
	public GameWindow(GameDriver gameDriver) {
		this.gameDriver = gameDriver;
		
		Dimension size = new Dimension(
				(int) (gameDriver.getGameWidth() * gameDriver.getScale()), 
				(int) (gameDriver.getGameHeight() * gameDriver.getScale()));
		
		image = new BufferedImage(
				gameDriver.getGameWidth(), 
				gameDriver.getGameHeight(), 
				BufferedImage.TYPE_INT_RGB);
		
		canvas = new Canvas();
		canvas.setMaximumSize(size);
		canvas.setMinimumSize(size);
		canvas.setPreferredSize(size);

		frame = new JFrame("Loading...");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null); //Starts window in center screen
		frame.setResizable(false);
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent componentEvent) {
				int gameWidth = (int) Math.floor((float) canvas.getHeight() / gameDriver.getScale());
				int gameHeight = (int) Math.floor((float) canvas.getWidth() / gameDriver.getScale());
				DeveloperLog.printLog("Canvas resize: " + frame.getWidth() + "x" + frame.getHeight()
				+ ", Game resize: " + gameWidth + "x" + gameHeight);
				gameDriver.setGameHeight(gameWidth);
				gameDriver.setGameWidth(gameHeight);
				updateScale();
			}
		});
		
		
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
				Toolkit.getDefaultToolkit().createImage(DEFAULT_CURSOR_PATH), 
						new Point(8, 8), "default"); 
		frame.setCursor(cursor);
		
		canvas.createBufferStrategy(2); //2 = Double Buffering
		bufferStrategy = canvas.getBufferStrategy();
		graphics = bufferStrategy.getDrawGraphics();
		
		frame.setVisible(true);	
	}
	
	/** Updates the canvas graphics with the buffered image  */
	public void update() {
		graphics = bufferStrategy.getDrawGraphics();
		
		graphics.drawImage(
				image, //Draw
				0, 0, //From
				canvas.getWidth(),
				canvas.getHeight(),
				null); //null = no image observer
		
		bufferStrategy.show();
	}
	
	/** Update the scale of the game and position the game window in the center
	 * of the screen */
	public void updateScale() {
		image = new BufferedImage(
				gameDriver.getGameWidth(), 
				gameDriver.getGameHeight(), 
				BufferedImage.TYPE_INT_RGB);
		
		Dimension size = new Dimension(
				(int) (gameDriver.getGameWidth() * gameDriver.getScale()), 
				(int) (gameDriver.getGameHeight() * gameDriver.getScale()));
		
		
		canvas.setPreferredSize(size);
		
		frame.pack();
	}
	
	/** Sets the title of the JFrame
	 * 
	 * @param frameTitle - the new title 
	 */
	public void setJFrameTitle(String frameTitle) {
		frame.setTitle(frameTitle);
	}
	
	/** Add a window listener to the JFrame - Very helpful for
	 * calling events before frame destruction such as logout.
	 * 
	 * @param listener - a window listener to add
	 */
	public void addWindowListener(WindowListener listener) {
		this.frame.addWindowListener(listener);
	}
	
	/** Remove a window listener given a reference to the item
	 * 
	 * @param listener - a refernece to a listener to remove
	 */
	public void removeWindowListener(WindowListener listener) {
		this.frame.removeWindowListener(listener);
	}
	
	/** Return all window listeners
	 * 
	 * @return an array of the frame window listeners
	 */
	public WindowListener[] getWindowListeners() {
		return this.frame.getWindowListeners();
	}
	
	
	/** Return the bufferedImage
	 * 
	 * @return the buffered image 
	 */
	public BufferedImage getImage() {
		return image;
	}

	
	/** Return the game canvas 
	 * 
	 * @return the canvas 
	 */
	public Canvas getCanvas() {
		return canvas;
	}

	/** Close the window */
	public void close() {
		this.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
}
