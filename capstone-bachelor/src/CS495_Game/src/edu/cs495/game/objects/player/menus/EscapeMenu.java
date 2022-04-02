/**
 * 
 */
package edu.cs495.game.objects.player.menus;

import java.awt.Point;
import java.awt.event.KeyEvent;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.SpriteSheet;
import edu.cs495.game.objects.player.MenuRibbon;

/** The escape/credit menu
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class EscapeMenu extends AbstractMenu{
	
	/** The F key that triggers this menu */
	private static final int F_KEY = KeyEvent.VK_ESCAPE;

	/** The file path of the ribbon icon */
	private static final String RIBBON_ICON_PATH = "/player/ui/escape_icon.png";

	/** The file path of the background */
	private static final String BACKGROUND_PATH = "/player/ui/escape.png";
	
	/** The server/client logo sprite sheet */
	private static final String EXIT_BUTTON_PATH = "/player/ui/exit_button.png";
	
	/** The width of the button */
	private static final int BUTTON_WIDTH = 25;
	
	/** The height of the button */
	private static final int BUTTON_HEIGHT = 8;
	
	/** The sprite sheet column index of the exit botton which indicates the button isn't hovered */
	private static final int INACTIVE = 0;
	
	/** The sprite sheet column index of the exit botton which indicates the button is hovered */
	private static final int HOVERING = 1;
	
	/** Position of the helmet on the background of this menu */
	private static final Point EXIT_BUTTON_POS = new Point(28, 70);
	
	/** The exit button sprite sheet */
	private SpriteSheet exitButton;
	
	/** The exit button frame */
	private int exitButtonFrame;
	
	public EscapeMenu(MenuRibbon parentRibbon, int order) {
		super(parentRibbon, F_KEY, order, "Exit", RIBBON_ICON_PATH, BACKGROUND_PATH);
		this.exitButton = null;
		this.exitButtonFrame = INACTIVE; 
	}
	
	

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		this.exitButton = new SpriteSheet(EXIT_BUTTON_PATH, BUTTON_WIDTH, BUTTON_HEIGHT);
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
		
		if (mouseX >= offX + EXIT_BUTTON_POS.x && mouseX <= offX + EXIT_BUTTON_POS.x + BUTTON_WIDTH 
				&& mouseY >= offY + EXIT_BUTTON_POS.y && mouseY <= offY + EXIT_BUTTON_POS.y + BUTTON_HEIGHT) {
			gameDriver.getWindow().close();
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
		if (mouseX >= offX + EXIT_BUTTON_POS.x && mouseX <= offX + EXIT_BUTTON_POS.x + BUTTON_WIDTH 
				&& mouseY >= offY + EXIT_BUTTON_POS.y && mouseY <= offY + EXIT_BUTTON_POS.y + BUTTON_HEIGHT) {
			exitButtonFrame = HOVERING;
			parentRibbon.getLocalPlayer().addOptionText("Exit Game");
		} else {
			exitButtonFrame = INACTIVE;
		}
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
		exitButtonFrame = INACTIVE;
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
		
		//Draw network button
		renderer.drawOverlay(exitButton, exitButtonFrame, 0, Integer.MAX_VALUE, 
				offX + EXIT_BUTTON_POS.x, offY + EXIT_BUTTON_POS.y);
	}
	
	

}
