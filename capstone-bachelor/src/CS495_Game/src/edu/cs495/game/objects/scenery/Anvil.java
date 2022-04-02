/**
 * 
 */
package edu.cs495.game.objects.scenery;


import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.input.Input;
import edu.cs495.game.Game;
import edu.cs495.game.objects.player.LocalPlayer;

/** An anvil object is a scenery piece which indicates players 
 * that they can can craft items here
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class Anvil extends AbstractHotspot{
	
	/** Anvil hotspot image */
	private static final String HOTSPOT_PATH = "/map/common/anvil_hotspot.png";
	
	/** A visual que to tell the player building is possible */
	private static final String HOTSPOT_ICON_PATH = "/player/ui/build_que.png";
	
	/** The distance above the hotspot which the icon hovers */
	private static final int HOTSPOT_ICON_HOVER = 40;
	
	/** The color that the hotspot glows */
	private static final int HOTSPOT_GLOW_RGB = 0x25c6c4;
	
	private static final String ANVIL_PATH = "/map/common/anvil.png";
	
	/** The image of the anvil */
	private Image anvilImage;
	

	/** Initialize an anvil
	 * 
	 * @param posX - the x position for the anvil
	 * @param posY - the y position for the anvil
	 */
	public Anvil(Input localInput, float posX, float posY) {
		super("Anvil", HOTSPOT_PATH, HOTSPOT_ICON_PATH, HOTSPOT_ICON_HOVER, HOTSPOT_GLOW_RGB, posX, posY);
	}
	
	
	/** Initialize the fence */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		
		anvilImage = new Image(ANVIL_PATH);
		
		this.width = anvilImage.getWidth();
		this.height = anvilImage.getHeight();
		this.offX = (int) posX - anvilImage.getHalfWidth();
		this.offY = (int) posY - anvilImage.getHeight();
	}

	/** Update the anvil */
	@Override
	public void update(GameDriver gameDriver) {
		super.update(gameDriver);
	}



	/** Renders the anvil */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		super.render(gameDriver, renderer);
		renderer.draw(anvilImage, (int) offX, (int) offY);
	}


	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.scenery.AbstractHotspot#onTrigger(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void onTrigger(GameDriver gameDriver) {
		LocalPlayer localPlayer = ((Game) gameDriver.getGame()).getPlayer();
		if (localPlayer.getMenuRibbon().BUILD_MENU.isOpen()) {
			localPlayer.getMenuRibbon().BUILD_MENU.close();
		} else {
			localPlayer.getMenuRibbon().BUILD_MENU.open();
		}
	}


	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.scenery.AbstractHotspot#onZoneEnter(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void onZoneEnter(GameDriver gameDriver) {
		LocalPlayer localPlayer = ((Game) gameDriver.getGame()).getPlayer();
		localPlayer.getMenuRibbon().BUILD_MENU.setVisible(true);
	}


	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.scenery.AbstractHotspot#onZoneExit(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void onZoneExit(GameDriver gameDriver) {
		LocalPlayer localPlayer = ((Game) gameDriver.getGame()).getPlayer();
		localPlayer.getMenuRibbon().BUILD_MENU.setVisible(false);
	}


	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.scenery.AbstractHotspot#onProximityEnter(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void onProximityEnter(GameDriver gameDriver) {
		LocalPlayer localPlayer = ((Game) gameDriver.getGame()).getPlayer();
		localPlayer.getMenuRibbon().BUILD_MENU.enable();
	}


	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.scenery.AbstractHotspot#onProximityExit(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void onProximityExit(GameDriver gameDriver) {
		LocalPlayer localPlayer = ((Game) gameDriver.getGame()).getPlayer();
		localPlayer.getMenuRibbon().BUILD_MENU.disable();
		
	}
}
