/**
 * 
 */
package edu.cs495.game.objects.player;

import java.util.Arrays;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.input.Input;
import edu.cs495.game.objects.player.menus.AbstractMenu;
import edu.cs495.game.objects.player.menus.BuildMenu;
import edu.cs495.game.objects.player.menus.EscapeMenu;
import edu.cs495.game.objects.player.menus.GearMenu;
import edu.cs495.game.objects.player.menus.InventoryMenu;
import edu.cs495.game.objects.player.menus.NetworkMenu;

/**
 * @author Spencer
 *
 */
public class MenuRibbon extends AbstractGameObject {

	/** The sprite index of an inactive icon */
	public static final int ICON_INACTIVE = 0;
	/** The sprite index of an active icon */
	public static final int ICON_ACTIVE = 1;
	/** The sprite index of an icon that is hovered */
	public static final int ICON_HOVERING = 2;
	/** The sprite index of an icon that is hovered */
	public static final int ICON_DISABLED = 3;

	/** The icon height - all ribbon UI icons should be 16x16 */
	public static final int ICON_WIDTH = 16;
	/** The icon height - all ribbon UI icons should be 16x16 */
	public static final int ICON_HEIGHT = 16;
	/** The amount of padding between icons on the ribbon */
	public static final int ICON_PADDING = 1;

	/** The inventory menu */
	public final AbstractMenu INVENTORY_MENU = new InventoryMenu(this, 0);
	
	public final AbstractMenu GEAR_MENU = new GearMenu(this, 1);
	
	public final AbstractMenu NETWORK_MENU = new NetworkMenu(this, 2);
	
	public final AbstractMenu ESCAPE_MENU = new EscapeMenu(this, 3);
	
	public final AbstractMenu BUILD_MENU = new BuildMenu(this, -1);
	
	/** Menus */
	public final AbstractMenu[] menus = {
			INVENTORY_MENU,
			GEAR_MENU,
			NETWORK_MENU,
			ESCAPE_MENU,
			BUILD_MENU
	};

	/** The local user this ribbon belongs to */
	private LocalPlayer localPlayer;

	/**
	 * Initialize a menu ribbon
	 * 
	 * @param localPlayer - the local player this menu ribbon belongs to
	 * @param screenX     - the screen X position to display this menu ribbon
	 * @param screenY     - the screen Y position to display this menu ribbon
	 */
	public MenuRibbon(LocalPlayer localPlayer, int screenX, int screenY) {
		super("(Menu)", (float) screenX, (float) screenY, 0, ICON_HEIGHT);
		this.localPlayer = localPlayer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cs495.engine.game.AbstractGameObject#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		//Sort the menus
		Arrays.sort(menus);

		int totalWidth = 0;
		for (int i = 0; i < menus.length; i++) {
			AbstractMenu menu = menus[i];
			menu.init(gameDriver);

			if (menu.isVisible()) {
				if (totalWidth != 0) {
					totalWidth += ICON_PADDING;
				} // Padding
				totalWidth += ICON_WIDTH;
			}
		}

		this.width = totalWidth;
		this.height = ICON_HEIGHT;

		this.offX = (int) posX - width;
		this.offY = (int) posY - height;
	}
 
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cs495.engine.game.AbstractGameObject#update(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void update(GameDriver gameDriver) {
		// The current amount of visible items while looping
		int visibleIndex = 0;
		// Get the local input to see if the player is hovering the ribbon icons
		Input localInput = localPlayer.getInput();
		int mouseX = localInput.getRelativeX();
		int mouseY = localInput.getRelativeY();
		
		for (int i = 0; i < menus.length; i++) {
			AbstractMenu menu = menus[i];

			if (menu.isVisible()) {
				//If the menu is enabled, perform state checks
				if (menu.isEnabled()) {
					// Invoke menu via hotkey
					if (localInput.isKeyDown(menu.getKeyEvent())) {
						// Toggle menu state
						if (menu.isOpen()) {
							menu.close();
						} else {
							menu.open();
						}
					}
					
					// Invoke menu via click
					int iconX = offX + (visibleIndex * (ICON_WIDTH + ICON_PADDING));
					boolean hovering = false;
					if (mouseX >= iconX && mouseX <= iconX + ICON_WIDTH 
							&& mouseY >= offY && mouseY <= offY + ICON_HEIGHT) {
						hovering = true;
					
						if (localInput.isButtonDown(Input.BUTTONS.LEFT_CLICK)) {
							// Toggle menu state
							if (menu.isOpen()) {
								menu.close();
							} else {
								menu.open();
							}
						}
						
						if (menu.isOpen()) {
							localPlayer.addOptionText("Close > " + menu.getName());
						} else {
							localPlayer.addOptionText("Open > " + menu.getName());
						}
					}

					//Assign an icon frame for rendering
					if (hovering) {
						menu.setIconFrame(ICON_HOVERING);
					} else {
						if (menu.isOpen()) {
							menu.setIconFrame(ICON_ACTIVE);
						} else {
							menu.setIconFrame(ICON_INACTIVE);
						}
					}

					//Update the menu itself
					menu.update(gameDriver);
				} else {
					menu.setIconFrame(ICON_DISABLED);
				}
				
				visibleIndex++;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cs495.engine.game.AbstractGameObject#render(edu.cs495.engine.GameDriver,
	 * edu.cs495.engine.gfx.Renderer)
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		// Count the visible menus for our icon offset
		int currentVisibleOrder = 0;

		for (int i = 0; i < menus.length; i++) {
			AbstractMenu menu = menus[i];
			// Only render menus that are visible
			if (menu.isVisible()) {
				int iconX = offX + (currentVisibleOrder * (ICON_WIDTH + ICON_PADDING));
				// Draw icon
				renderer.drawOverlay(menu.getIcon(), menu.getIconFrame(), 0, Integer.MAX_VALUE, iconX, offY);

				// If enabled and open, render the menu
				if (menu.isEnabled() && menu.isOpen()) {
					menu.render(gameDriver, renderer);
				}

				// Increment visible count
				currentVisibleOrder++;
			}
		}
	}
	
	/** Update this ribbon's values */
	public void update() {
		int totalWidth = 0;

		for (int i = 0; i < menus.length; i++) {
			AbstractMenu menu = menus[i];

			if (menu.isVisible()) {

				//Keep track of current width
				if (totalWidth != 0) {
					totalWidth += ICON_PADDING;
				}
				totalWidth += menu.getIcon().getTileWidth();
			}
		}
		
		this.width = totalWidth;

		this.offX = (int) posX - width;
	}

	/**
	 * Return the local player that owns this menu ribbon
	 * 
	 * @return the local player using this menu ribbon
	 */
	public LocalPlayer getLocalPlayer() {
		return this.localPlayer;
	}

	/** Collapse all open menus */
	public void closeMenus() {
		for (int i = 0; i < menus.length; i++) {
			menus[i].close();
		}
	}

}
