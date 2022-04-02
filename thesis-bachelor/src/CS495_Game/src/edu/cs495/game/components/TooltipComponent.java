/**
 * 
 */
package edu.cs495.game.components;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractComponent;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Box;
import edu.cs495.engine.gfx.obj.Font;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.input.Input;
import edu.cs495.game.Game;
import edu.cs495.game.objects.equipment.AbstractItem;
import edu.cs495.game.objects.player.LocalPlayer;

/** This component adds tooptip overlay when the user's mouse hovers over the
 * item. 
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class TooltipComponent extends AbstractComponent{

	/** The expansion magnitude on the mouse trigger zone (aim assist) */
	private static final int HITBOX = 5;
	
	/** The amount of padding between the mouse when the item is hovered */
	private static final int POSITION_PADDING = 5;
	
	/** The border color of tooltips */
	private static final int BORDER_COLOR = 0xff2f2a2a;
	
	/** The inner boundary color of tooltips */
	private static final int BG_COLOR = 0xff323232;
	
	/** The local player which could activate the tooltip */
	private LocalPlayer localPlayer;
	
	/** The parent item we are tooltipping */
	private AbstractItem item;
	
	/** The tool tip text */
	private String text;
	
	/** The border of the tooltip box */
	private Box borderBox;
	
	/** The background box */
	private Box bgBox;
	
	/** The cached tool tip image */
	private Image toolTipImage;
	
	/** The font color of the tooltip */
	private int fontColor;
	
	/** Determines whether the tooltip is showing */
	private boolean show;
	
	
	/**Initialize a tooltip component
	 * 
	 * @param localPlayer - the local player which could hover the item
	 * @param item - the parent item
	 * @param text - tool tip text
	 * @param fontColor - font color
	 */
	public TooltipComponent(LocalPlayer localPlayer, AbstractItem item, String text, int fontColor) {
		super(localPlayer);
		this.localPlayer = localPlayer;
		this.item = item;
		this.text = text;
		this.fontColor = fontColor;
		this.show = false;
	}

	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractComponent#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		this.toolTipImage = Font.SMALL.getStringImage(this.text, this.fontColor);
		
		int boxWidth = 1 + toolTipImage.getWidth() + 1 + item.getIcon().getWidth() + 1;
		int boxHeight = 1 + Math.max(item.getIcon().getHeight(), toolTipImage.getHeight()) + 1;
		
		this.borderBox = new Box(boxWidth+2, boxHeight+2, BORDER_COLOR, false);
		this.bgBox = new Box(boxWidth, boxHeight, BG_COLOR, true);
	}

	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractComponent#update(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void update(GameDriver gameDriver) {
		
		Input myInput = localPlayer.getInput();

		// Get mouse coordinates
		int mouseX = myInput.getPosX();
		int mouseY = myInput.getPosY();

		// Check mouse coordinates
		if (item.isOnGround() &&
				(mouseX >= item.getOffX() - HITBOX) 
				&& (mouseX <= item.getOffX() + item.getIcon().getWidth() + HITBOX)
				&& (mouseY >= item.getOffY() - HITBOX)
				&& (mouseY <= item.getOffY() + item.getIcon().getHeight() + HITBOX)) {
			show = true;
		} else {
			show = false;
		}
	}

	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractComponent#render(edu.cs495.engine.GameDriver, edu.cs495.engine.gfx.Renderer)
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		Input myInput = ((Game) gameDriver.getGame()).getPlayer().getInput();
		
		// Get mouse coordinates
		int mouseX = myInput.getRelativeX();
		int mouseY = myInput.getRelativeY();
		
		if (show) {
			gameDriver.getRenderer().drawOverlay(borderBox, 0, mouseX + POSITION_PADDING,
					mouseY - POSITION_PADDING - borderBox.getHeight());
			gameDriver.getRenderer().drawOverlay(bgBox, 0, mouseX + POSITION_PADDING + 1,
					mouseY - POSITION_PADDING - borderBox.getHeight() + 1);
			gameDriver.getRenderer().drawOverlay(item.getIcon(), 0, mouseX + POSITION_PADDING + 2,
					mouseY - POSITION_PADDING - borderBox.getHeight() + 2);
			gameDriver.getRenderer().drawOverlay(toolTipImage, 0,
					mouseX + POSITION_PADDING + 2 + item.getIcon().getWidth() + 1,
					mouseY - POSITION_PADDING - borderBox.getHeight() + 1 + 5);
		}
		
	}

}
