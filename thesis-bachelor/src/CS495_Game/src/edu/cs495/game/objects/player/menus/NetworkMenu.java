/**
 * 
 */
package edu.cs495.game.objects.player.menus;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.List;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Box;
import edu.cs495.engine.gfx.obj.Font;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.gfx.obj.SpriteSheet;
import edu.cs495.game.net.GameClient;
import edu.cs495.game.net.GameNetwork;
import edu.cs495.game.net.GameServer;
import edu.cs495.game.objects.player.MenuRibbon;
import edu.cs495.game.objects.player.Player;

/** The network menu
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class NetworkMenu extends AbstractMenu {
	
	/** The F key that triggers this menu */
	private static final int F_KEY = KeyEvent.VK_F3;

	/** The file path of the ribbon icon */
	private static final String RIBBON_ICON_PATH = "/player/ui/network_icon.png";

	/** The file path of the background */
	private static final String BACKGROUND_PATH = "/player/ui/network.png";
	
	/** The server/client logo sprite sheet */
	private static final String LOGO_PATH = "/player/ui/network_logo.png";
	
	/** The width of the logo */
	private static final int LOGO_WIDTH = 25;
	
	/** The height of the logo */
	private static final int LOGO_HEIGHT = 8;
	
	/** The sprite sheet index in the network logo for the server row */
	private static final int SERVER_ROW = 0;
	/** The sprite sheet index in the network logo for the client row */
	private static final int CLIENT_ROW = 1;
	
	/** The sprite sheet column index of the network logo sprite which represents unbound */
	private static final int UNBOUND = 0;
	/** The sprite sheet column index of the network logo sprite which represents active */
	private static final int BOUND = 1;
	/** The sprite sheet column index of the network logo sprite which represents waiting */
	private static final int HOVERING = 2;
	/** The sprite sheet column index of the network logo sprite which represents disabled */
	private static final int DISABLED = 3;
	
	/** Position of the helmet on the background of this menu */
	private static final Point SERVER_BUTTON_POS = new Point(2, 2);
	/** Position of the helmet on the background of this menu */
	private static final Point CLIENT_BUTTON_POS = new Point(28, 2);
	/** Position of the chest on the background of this menu */
	private static final Point ONLINE_BOX_POS = new Point(74, 11);
	/** Position of the legs on the background of this menu */
	private static final Point CONNECTION_BOX_POS = new Point(74, 71);
	/** Position of the shoes on the background of this menu */
	private static final Point BODY_MID_POS = new Point(40, 30);
	/** Position of the primary glove on the background of this menu */ 
	private static final Point PLAYERS_MID_POS = new Point(73, 66);
	
	/** Status boxes */
	/** A good connection */
	private static final Box HEALTHY = new Box(3, 6, 0xff20ee47, true);
	/** An OK connection */
	private static final Box OK = new Box(3, 6, 0xffc3a518, true);
	/** A poor connection */
	private static final Box POOR = new Box(3, 6, 0xff650909, true);
	
	/** Online */
	private static final Box ONLINE = new Box(3, 6, 0xff20ee47, true);
	/** Offline */
	private static final Box OFFLINE = new Box(3, 6, 0xff650909, true);
	/** Logging In */
	private static final Box LOGGING_IN = new Box(3, 6, 0xff1c0872, true);
	
	/** The network button spreadsheet */
	private SpriteSheet networkButton;
	
	/** The frame for the server button */
	private int serverFrame;
	
	/** The frame for the client button */
	private int clientFrame;
	
	/** The online player count */
	private int playerCount;
	
	/** The cached player count image */
	private Image playerCountImage;
	
	/** Create a newwork menu 
	 * 
	 * @param parentRibbon - the parent ribbon
	 * @param order - the order of this button
	 */
	public NetworkMenu(MenuRibbon parentRibbon, int order) {
		super(parentRibbon, F_KEY, order, "Network", RIBBON_ICON_PATH, BACKGROUND_PATH);
		this.playerCount = 0;
		this.playerCountImage = null;
		this.networkButton = null;
		this.serverFrame = DISABLED;
		this.clientFrame = DISABLED;
	}
	
	
	

	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		this.networkButton = new SpriteSheet(LOGO_PATH, LOGO_WIDTH, LOGO_HEIGHT);
		this.enabled = false;
		this.serverFrame = DISABLED;
		this.clientFrame = DISABLED;
	}




	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#update(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void update(GameDriver gameDriver) {
		super.update(gameDriver);
		
		//Count players in network
		int players = 0;
		List<AbstractGameObject> objects = gameDriver.getGame().getLevel().getObjects();
		for (int i = 0; i < objects.size(); i++) {
			AbstractGameObject object = objects.get(i);
			if (object instanceof Player) {
				players++;
			}
		}
			
		//Update cache
		if (this.playerCount != players) {
			this.playerCountImage = Font.CHAT.getStringImage("" + players, 0xffffffff);
			this.playerCount = players;
		}
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
		if (parentRibbon.getLocalPlayer().isOnline()) {
			if (parentRibbon.getLocalPlayer().getNetwork() instanceof GameServer) {
				if (mouseX >= offX + SERVER_BUTTON_POS.x && mouseX <= offX + SERVER_BUTTON_POS.x + LOGO_WIDTH
						&& mouseY >= offY + SERVER_BUTTON_POS.y && mouseY <= offY + SERVER_BUTTON_POS.y + LOGO_HEIGHT) {
					serverFrame = DISABLED;
				} else {
					serverFrame = BOUND;
				}
			} else {
				if (mouseX >= offX + CLIENT_BUTTON_POS.x && mouseX <= offX + CLIENT_BUTTON_POS.x + LOGO_WIDTH
						&& mouseY >= offY + CLIENT_BUTTON_POS.y && mouseY <= offY + CLIENT_BUTTON_POS.y + LOGO_HEIGHT) {
					clientFrame = DISABLED;
				} else {
					clientFrame = BOUND;
				}
			}
		} else {
			if (mouseX >= offX + SERVER_BUTTON_POS.x && mouseX <= offX + SERVER_BUTTON_POS.x + LOGO_WIDTH
					&& mouseY >= offY + SERVER_BUTTON_POS.y && mouseY <= offY + SERVER_BUTTON_POS.y + LOGO_HEIGHT) {
				serverFrame = HOVERING;
			} else {
				serverFrame = UNBOUND;
			}
			if (mouseX >= offX + CLIENT_BUTTON_POS.x && mouseX <= offX + CLIENT_BUTTON_POS.x + LOGO_WIDTH
					&& mouseY >= offY + CLIENT_BUTTON_POS.y && mouseY <= offY + CLIENT_BUTTON_POS.y + LOGO_HEIGHT) {
				clientFrame = HOVERING;
			} else {
				clientFrame = UNBOUND;
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see edu.cs495.game.objects.player.menus.AbstractMenu#onNotHovering(edu.cs495.engine.GameDriver, int, int)
	 */
	@Override
	public void onNotHovering(GameDriver gameDriver, int mouseX, int mouseY) {
		if (parentRibbon.getLocalPlayer().isOnline()) {
			this.clientFrame = (parentRibbon.getLocalPlayer().getNetwork() instanceof GameServer) ? DISABLED : BOUND;
			this.serverFrame = (parentRibbon.getLocalPlayer().getNetwork() instanceof GameServer) ? BOUND : DISABLED;
		} else {
			this.clientFrame = UNBOUND;
			this.serverFrame = UNBOUND;
		}
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
		
		//Draw network button
		renderer.drawOverlay(networkButton, serverFrame, SERVER_ROW, Integer.MAX_VALUE, 
				offX + SERVER_BUTTON_POS.x, offY + SERVER_BUTTON_POS.y);
		renderer.drawOverlay(networkButton, clientFrame, CLIENT_ROW, Integer.MAX_VALUE, 
				offX + CLIENT_BUTTON_POS.x, offY + CLIENT_BUTTON_POS.y);
		
		if (parentRibbon.getLocalPlayer().isOnline()) {
			GameNetwork localNetwork = parentRibbon.getLocalPlayer().getNetwork();
			if (localNetwork instanceof GameClient) {
				GameClient localClient = (GameClient) localNetwork;
				if (localClient.isLoggedIn()) {
					renderer.drawOverlay(ONLINE, Integer.MAX_VALUE, offX + ONLINE_BOX_POS.x, offY + ONLINE_BOX_POS.y);	
				} else {
					renderer.drawOverlay(LOGGING_IN, Integer.MAX_VALUE, offX + ONLINE_BOX_POS.x, offY + ONLINE_BOX_POS.y);		
				}
				
				
			} else if (localNetwork instanceof GameServer) {
				renderer.drawOverlay(ONLINE, Integer.MAX_VALUE, offX + ONLINE_BOX_POS.x, offY + ONLINE_BOX_POS.y);			
			}
		} else {
			renderer.drawOverlay(OFFLINE, Integer.MAX_VALUE, offX + ONLINE_BOX_POS.x, offY + ONLINE_BOX_POS.y);
		}
	
		
		
		renderer.drawOverlay(playerCountImage, Integer.MAX_VALUE, 
				offX + PLAYERS_MID_POS.x - playerCountImage.getHalfWidth(),
				offY + PLAYERS_MID_POS.y - playerCountImage.getHalfHeight());
	}
}
