/**
 * 
 */
package edu.cs495.game.objects.player.menus;

import java.awt.event.KeyEvent;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Box;
import edu.cs495.game.objects.equipment.AbstractItem;
import edu.cs495.game.objects.player.GearManager;
import edu.cs495.game.objects.player.MenuRibbon;

/**
 * The inventory menu
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class InventoryMenu extends AbstractMenu {

	/** The F key that triggers this menu */
	private static final int F_KEY = KeyEvent.VK_F1;

	/** The file path of the ribbon icon */
	private static final String RIBBON_ICON_PATH = "/player/ui/inventory_icon.png";

	/** The file path of the background */
	private static final String BACKGROUND_PATH = "/player/ui/inventory.png";

	/** The amount of padding between inventory icons */
	private static final int PADDING = 2;

	/** The amount of total padding for an inventory item */
	private static final int INVENTORY_SPACING = MenuRibbon.ICON_WIDTH + PADDING;

	/** How many items are in a row on the inventory */
	private static final int INVENTORY_ROWS = 3;

	/** How many items are in a column on the inventory */
	private static final int INVENTORY_COLUMNS = 4;
	
	/** The size of an inventory item slot */
	private static final int INV_SLOT_SIZE = 16;
	
	/** A highlight box that appears under any armor that is highlighted */
	private static final Box HIGHLIGHT = new Box(INV_SLOT_SIZE, INV_SLOT_SIZE, 0xff169f94, true);
	
	/** An item which is highlighted by the mouse */
	private AbstractItem highlighted;
	
	/**
	 * Initialize the inventory menu
	 * 
	 * @param parentRibbon - the ribbon this menu belongs to
	 * @param order        - the order of this menu
	 */
	public InventoryMenu(MenuRibbon parentRibbon, int order) {
		super(parentRibbon, F_KEY, order, "Inventory", RIBBON_ICON_PATH, BACKGROUND_PATH);
		this.highlighted = null;
	}

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#update(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void update(GameDriver gameDriver) {
		super.update(gameDriver);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cs495.game.objects.player.menus.AbstractMenu#update(edu.cs495.engine.
	 * GameDriver)
	 */
	@Override
	public void onLeftClick(GameDriver gameDriver, int mouseX, int mouseY) {
		super.onLeftClick(gameDriver, mouseX, mouseY);
		
		if (highlighted != null) {
			if (parentRibbon.getLocalPlayer().getInput().isKeyActive(KeyEvent.VK_SHIFT)) {
				// On shift, drop the item
				highlighted.drop(gameDriver.getLevel(), parentRibbon.getLocalPlayer().getGearManager(), 
						(int) parentRibbon.getLocalPlayer().getPosX(), (int) parentRibbon.getLocalPlayer().getPosY());
			} else {
				// Use the item
				highlighted.use(gameDriver, parentRibbon.getLocalPlayer());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#onRightClick(edu.cs495.
	 * engine.GameDriver, int, int)
	 */
	@Override
	public void onRightClick(GameDriver gameDriver, int mouseX, int mouseY) {
		super.onRightClick(gameDriver, mouseX, mouseY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cs495.game.objects.player.menus.AbstractMenu#onHover(edu.cs495.engine.
	 * GameDriver, int, int)
	 */
	@Override
	public void onHovering(GameDriver gameDriver, int mouseX, int mouseY) {
		highlighted = null;
		// Check if we clicked any item
		GearManager localGear = parentRibbon.getLocalPlayer().getGearManager();
		// Cycle through items
		for (int i = 0; i < GearManager.MAX_INVENTORY_SIZE; i++) {
			if (localGear.itemExistsAt(i)) {
				// Reference item
				AbstractItem item = localGear.getInventoryItem(i);

				// Inventory Background is a 3x4 Matrix
				int row = i / INVENTORY_ROWS;
				int column = ((i - (row * INVENTORY_ROWS)) % INVENTORY_COLUMNS);

				int itemX = offX + PADDING + (column * (INV_SLOT_SIZE + PADDING));
				int itemY = offY + PADDING + (row * (INV_SLOT_SIZE +  PADDING));

				// Determine if we clicked item i
				if (mouseX >= itemX && mouseX <= itemX + INV_SLOT_SIZE
						&& mouseY >= itemY	&& mouseY <= itemY + INV_SLOT_SIZE) {
					
					highlighted = item;
					if (parentRibbon.getLocalPlayer().getInput().isKeyActive(KeyEvent.VK_SHIFT)) {
						parentRibbon.getLocalPlayer().addOptionText("Drop > " + item.getName());
					} else {
						// Use the item
						parentRibbon.getLocalPlayer().addOptionText(item.getUseText());
					}
					
				}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cs495.game.objects.player.menus.AbstractMenu#render(edu.cs495.engine.
	 * GameDriver, edu.cs495.engine.gfx.Renderer)
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		super.render(gameDriver, renderer);

		GearManager playerGear = parentRibbon.getLocalPlayer().getGearManager();
		if (playerGear.isInventoryEmpty()) {
			return;
		} else {
			for (int i = 0; i < GearManager.MAX_INVENTORY_SIZE; i++) {
				if (playerGear.itemExistsAt(i)) {
					// Inventory Background is a 3x4 Matrix
					int row = i / INVENTORY_ROWS;
					int column = ((i - (row * INVENTORY_ROWS)) % INVENTORY_COLUMNS);

					// Get item
					AbstractItem item = playerGear.getInventoryItem(i);

					// Get bounds
					int renderX = (offX + PADDING) + (column * INVENTORY_SPACING);
					int renderY = (offY + PADDING) + (row * INVENTORY_SPACING);
					
					if (highlighted == item) {
						renderer.drawOverlay(HIGHLIGHT, Integer.MAX_VALUE, renderX, renderY);
					}

					// Draw Sprite in inventory
					renderer.drawOverlay(item.getIcon(), Integer.MAX_VALUE, renderX, renderY);


				}
			}
		}
	}
	

}
