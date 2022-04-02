package edu.cs495.game.objects.player;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.audio.Sound;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.game.satcollision.CollisionEvent;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Box;
import edu.cs495.engine.gfx.obj.Font;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.input.Input;
import edu.cs495.engine.input.Input.BUTTONS;
import edu.cs495.game.net.GameNetwork;
import edu.cs495.game.objects.player.chatbox.ChatConsole;
import edu.cs495.game.objects.equipment.AbstractItem;
import edu.cs495.game.objects.equipment.gloves.AbstractGlove;
import edu.cs495.game.objects.equipment.gloves.AbstractGlove.GloveMode;
import edu.cs495.game.objects.player.menus.AbstractMenu;

/**
 * A local player is a player which is controlled by an input, not networking
 * updates.
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class LocalPlayer extends Player {

	/** The network for the player */
	private GameNetwork network;

	/** The player's input */
	private Input input;

	/** The chat console */
	private ChatConsole console;
	
	/** The menu ribbon */
	private MenuRibbon menuRibbon;
	
	/** The help text buffer */
	private List<String> optionTexts;

	/** Initialize the local player
	 * 
	 * @param input - the player's input
	 * @param config - the player's configuation
	 * @param posX - the position of the player X
	 * @param posY - the position of the player Y
	 * @throws UnknownHostException 
	 */
	public LocalPlayer(Input input, PlayerConfig config, float posX, float posY) {
		super(null, -1, config, posX, posY);
		this.network = null;
		this.input = input;
		this.optionTexts = new LinkedList<>();
	}

	/** Initialize the local player
	 * 
	 * @param gameDriver - the driver for the game
	 */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		
		// Init chat console 1px above and right of the bottom left corner
		this.console = new ChatConsole(1, gameDriver.getGameHeight() - 1, this);
		this.console.init(gameDriver);
		
		//Init the menu ribbon in the bottom right
		this.menuRibbon = new MenuRibbon(this, gameDriver.getGameWidth() - 1, gameDriver.getGameHeight() - 1);
		menuRibbon.init(gameDriver);
		
		//Reset the option texts
		this.optionTexts.clear();
	}

	/** Update the local player
	 * 
	 * @param gameDriver - the driver for the game
	 */
	@Override
	public void update(GameDriver gameDriver) {
		super.update(gameDriver);
		
		//Reset all the option texts
		this.optionTexts.clear();
		
		//Update console
		console.update(gameDriver);
		
		//Update menu ribbon
		menuRibbon.update(gameDriver);
		
		//Handle keystrokes for movement
		handleMovement();

		//Handle keystroke events
		handleKeyEvents(gameDriver);

		//Handle mouse events
		handleMouseEvents(gameDriver);
	}

	/** Helper method which handles capturing movement keystrokes and
	 * updating movement
	 * 
	 * @param gameDriver - the driver for the game
	 */
	private void handleMovement() {
		// Determine direction
		int dx = 0;
		int dy = 0;

		//Check keystrokes
		if (input.isKeyActive(KeyEvent.VK_W)) {
			dy--;
		}
		if (input.isKeyActive(KeyEvent.VK_S)) {
			dy++;
		}
		if (input.isKeyActive(KeyEvent.VK_A)) {
			dx--;
		}
		if (input.isKeyActive(KeyEvent.VK_D)) {
			dx++;
		}

		// Disable movement if we have released all keys
		if (dx == 0 && dy == 0) {
			isMoving = false;
		} else {
			// The player has moved, update direction
			movementTheta = (float) calcTheta(0, 0, dx, dy);

			if (dx == 0) {
				side = PlayerSides.CENTER;
			} else if (dx > 0) {
				side = PlayerSides.RIGHT;
			} else if (dx < 0) {
				side = PlayerSides.LEFT;
			}

			if (dy > 0) {
				direction = PlayerDirections.FORWARD;
			} else if (dy < 0) {
				direction = PlayerDirections.BACKWARD;
			}

			restingTheta = side.restAngle;
			isMoving = true;
		}
	}

	/** Helper method which implements the handling
	 * of all the player's keystrokes
	 * 
	 * @param gameDriver - the driver for the game
	 */
	private void handleKeyEvents(GameDriver gameDriver) {
		if (!console.isTyping()) {
			
			// Pickup items with space
			if (input.isKeyDown(KeyEvent.VK_SPACE)) {
				for (int i = 0; i < gameDriver.getLevel().getObjects().size(); i++) {
					AbstractGameObject obj = gameDriver.getLevel().getObjects().get(i);
					if (obj instanceof AbstractItem) {
						AbstractItem item = (AbstractItem) obj;
						if (canPickUp(item)) {
							Sound clip = new Sound("/audio/test.wav");
							clip.play();
							item.pickUpBy(gameDriver, gearManager);
						}
					}
				}
			}
		}
	}

	/** Helper method which implements the handling of a player's clicks
	 * 
	 * @param gameDriver - the driver for the game
	 */
	private void handleMouseEvents(GameDriver gameDriver) {
		// Handle mouse events
		boolean leftActive = input.isButtonActive(Input.BUTTONS.LEFT_CLICK);
		boolean rightActive = input.isButtonActive(Input.BUTTONS.RIGHT_CLICK);

		if (leftActive || rightActive) {
			// Capture mouse x,y
			int mouseX = input.getRelativeX();
			int mouseY = input.getRelativeY();

			//Determine if the mouse is in a menu
			boolean mouseOverMenu = false;
			if (mouseX >= menuRibbon.getOffX() && mouseX <= menuRibbon.getOffX() + menuRibbon.getWidth() 
			&& mouseY >= menuRibbon.getOffY() && mouseY <= menuRibbon.getOffY() + menuRibbon.getHeight()) {
				mouseOverMenu = true;
			} else {
				for (int i = 0; i < menuRibbon.menus.length; i++) {
					AbstractMenu menu = menuRibbon.menus[i];
					if (menu.isOpen()) {
						if (mouseX >= menu.getOffX() && mouseX <= menu.getOffX() + menu.getWidth()
						&& mouseY >= menu.getOffY() && mouseY <= menu.getOffY() + menu.getHeight()) {
							mouseOverMenu = true;
							break;
						}
					}
				}
			}
			
			if (!mouseOverMenu) {
				//Shoot, since we aren't clicking on a menu
				// Grab the location we're clicking
				int worldX = input.getPosX();
				int worldY = input.getPosY();

				// Update shot destination
				this.shotDestination = new Point(worldX, worldY);

				// Look at the point
				lookAt(worldX, worldY);

				// Tween Arms towards mouse point
				if (leftActive) {
					isPrimaryAiming = true;
				} else {
					isPrimaryAiming = false;
				}
				if (rightActive) {
					isSecondaryAiming = true;
				} else {
					isSecondaryAiming = false;
				}

				// Shoot towards mouse point
				if (shootingAllowed) {
					double shotTheta = calcTheta(posX, posY, (float) shotDestination.x, (float) shotDestination.y);
					if (isPrimaryAiming) {
						shootPrimarySpellTo(gameDriver, shotTheta);
					}
					if (isSecondaryAiming) {
						shootSecondarySpellTo(gameDriver, shotTheta);
					}
				}
			}
		} else {
			isPrimaryAiming = false;
			isSecondaryAiming = false;
		}
	}


	// RENDERING FUNCTIONS
	
	/** Remder the player and other things such as the chat console,
	 * the user interface, and the cursor
	 * 
	 * @param gameDriver - the driver for the game
	 * @param renderer - the renderer for the game
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		//Render the player
		super.render(gameDriver, renderer);

		// Render console
		console.render(gameDriver, renderer);
		
		//Render menus
		menuRibbon.render(gameDriver, renderer);
		
		//Draw option texts
		if (optionTexts.size() != 0) {
			String displayText = optionTexts.get(optionTexts.size() - 1); //Display topmost
			if (optionTexts.size() > 1) {
				displayText += " // " + (optionTexts.size() - 1) + " other options";
			}
			renderer.drawOverlay(displayText, Font.SMALL, 0xffffff00, Integer.MAX_VALUE, 1, 1);
		}

		//Capture mouse coordinates
		int mouseX = input.getRelativeX();
		int mouseY = input.getRelativeY();
		// Draw the cursor over everything else
		renderer.drawOverlay(new Box(5, 5, 0xff00ff00, false), Integer.MAX_VALUE, mouseX - 2, mouseY - 2);
		// Debug Text
		if (DeveloperLog.isDebugging()) {
			// Draw mouse coordinates
			Image mouseString = Font.SMALL.getStringImage(input.getRelativeX() + ", " + input.getRelativeY() + " ("
					+ input.getPosX() + ", " + input.getPosY() + ")", 0xffff0000);
			renderer.drawOverlay(mouseString, Integer.MAX_VALUE, input.getRelativeX(), input.getRelativeY());
		}
	}	

	/** Handle a collision event
	 * 
	 * @param collisionEvent - a collision event with the player
	 */
	@Override
	public void handleCollision(CollisionEvent collisionEvent) {
		super.handleCollision(collisionEvent);
	}

	/** Attempt to spellcast, although since this is the local player we 
	 * obey the shooting procedure and timers for the glove.
	 * 
	 * @param gameDriver - the driver for the game
	 * @double theta - the direction in radians to fire the spell
	 */
	@Override
	public void shootSecondarySpellTo(GameDriver gameDriver, double theta) {
		if (gearManager.hasSecondaryGlove()) {
			AbstractGlove glove = gearManager.getSecondaryGlove();
			if (glove.canShoot()) {

				if (glove.getMode() == GloveMode.SEMI) {
					if (input.isButtonDown(BUTTONS.RIGHT_CLICK)) {
						glove.shoot(gameDriver, this, new Point((int) posX, (int) posY), theta);
					}
				} else {
					glove.shoot(gameDriver, this, new Point((int) posX, (int) posY), theta);
				}

			}
		}
	}

	/** Attempt to spellcast, although since this is the local player we 
	 * obey the shooting procedure and timers for the glove.
	 * 
	 * @param gameDriver - the driver for the game
	 * @double theta - the direction in radians to fire the spell
	 */
	@Override
	public void shootPrimarySpellTo(GameDriver gameDriver, double theta) {
		if (gearManager.hasPrimaryGlove()) {
			AbstractGlove glove = gearManager.getPrimaryGlove();
			if (glove.canShoot()) {

				if (glove.getMode() == GloveMode.SEMI) {
					if (input.isButtonDown(BUTTONS.LEFT_CLICK)) {
						glove.shoot(gameDriver, this, new Point((int) posX, (int) posY), theta);
					}
				} else {
					glove.shoot(gameDriver, this, new Point((int) posX, (int) posY), theta);
				}

			}
		}
	}

	/** Return the chat console of the local player
	 * 
	 * @return
	 */
	public ChatConsole getConsole() {
		return this.console;
	}

	/** Return the menu ribbon for the local player
	 * 
	 * @return the player's menu ribbon
	 */
	public MenuRibbon getMenuRibbon() {
		return this.menuRibbon;
	}
	
	/** Return the player's input
	 * 
	 * @return this player's input
	 */
	public Input getInput() {
		return this.input;
	}

	/** Returns whether we are in an online session 
	 * 
	 * @return true if we are online, false otherwise
	 */
	public boolean isOnline() {
		return (this.network != null);
	}
	
	/** Sets the network for the player
	 * 
	 * @param network
	 */
	public void setNetwork(GameNetwork network) {
		if (network != null) {
			menuRibbon.NETWORK_MENU.enable();
		}
		this.network = network;
	}
	
	/** Returns the network for the player
	 * 
	 * @return the network
	 */
	public GameNetwork getNetwork() {
		return this.network;
	}
	
	/** Add an option text to dispaly during this game tick
	 * 
	 * @param useText - the option text
	 */
	public void addOptionText(String useText) {
		this.optionTexts.add(useText);
	}
}
