/**
 * 
 */
package edu.cs495.engine;
import java.util.Random;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.game.AbstractGame;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.game.AbstractLevel;
import edu.cs495.engine.gfx.Renderer;

/** The GameDriver runs the game and handles game loop operations
 * 
 * @version March 2019
 * @author Spencer
 */
public class GameDriver implements Runnable {
	
	//CONSTANTS
	/** The amount of game updates per second */
	private static final int UPDATES_PER_SECOND = 60;
	/** The amount of nano seconds in one second */
	public static final long NANO_SECOND = 1000000000l;	
	/** The delta time, in seconds, for one game update */
	public static final float UPDATE_DT = (1f / UPDATES_PER_SECOND);
	
	//Non-Constants

	/** A universal RNG accessor */
	private Random RNG;
	
	/** Thread of the game */
	private Thread thread;	
	
	/** The game window */
	private GameWindow window;
	
	/** The game class */
	private AbstractGame game;

	/** The renderer */
	private Renderer renderer;
	
	/** Returns true or false if the game is running */
	private boolean isRunning = false;

	/** The frames per second at any given moment */
	private int fps;
	/** Determines if the frame rate will be locked to the updates per second */
	private boolean boundFramerate = false;
	
	/** The width of our game */
	private int gameWidth = 400;
	/** The height of our game */
	private int gameHeight = 225;
	/** The scale of our game */
	private float scale = 3f;
	
	/** Constructor for game driver 
	 * 
	 * @param timeSeed - the nanosecond time when the game starts for RNG seeding
	 * @param game - The game to run 
	 */
	public GameDriver(AbstractGame game) {
		this.game = game;
		this.fps = 0;
		this.RNG = new Random(game.getTime()); //Seed the random with the gametime
	}
	
	/** Starts the game on a new thread */
	public void start() {
		//Create window, renderer, and input
		window = new GameWindow(this);
		renderer = new Renderer(this);
		
		//Init game
		game.init(this);
		
		//Start the game loop on a new thread
		thread = new Thread(this);
		thread.run();
	}
	
	/** Interrupts the game thread */
	public void stop() {
		if (thread != null) {
			thread.interrupt();
		}
		isRunning = false;
	}
	
	/** Runs the game loop */
	public void run() {
		this.isRunning = true;
		
		boolean render = false;
		
		long currentTime = 0l;
		long lastTime = System.nanoTime();
		long deltaTime = 0l;
		long unprocessedTime = 0l;
		
		long frameTime = 0l;
		int frames = 0;
		this.fps = 0;
		
		long updateDT = (NANO_SECOND / UPDATES_PER_SECOND);
		
		while (this.isRunning) {
			//Get delta time since last update
			currentTime = System.nanoTime();
			deltaTime = currentTime - lastTime;
			lastTime = currentTime;
			
			//Accessor for anyone to get the gametime
			this.game.incrementTime(deltaTime);
			//Keep track of game tick timing
			unprocessedTime += deltaTime;
			//Keep track of our FPS time
			frameTime += deltaTime;
			
			while(unprocessedTime >= updateDT) {
				unprocessedTime -= updateDT;
				render = true;
				
				//Update the game
				game.update(this);
				game.getCamera().update(this);
				
				
				if(frameTime >= NANO_SECOND) {
					frameTime = 0l; //Reset frame time
					fps = frames; //Record FPS
					frames = 0; //Reset frames
				}
			} //end while (unprocessedTime >= UPDATE_DT)
			
			if (render) {
				renderer.clear(); //Clear the screen
				game.render(this, renderer); //Receive Render Requests

				renderer.process(game.getCamera()); //Handles sorting of rendering procedures
				
				window.update(); //Update our buffered image on the canvas
				frames++; //Update Frame Count
				if (boundFramerate) {
					render = false;
				}
			} else {
				try {
					//Try to save CPU usage
					Thread.sleep(1);
				} catch (InterruptedException e) {
					DeveloperLog.errLog(e.getStackTrace().toString());
				}
			} //end if (render)
		} //end while(this.isRunning)
		
	}
	

	/** Returns the fps of the game
	 * 
	 *  @return the frames per second of the game
	 */
	public int getFPS() {
		return this.fps;
	}
	
	
	//======= Getters & Setters Below
	
	/** Returns the random number generator for this game
	 * 
	 * @return the RNG for this game
	 */
	public Random getRNG() {
		return this.RNG;
	}
	
	/** Returns the renderer for the game engine
	 * 
	 *  @return the renderer of the game engine
	 */
	public Renderer getRenderer() {
		return renderer;
	}

	/** Returns the game object 
	 * 
	 * @return the game object 
	 */
	public AbstractGame getGame() {
		return game;
	}
	
	/** Returns the current level of the game
	 * 
	 * @return current level of the game
	 */
	public AbstractLevel getLevel() {
		return game.getLevel();
	}

	/** Return the GameWindow object 
	 * 
	 * @return the game window object tied to the game 
	 */
	public GameWindow getWindow() {
		return window;
	}
	
	/** Return the game height
	 * 
	 * @return the height of the game 
	 */
	public int getGameHeight() {
		return gameHeight;
	}

	/** Set the height of the game 
	 * 
	 * @param gameHeight - a height to set the game 
	 */
	public void setGameHeight(int gameHeight) {
		this.gameHeight = gameHeight;
	}

	/** Return the game width
	 * 
	 * @return the width of our game 
	 */
	public int getGameWidth() {
		return gameWidth;
	}

	/** Set the game with
	 * 
	 * @param gameWidth - a width to set the game 
	 */
	public void setGameWidth(int gameWidth) {
		this.gameWidth = gameWidth;
	}

	/** Return the scale of the game
	 * 
	 * @return the scale which is multiplied to the game dimensions (WxH) 
	 */
	public float getScale() {
		return scale;
	}

	/** Set the scale of the game 
	 * 
	 * @param scale - a new scale to multiply our game dimensions
	 */
	public void setScale(float scale) {
			this.scale = scale;
	}
	
	/** Return whether the frame rate is bound
	 * 
	 * @return True if the frame rate is bound to update cap, false otherwise 
	 */
	public boolean isFramesBound() {
		return boundFramerate;
	}
	
	/** Toggle frame binding to the update count */
	public void toggleFrameBind() {
		boundFramerate = !boundFramerate;
	}
	
	/** Syntactic sugar for referencing an object
	 * Equivalent to game.getLevel().getObject(tag)
	 * 
	 * @param tag - the tag of the object
	 * @return an object if it exists with the given unique tag 
	 */
	public AbstractGameObject getObject(String tag) {
		return game.getLevel().getObject(tag);
	}

	/** Syntactic sugar for getGame().getTime()
	 * 
	 * @return the game time
	 */
	public long getGameTime() {
		return game.getTime();
	}
	
}
