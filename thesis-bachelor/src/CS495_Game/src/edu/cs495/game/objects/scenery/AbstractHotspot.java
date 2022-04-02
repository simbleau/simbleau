/**
 * 
 */
package edu.cs495.game.objects.scenery;

import java.awt.event.KeyEvent;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.FilteredImage;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.gfx.obj.filters.AlphaFilter;
import edu.cs495.engine.gfx.obj.filters.TotalColorFilter;
import edu.cs495.game.Game;
import edu.cs495.game.objects.player.LocalPlayer;
import edu.cs495.game.objects.player.chatbox.ChatTextbox;

/** A key hotspot is a space wherein if the player moves toward it,
 * it glows a certain color to indicate one is getting close. Within
 * an even closer proximity range, a hotkey can be used to trigger an event.
 *  
 * @author Spencer Imbleau
 * @version March 2019
 */
public abstract class AbstractHotspot extends AbstractGameObject{
	
	/** The hotkey used to trigger the anvil when in proximity */
	private static final int HOTKEY = KeyEvent.VK_B;
	
	/** The distance in which the glow effect starts to occur */
	private static final int GLOW_TRIGGER = 120;
	
	/** The distance in which the trigger can be activated */
	private static final int PROXIMITY_TRIGGER = 40;
	
	/** The amount of pixels in which the key hotspot icon hovers between */
	private static final int HOVER_MAGNITUDE = 10;
	
	/** The speed of the hovering (frequency) */
	private static final float HOVER_SPEED = 2f;
	
	/** The path to the hotspot image */
	private String hotspotPath;
	
	/** The image of the hotspot/shadow */
	private FilteredImage hotspotImage;
	
	/** Color for the hotspot */
	private TotalColorFilter hotspotColorFilter;
	
	/** Opacity for the hotspot */
	private AlphaFilter hotspotAlphaFilter;

	/** The path to the hotspot image */
	private String hotspotIconPath;
	
	/** The image of the hotspot/shadow */
	private Image hotspotIcon;
	
	/** The color which the hotspot glows */
	private int glowRGB;
	
	/** The amount in which the icon hovers over the ground */
	private int yOffset;
	
	/** Whether we are showing the icon */
	private boolean showIcon;
	
	private boolean showIconLast;
	
	/** Whether we are showing the hotspot glowed */
	private boolean showGlow;
	
	private boolean showGlowLast;
	
	
	public AbstractHotspot(String name, String hotspotPath, String hotspotIconPath, int yOffset, int glowRGB, float posX, float posY) {
		super("(" + name + ")", posX, posY, 0, 0);
		this.hotspotPath = hotspotPath;
		this.glowRGB = glowRGB;
		this.hotspotImage = null;
		this.hotspotColorFilter = null;
		this.hotspotAlphaFilter = null;
		this.hotspotIconPath = hotspotIconPath;
		this.hotspotIcon = null;
		this.yOffset = yOffset;
		this.showIcon = false;
		this.showIconLast = false;
		this.showGlow = false;
		this.showGlowLast = false;
	}

	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		this.hotspotImage = new FilteredImage(hotspotPath);
		this.hotspotIcon = new Image(hotspotIconPath);
		this.hotspotColorFilter = new TotalColorFilter(hotspotImage);
		this.hotspotAlphaFilter = new AlphaFilter(hotspotImage);
		
		hotspotImage.addFilter(hotspotAlphaFilter);
		hotspotImage.addFilter(hotspotColorFilter);
		
		hotspotColorFilter.setColor(0x00); //Black
		hotspotAlphaFilter.setAlphaPercent(50);
		
		hotspotImage.build();
	}

	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#update(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void update(GameDriver gameDriver) {		
		super.update(gameDriver);
		//Get the player
		LocalPlayer localPlayer = ((Game) gameDriver.getGame()).getPlayer();
		
		//Check if player is alive
		if (!localPlayer.isDead()) {

			//Get dx,dy, and check that we are in radius for animation
			float dx = localPlayer.getPosX() - posX;
			float dy = localPlayer.getPosY() - posY;
			double distance = Math.sqrt(dx * dx + dy * dy);
			if (distance < GLOW_TRIGGER) {
				showGlowLast = showGlow;
				showGlow = true;
				if (!showGlowLast) {
					onZoneEnter(gameDriver);
				}
				
				//Fade hotspot
				double power = 1d - (distance / GLOW_TRIGGER);
				int newOpacityPercent = Math.max(50, Math.min(80, 50 + (int) (power * 50.0)));
				int glowR = (int) (power * (glowRGB >> 16));
				int glowG = (int) (power * ((glowRGB & 0x00ff00) >> 8));
				int glowB = (int) (power * (glowRGB & 0x0000ff));
				int newColor = (glowR << 16) | (glowG << 8) | glowB;
				if (hotspotColorFilter.getColor() != newColor
						|| hotspotAlphaFilter.getAlphaPercent() != newOpacityPercent) {
					hotspotColorFilter.setColor(newColor);
					hotspotAlphaFilter.setAlphaPercent(newOpacityPercent);
					hotspotImage.clean();
					hotspotImage.build();
				} 
				//Check if we're in radius for building
				if (distance <= PROXIMITY_TRIGGER) {
					showIconLast = showIcon;
					showIcon = true;
					if (!showIconLast) {
						onProximityEnter(gameDriver);
					}
					
					if (!localPlayer.getConsole().isTyping() && localPlayer.getInput().isKeyDown(HOTKEY)) {
						onTrigger(gameDriver);
					} 

					localPlayer.addOptionText("Press " + ChatTextbox.getChar(HOTKEY, false) + " to interact with " + this.getTag());
				} else {
					showIconLast = showIcon;
					showIcon = false;
					if (showIconLast) {
						onProximityExit(gameDriver);
					}
				}
			} else {
				//We are not within radius
				showGlowLast = showGlow;
				showGlow = false;
				if (showGlowLast) {
					hotspotColorFilter.setColor(0x00); //Black
					hotspotAlphaFilter.setAlphaPercent(50);
					hotspotImage.clean();
					hotspotImage.build();
					onZoneExit(gameDriver);
				}
			}
			} else {
			//We are dead!
			if (hotspotColorFilter.getColor() != 0x00 
					|| hotspotAlphaFilter.getAlphaPercent() != 50 || showIcon == false) {
				hotspotColorFilter.setColor(0x00); //Black
				hotspotAlphaFilter.setAlphaPercent(50);
				hotspotImage.clean();
				hotspotImage.build();
				showGlow = false;
				showIcon = false;
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#render(edu.cs495.engine.GameDriver, edu.cs495.engine.gfx.Renderer)
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		super.render(gameDriver, renderer);
		
		renderer.draw(hotspotImage, Integer.MIN_VALUE, (int)posX -  hotspotImage.getHalfWidth(), (int) posY - hotspotImage.getHalfHeight());
		if (showIcon) {
		renderer.drawOverlay(hotspotIcon,
				Integer.MAX_VALUE,
				(int)(posX - gameDriver.getGame().getCamera().getCamX()) - hotspotIcon.getHalfWidth(),
				(int)(posY - gameDriver.getGame().getCamera().getCamY()) - hotspotIcon.getHalfHeight()
				 	- yOffset - (int) (HOVER_MAGNITUDE * Math.cos(gameDriver.getGameTime() * HOVER_SPEED * Math.PI / GameDriver.NANO_SECOND)));
		}
	}
	
	/** Triggered when the zone is entered */
	public abstract void onZoneEnter(GameDriver gameDriver);
	
	/** Triggered when the zone is exited */
	public abstract void onZoneExit(GameDriver gameDriver);
	
	/** Triggered when the proximity zone is entered */
	public abstract void onProximityEnter(GameDriver gameDriver);
	
	/** Triggered when the proximity zone is exited */
	public abstract void onProximityExit(GameDriver gameDriver);
	
	/** The hotkey was triggered */
	public abstract void onTrigger(GameDriver gameDriver);
}
