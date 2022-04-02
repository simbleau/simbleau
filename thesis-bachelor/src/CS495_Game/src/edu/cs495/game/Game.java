/**
 * 
 */
package edu.cs495.game;

import java.awt.event.WindowListener;
import java.net.UnknownHostException;
import java.util.Random;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.game.AbstractGame;
import edu.cs495.engine.game.cameras.TargetCamera;
import edu.cs495.engine.game.cameras.TargetCamera.TYPE;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.input.MouseKeyboard;
import edu.cs495.game.levels.Estate;
import edu.cs495.game.objects.player.LocalPlayer;
import edu.cs495.game.objects.player.PlayerConfig;
import edu.cs495.game.objects.player.PlayerPrivileges;

/** The game class itself
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class Game extends AbstractGame {
	
	/** A reference to the game driver */
	private GameDriver gameDriver;
	
	/** A reference to the user! */
	private LocalPlayer me;
	
	/** The window listener for our JFrame */
	private WindowListener windowListener;
	
	/** Initializes the game */
	public Game(Long startTime) {
		this.time = startTime;
		setLevel(new Estate());
		this.gameDriver = null;
		this.windowListener = new GameWindowListener(this);
	}
	
	/** Initialize the game */
	@Override
	public void init(GameDriver gameDriver) {
		this.gameDriver = gameDriver;
		
		//Set window listener to log out when we close the JFrame
		gameDriver.getWindow().addWindowListener(windowListener);
		
		//Add player to game retrieved network player account data
		//For now, it's hardcoded.
//		PlayerConfig spencerConfig = new PlayerConfig(
//				"Spencer", //Username
//				0xf4c2a6, //Skin color
//				0x5bb2da, //Shirt Color
//				0xbc9c5e, //Pants color
//				0x162139, //Eye color
//				60f, //Starting health
//				PlayerPrivilege.ADMIN); //Privilege level
		me = new LocalPlayer(
				new MouseKeyboard(gameDriver),
				generateRandomPlayerConfig(gameDriver.getRNG()), //Configuration
				getLevel().getSpawnX(), //Starting Position X
				getLevel().getSpawnY()); //Starting Position Y
		
		//Set camera to target ourselves
		//'(Me)' is the tag of our player
		setCamera(new TargetCamera(me.getTag(), TYPE.EXPONENTIAL));
		//Add our player to the game level
		level.addGameObject(me); 
		
		//Initialize the game
		level.init(gameDriver);
	}
	
	/** Generate a random player configuation.
	 * 
	 * @param rng - a {@link Random} used for randomness
	 * @return a new player configuation
	 */
	private PlayerConfig generateRandomPlayerConfig(Random rng) {
		int skinLightness = rng.nextInt(50) + 50;
		int skinRGB = 
				((int) ((skinLightness / 100f) * 0xff)) << 16 
				| ((int) (((skinLightness - 15) / 100f) * 0xff)) << 8 
				| ((int) (((skinLightness - 30) / 100f) * 0xff));
		int shirtRGB = 
				(rng.nextInt(0xff) << 16) 
				| (rng.nextInt(0xff) << 8) 
				| rng.nextInt(0xff);
		int pantsRGB = 
				(rng.nextInt(0xff) << 16) 
				| (rng.nextInt(0xff) << 8) 
				| rng.nextInt(0xff);
		int eyeRGB;
		if (skinLightness > 85) {
			eyeRGB = 0x1621a9; //Blue
		} else if (skinLightness > 75) {
			eyeRGB = 0x409721; //green
		} else {
			eyeRGB = 0x654200; //Brown
		}
		
		PlayerConfig config = new PlayerConfig(
				"Player" + rng.nextInt(10000), //Username
				skinRGB, //Skin color
				shirtRGB, //Shirt Color
				pantsRGB, //Pants color
				eyeRGB, //Eye color
				100f, //Starting health
				PlayerPrivileges.ADMIN); //Privilege level
		
		return config;
	}

	/** Updates the game */
	@Override
	public void update(GameDriver gameDriver) {
		super.update(gameDriver);
		me.getInput().update();
	}

	/** Renders the game */  
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {		
		super.render(gameDriver, renderer);
	}
	
	/** Return the driver for this game
	 * 
	 * @return the game driver
	 */
	public GameDriver getDriver() {
		return this.gameDriver;
	}
	
	/** Returns the player
	 * 
	 * @return our player
	 */
	public LocalPlayer getPlayer() {
		return me;
	}


}
