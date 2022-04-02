/**
 * 
 */
package edu.cs495.game.objects.player.menus;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.gfx.obj.SpriteSheet;
import edu.cs495.engine.input.Input;
import edu.cs495.game.objects.player.MenuRibbon;

/**
 * An abstract menu for the local player
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public abstract class AbstractMenu extends AbstractGameObject implements Comparable<AbstractMenu> {

	/** The menu ribbon this menu belongs to */
	protected MenuRibbon parentRibbon;

	/** The F-key used to open this menu */
	protected int keyEvent;

	/** The order of this menu in the menu ribbon */
	protected int order;

	/** The menu name */
	protected String name;

	/** Determines whether this menu is enabled or not */
	protected boolean enabled;

	/** Determines whether this menu is visible or not */
	protected boolean visible;

	/** Determines whether this menu is open or closed */
	protected boolean open;
	
	/** Informs whether the user is hovering over the UI */
	protected boolean hovering;
	
	/** For determining when to trigger open and exit events */
	private boolean hoveringLast;

	/** The frame of the icon in the UI bar */
	protected int iconFrame;

	/** The file path of the menu ribbon icon */
	private String iconPath;

	/** The file path of the background image */
	private String backgroundPath;

	/** The icon on the ribbon */
	private SpriteSheet ribbonIcon;

	/** The background for the menu */
	private Image background;

	public AbstractMenu(MenuRibbon parentRibbon, int keyEvent, int order, String name, String iconPath,
			String backgroundPath) {
		super("(" + name + ")", 0, 0, 0, 0);
		this.parentRibbon = parentRibbon;
		this.keyEvent = keyEvent;
		this.order = order;
		this.name = name;
		this.open = false;
		this.hovering = false;
		this.hoveringLast = false;
		this.enabled = false;
		this.visible = false;
		this.iconPath = iconPath;
		this.backgroundPath = backgroundPath;
		this.iconFrame = MenuRibbon.ICON_DISABLED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cs495.engine.game.AbstractGameObject#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		this.ribbonIcon = new SpriteSheet(iconPath, MenuRibbon.ICON_WIDTH, MenuRibbon.ICON_HEIGHT);
		this.background = new Image(backgroundPath);
		this.width = background.getWidth();
		this.height = background.getHeight();
		this.open = false;
		this.hovering = false;
		this.hoveringLast = false;
		this.enabled = true;
		this.visible = true;
		this.offX = parentRibbon.getOffX() + parentRibbon.getWidth() - background.getWidth();
		this.offY = parentRibbon.getOffY() - 1 - height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cs495.engine.game.AbstractGameObject#update(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void update(GameDriver gameDriver) {
		if (open) {
			Input localInput = parentRibbon.getLocalPlayer().getInput();
			int mouseX = localInput.getRelativeX();
			int mouseY = localInput.getRelativeY();
			
			//Check if the mouse is within the menu
			if (mouseX >= offX && mouseX <= offX + width
					&& mouseY >= offY && mouseY <= offY + height) {
				if (!hoveringLast) {
					onEnter(gameDriver, mouseX, mouseY);
				}
				//Handle any event
				boolean leftClick = localInput.isButtonDown(Input.BUTTONS.LEFT_CLICK);
				boolean rightClick = localInput.isButtonDown(Input.BUTTONS.RIGHT_CLICK);
				if (rightClick) {
					onRightClick(gameDriver, mouseX, mouseY);
				} else if (leftClick) {
					onLeftClick(gameDriver, mouseX, mouseY);
				} else {
					onHovering(gameDriver, mouseX, mouseY);
				}
				hovering = hoveringLast;
				hoveringLast = true;
			} else {
				if (hoveringLast) {
					onExit(gameDriver, mouseX, mouseY);
				} else {
					onNotHovering(gameDriver, mouseX, mouseY);
				}
				hovering = hoveringLast;
				hoveringLast = false;
			}
		}
	}
	
	/** Handle a left click event on the menu
	 * 
	 * @param gameDriver - the driver for the game
	 * @param mouseX - the x coordinate of the mouse
	 * @param mouseY - the y coordinate of the mouse
	 */
	public void onLeftClick(GameDriver gameDriver, int mouseX, int mouseY) {
		DeveloperLog.print("Input left-clicked the '" + name + "' menu");
	}

	/** Handle a right click event on the menu
	 * 
	 * @param gameDriver - the driver for the game
	 * @param mouseX - the x coordinate of the mouse
	 * @param mouseY - the y coordinate of the mouse
	 */
	public void onRightClick(GameDriver gameDriver, int mouseX, int mouseY) {
		DeveloperLog.print("Input right-clicked the '" + name + "' menu");
	}

	/** Handle a mouse hover event
	 * 
	 * @param gameDriver - the driver for the game
	 * @param mouseX - the x coordinate of the mouse
	 * @param mouseY - the y coordinate of the mouse
	 */
	public abstract void onHovering(GameDriver gameDriver, int mouseX, int mouseY);
	

	/** Handle a mouse off the menu event
	 * 
	 * @param gameDriver - the driver for the game
	 * @param mouseX - the x coordinate of the mouse
	 * @param mouseY - the y coordinate of the mouse
	 */
	public abstract void onNotHovering(GameDriver gameDriver, int mouseX, int mouseY);

	/** Handle a mouse enter event
	 * 
	 * @param gameDriver - the driver for the game
	 * @param mouseX - the x coordinate of the mouse
	 * @param mouseY - the y coordinate of the mouse
	 */
	public void onEnter(GameDriver gameDriver, int mouseX, int mouseY) {
		DeveloperLog.print("Input entered the '" + name + "' menu");
	}

	/** Handle a mouse exit event
	 * 
	 * @param gameDriver - the driver for the game
	 * @param mouseX - the x coordinate of the mouse
	 * @param mouseY - the y coordinate of the mouse
	 */
	public void onExit(GameDriver gameDriver, int mouseX, int mouseY) {
		DeveloperLog.print("Input exited the '" + name + "' menu");
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
		if (open) {
			renderer.drawOverlay(background, Integer.MAX_VALUE, offX, offY);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AbstractMenu otherMenu) {
		return Integer.compare(this.order, otherMenu.order);
	}
	
	/** Return this menu's name
	 * 
	 * @return the name of the menu
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the keyEvent
	 */
	public int getKeyEvent() {
		return keyEvent;
	}

	/**
	 * @return the background
	 */
	public Image getBackground() {
		return background;
	}

	/**
	 * Return the ribbon icon
	 * 
	 * @return the ribbon icon
	 */
	public SpriteSheet getIcon() {
		return this.ribbonIcon;
	}

	/**
	 * Return the icon frame
	 * 
	 * @return the icon sprite sheet index for the column
	 */
	public int getIconFrame() {
		return this.iconFrame;
	}
	
	/** Set the icon frame
	 * 
	 * @param iconFrame - the new sprite index for the icon's column
	 */
	public void setIconFrame(int iconFrame) {
		this.iconFrame = iconFrame; 
	}

	/**
	 * Returns whether the menu is visible or not
	 * 
	 * @return true if the menu is visible, false otherwise
	 */
	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * Sets this menu to be visible or not
	 * 
	 * @param visible - true if you want the menu visible, false invisible
	 */
	public void setVisible(boolean visible) {
		if(this.visible != visible) {
			this.visible = visible;
			parentRibbon.update();
		}
	
		
	}

	/**
	 * Returns whether the menu is enabled or not
	 * 
	 * @return true - if the menu is enabled, false otherwise
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/** Set this menu to be enabled */
	public void enable() {
		this.enabled = true;
	}

	/** Set this menu to be disabled */
	public void disable() {
		this.enabled = false;
		this.open = false;
	}

	/**
	 * Returns whether the menu is open or not
	 * 
	 * @return true - if the menu is open
	 */
	public boolean isOpen() {
		return this.open;
	}

	/** Opens this menu */
	public void open() {
		if (!open) {
			//Close all other menus
			parentRibbon.closeMenus();
			this.open = true;
		}
	}

	/** Close this menu */
	public void close() {
		if (open) {
			this.open = false;
		}
	}
}
