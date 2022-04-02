/**
 * 
 */
package edu.cs495.game.objects.player.menus;

import java.awt.event.KeyEvent;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.game.objects.player.MenuRibbon;

/** The builder's menu
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class BuildMenu extends AbstractMenu{
	
	/** The F key that triggers this menu */
	private static final int F_KEY = KeyEvent.VK_F4;

	/** The file path of the ribbon icon */
	private static final String RIBBON_ICON_PATH = "/player/ui/build_icon.png";

	/** The file path of the background */
	private static final String BACKGROUND_PATH = "/player/ui/build.png";
	
	/** Initialize a build menu
	 * 
	 * @param parentRibbon - the parent menu ribbon
	 * @param order - order for this menu item
	 */
	public BuildMenu(MenuRibbon parentRibbon, int order) {
		super(parentRibbon, F_KEY, order, "Creative", RIBBON_ICON_PATH, BACKGROUND_PATH);
	}
	

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		this.visible = false;
		this.enabled = false;
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
	}
	
	
	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#onNotHovering(edu.cs495.engine.GameDriver, int, int)
	 */
	@Override
	public void onNotHovering(GameDriver gameDriver, int mouseX, int mouseY) {
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
	}
	
	

}
