/**
 * 
 */
package edu.cs495.game.objects.player.menus;

import java.awt.Point;
import java.awt.event.KeyEvent;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Box;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.input.Input;
import edu.cs495.game.objects.equipment.armor.AbstractArmor;
import edu.cs495.game.objects.equipment.gloves.AbstractGlove;
import edu.cs495.game.objects.player.GearManager;
import edu.cs495.game.objects.player.MenuRibbon;

/** The gear menu
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class GearMenu extends AbstractMenu {

	/** The F key that triggers this menu */
	private static final int F_KEY = KeyEvent.VK_F2;

	/** The file path of the ribbon icon */
	private static final String RIBBON_ICON_PATH = "/player/ui/gear_icon.png";

	/** The file path of the background */
	private static final String BACKGROUND_PATH = "/player/ui/gear.png";
	
	/** Position of the helmet on the background of this menu */
	private static final Point HELMET_POS = new Point(20, 2);
	/** Position of the chest on the background of this menu */
	private static final Point CHEST_POS = new Point(20, 20);
	/** Position of the legs on the background of this menu */
	private static final Point LEGS_POS = new Point(20, 38);
	/** Position of the shoes on the background of this menu */
	private static final Point SHOES_POS = new Point(20, 56);
	/** Position of the primary glove on the background of this menu */ 
	private static final Point PGLOVE_POS = new Point(2, 20);
	/** Position of the secondary glove on the background of this menu */
	private static final Point SGLOVE_POS = new Point(38, 20);
	
	/** The size of a gear slot in the inventory */
	private static final int GEAR_SLOT_SIZE = 16;
	
	/** A highlight box that appears under any armor that is highlighted */
	private static final Box HIGHLIGHT = new Box(GEAR_SLOT_SIZE, GEAR_SLOT_SIZE, 0xff169f94, true);
	
	/** An armor piece which is highlighted by the mouse */
	private AbstractArmor highlighted;
	

	/**
	 * Initialize the gear menu
	 * 
	 * @param parentRibbon - the ribbon this menu belongs to
	 * @param order        - the order of this menu
	 */
	public GearMenu(MenuRibbon parentRibbon, int order) {
		super(parentRibbon, F_KEY, order, "Gear", RIBBON_ICON_PATH, BACKGROUND_PATH);
		this.highlighted = null;
	}

	
	
	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#update(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void update(GameDriver gameDriver) {
		// TODO Auto-generated method stub
		super.update(gameDriver);
		if (highlighted != null) {
			parentRibbon.getLocalPlayer().addOptionText(highlighted.getAltText());
		}
	}



	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#onLeftClick(edu.cs495.engine.GameDriver, int, int)
	 */
	@Override
	public void onLeftClick(GameDriver gameDriver, int mouseX, int mouseY) {
		super.onLeftClick(gameDriver, mouseX, mouseY);
		if (highlighted != null) {
			highlighted.alt(gameDriver, parentRibbon.getLocalPlayer());
		}
		
	}

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#onRightClick(edu.cs495.engine.GameDriver, int, int)
	 */
	@Override
	public void onRightClick(GameDriver gameDriver, int mouseX, int mouseY) {
		super.onRightClick(gameDriver, mouseX, mouseY);
	}

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#onHover(edu.cs495.engine.GameDriver, int, int)
	 */
	@Override
	public void onHovering(GameDriver gameDriver, int mouseX, int mouseY) {
		this.highlighted = null;
		
		GearManager localGear = parentRibbon.getLocalPlayer().getGearManager();
		
		// Highlight secondary glove
		if (localGear.hasSecondaryGlove()) {
			AbstractGlove sGlove = localGear.getSecondaryGlove();
			if (mouseX >= offX + SGLOVE_POS.x && mouseX <= offX + SGLOVE_POS.x + GEAR_SLOT_SIZE 
					&& mouseY >= offY + SGLOVE_POS.y && mouseY <= offY + SGLOVE_POS.y + GEAR_SLOT_SIZE) {
				this.highlighted = sGlove;
			}
		}
		
		// Highlight helmet
		if (localGear.hasHelmet()) {
			AbstractArmor helmet = localGear.getHelmet();
			if (mouseX >= offX + HELMET_POS.x && mouseX <= offX + HELMET_POS.x + GEAR_SLOT_SIZE 
					&& mouseY >= offY + HELMET_POS.y && mouseY <= offY + HELMET_POS.y + GEAR_SLOT_SIZE) {
				this.highlighted = helmet;
			}
		}
		
		// Highlight helmet
		if (localGear.hasChestArmor()) {
			AbstractArmor chest = localGear.getChestArmor();
			if (mouseX >= offX + CHEST_POS.x && mouseX <= offX + CHEST_POS.x + GEAR_SLOT_SIZE
					&& mouseY >= offY + CHEST_POS.y && mouseY <= offY + CHEST_POS.y + GEAR_SLOT_SIZE) {
				this.highlighted = chest;
			}
		}
		
		// Highlight legs
		if (localGear.hasLegArmor()) {
			AbstractArmor legs = localGear.getLegArmor();
			if (mouseX >= offX + LEGS_POS.x && mouseX <= offX + LEGS_POS.x + GEAR_SLOT_SIZE
					&& mouseY >= offY + LEGS_POS.y && mouseY <= offY + LEGS_POS.y + GEAR_SLOT_SIZE) {
				this.highlighted = legs;
			}
		}
		
		// Highlight shoes
		if (localGear.hasShoes()) {
			AbstractArmor shoes = localGear.getShoes();
			if (mouseX >= offX + SHOES_POS.x && mouseX <= offX + SHOES_POS.x + GEAR_SLOT_SIZE 
					&& mouseY >= offY + SHOES_POS.y && mouseY <= offY + SHOES_POS.y + GEAR_SLOT_SIZE) {
				this.highlighted = shoes;
			}
		}
		
		// Highlight primary glove
		if (localGear.hasPrimaryGlove()) {
			AbstractGlove pGlove = localGear.getPrimaryGlove();
			if (mouseX >= offX + PGLOVE_POS.x && mouseX <= offX + PGLOVE_POS.x + GEAR_SLOT_SIZE 
					&& mouseY >= offY + PGLOVE_POS.y && mouseY <= offY + PGLOVE_POS.y + GEAR_SLOT_SIZE) {
				this.highlighted = pGlove;
			}
		}
	}

	
	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#onNotHovering(edu.cs495.engine.GameDriver, int, int)
	 */
	@Override
	public void onNotHovering(GameDriver gameDriver, int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#onEnter(edu.cs495.engine.GameDriver, int, int)
	 */
	@Override
	public void onEnter(GameDriver gameDriver, int mouseX, int mouseY) {
		super.onEnter(gameDriver, mouseX, mouseY);
	}

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#onExit(edu.cs495.engine.GameDriver, int, int)
	 */
	@Override
	public void onExit(GameDriver gameDriver, int mouseX, int mouseY) {
		super.onExit(gameDriver, mouseX, mouseY);
		highlighted = null;
	}

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#render(edu.cs495.engine.GameDriver, edu.cs495.engine.gfx.Renderer)
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		super.render(gameDriver, renderer); //Draw background
		
		if (open) {
			
			//Draw the highlighted tile
			if (highlighted != null) {
				switch (highlighted.getType()) {
				case HELMET:
					renderer.drawOverlay(HIGHLIGHT, Integer.MAX_VALUE, offX + HELMET_POS.x, offY + HELMET_POS.y);
					break;
				case CHEST:
					renderer.drawOverlay(HIGHLIGHT, Integer.MAX_VALUE, offX + CHEST_POS.x, offY + CHEST_POS.y);
					break;
				case LEG:
					renderer.drawOverlay(HIGHLIGHT, Integer.MAX_VALUE, offX + LEGS_POS.x, offY + LEGS_POS.y);
					break;
				case P_GLOVE:
					renderer.drawOverlay(HIGHLIGHT, Integer.MAX_VALUE, offX + PGLOVE_POS.x, offY + PGLOVE_POS.y);
					break;
				case SHOES:
					renderer.drawOverlay(HIGHLIGHT, Integer.MAX_VALUE, offX + SHOES_POS.x, offY + SHOES_POS.y);
					break;
				case S_GLOVE:
					renderer.drawOverlay(HIGHLIGHT, Integer.MAX_VALUE, offX + SGLOVE_POS.x, offY + SGLOVE_POS.y);
					break;
				}
				
			}

			GearManager localGear = parentRibbon.getLocalPlayer().getGearManager();
			// Draw Secondary Glove
			if (localGear.hasSecondaryGlove()) {
				AbstractGlove sGlove = localGear.getSecondaryGlove();

				// Draw sprite in gear position
				renderer.drawOverlay(sGlove.getIcon(), Integer.MAX_VALUE,
						offX + SGLOVE_POS.x, offY + SGLOVE_POS.y);
			}
			// Draw Helmet
			if (localGear.hasHelmet()) {
				AbstractArmor helmet = localGear.getHelmet();

				// Draw sprite in gear position
				renderer.drawOverlay(helmet.getIcon(), Integer.MAX_VALUE,
						offX + HELMET_POS.x, offY + HELMET_POS.y);
			}
			
			// Draw Chest
			if (localGear.hasChestArmor()) {
				AbstractArmor chest = localGear.getChestArmor();

				// Draw sprite in gear position
				renderer.drawOverlay(chest.getIcon(), Integer.MAX_VALUE,
						offX + CHEST_POS.x, offY + CHEST_POS.y);

			}
			// Draw Legs
			if (localGear.hasLegArmor()) {
				AbstractArmor legs = localGear.getLegArmor();

				// Draw sprite in gear position
				renderer.drawOverlay(legs.getIcon(), Integer.MAX_VALUE,
						offX + LEGS_POS.x, offY + LEGS_POS.y);
			}
			// Draw Shoes
			if (localGear.hasShoes()) {
				AbstractArmor shoes = localGear.getLegArmor();

				// Draw sprite in gear position
				renderer.drawOverlay(shoes.getIcon(), Integer.MAX_VALUE,
						offX + SHOES_POS.x, offY + SHOES_POS.y);
			}
			// Draw Primary Glove
			if (localGear.hasPrimaryGlove()) {
				AbstractGlove pGlove = localGear.getPrimaryGlove();

				// Draw sprite in gear position
				renderer.drawOverlay(pGlove.getIcon(), Integer.MAX_VALUE,
						offX + PGLOVE_POS.x, offY + PGLOVE_POS.y);
			}
		
			//Draw hightlighted stats
			if (highlighted != null) {
				//Get the local input
				Input localInput = parentRibbon.getLocalPlayer().getInput();
				
				//Draw the stats image
				Image statsImage = highlighted.getDamageReductionTextImage();
				if (localInput.getRelativeX() + statsImage.getWidth() + 5 >= gameDriver.getGameWidth()) {
					renderer.drawOverlay(statsImage, Integer.MAX_VALUE, 
							localInput.getRelativeX() - 5 - statsImage.getWidth(), localInput.getRelativeY() - 5);
				} else {
					renderer.drawOverlay(statsImage, Integer.MAX_VALUE, 
							localInput.getRelativeX() + 5, localInput.getRelativeY() - 5);
				}
			}
		}
	}
	
	

}
