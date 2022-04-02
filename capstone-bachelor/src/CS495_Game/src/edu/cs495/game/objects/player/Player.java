package edu.cs495.game.objects.player;

import java.awt.Point;
import java.net.InetAddress;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.game.AbstractPhysicalGameObject;
import edu.cs495.engine.game.MapBoundaries;
import edu.cs495.engine.game.satcollision.CollisionEvent;
import edu.cs495.engine.game.satcollision.SATCircle;
import edu.cs495.engine.game.satcollision.SATPolygon;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Box;
import edu.cs495.engine.gfx.obj.FilteredImage;
import edu.cs495.engine.gfx.obj.FilteredSpriteSheet;
import edu.cs495.engine.gfx.obj.Font;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.gfx.obj.SpriteSheet;
import edu.cs495.engine.gfx.obj.filters.AlphaFilter;
import edu.cs495.engine.gfx.obj.filters.ColorSwapFilter;
import edu.cs495.engine.gfx.obj.filters.RotationFilter;
import edu.cs495.engine.gfx.obj.filters.TotalColorFilter;
import edu.cs495.game.objects.player.chatbox.ChatColor;
import edu.cs495.game.objects.player.chatbox.ChatIcon;
import edu.cs495.game.objects.projectiles.AbstractProjectile;
import edu.cs495.game.objects.equipment.AbstractItem;
import edu.cs495.game.objects.equipment.armor.AbstractArmor;
import edu.cs495.game.objects.equipment.gloves.AbstractGlove;

/** A player
 * 
 * @version March 2019
 * @author Spencer Imbleau
 */
public class Player extends AbstractPhysicalGameObject {

	// ==================================================
	// Constants
	// ==========
	
	/** A shirt that is always visible if no chest armor is equipped (Naked mode disabled) */
	private static final Image SHIRT = new Image("/player/shirt.png");
	/** Pants that is always visible if no leg armor is equipped (Naked mode disabled) */
	private static final SpriteSheet PANTS = new SpriteSheet("/player/pants.png", 32, 22);
	/** The player's shadow - same for everyone */
	private static final SpriteSheet SHADOW = new SpriteSheet("/player/shadow.png", 32, 13);

	/** Initial maximum player health */
	private static final float INITIAL_MAX_HEALTH = 100f;
	
	/** Initial Player Speed */
	private static final float INITIAL_PLAYER_SPEED = 125f;
	
	/** The distance one can pickup an item from (pixels) */
	private static final int PICKUP_RANGE = 30;

	/** The height in pixels above the ground player projectiles spawn from */
	public static final int SHOOT_HEIGHT = 45;
	
	/** The seconds to show the health for when attacked */
	public static final float SHOW_HEALTH_FOR = 5f;
	
	/** The seconds to show a public chat dialogue for */
	public static final float SHOW_DIALOGUE_FOR = 5f;

	// ==================================================
	// Non Constant Values
	// ====================
	
	/** The player's IP address */
	protected InetAddress ipAddress;
	
	/** The player's port */
	protected int port;

	/** The player's configuration  */
	protected PlayerConfig config;

	/** The player's gear manager */
	protected GearManager gearManager;

	/** The direction of the player */
	protected PlayerDirections direction;
	/** The side the player faces */
	protected PlayerSides side;
	
	/** Determines if movement is allowed */
	protected boolean movementAllowed;
	/** Determines if we are moving */
	protected boolean isMoving;
	/** The speed of the player */
	protected float speed;
	/** The direction the player is moving in radians */
	protected float movementTheta;
	
	/** Determines if shooting is allowed */
	protected boolean shootingAllowed;
	/** Determines if we are attempting to shoot our primary glove */
	protected boolean isPrimaryAiming;
	/** Determines if we are attempting to shoot our secondary glove */
	protected boolean isSecondaryAiming;
	/** The point which we are shooting at */
	protected Point shotDestination;
	
	/** The time the player was last in combat, which tells us when to render the health bar */
	private float showHealthFor;
	/** The health of the player */
	private float health;
	/** The maximum health of the player */
	private float maxHealth;
	
	/** The time to show a player's overhead text */
	private float showDialogueFor;
	/** The dialogue image */
	private Image dialogueImage;
	/** The dialogue shadow */
	private FilteredImage dialogueShadowImage;
	
	/** The sprite sheet of the player */
	private FilteredSpriteSheet skin;
	/** The default shirt for the player */
	private FilteredImage shirt;
	/** The default pants for the player */
	private FilteredSpriteSheet pants;
	/** The default shadow for the player */
	private FilteredSpriteSheet shadow;
	/** A player's eye */
	private Box eye;
	/** The sprite for the left arm */
	private FilteredImage leftArm;
	/** The rotation filter for the left arm */
	private RotationFilter leftArmRotationFilter;
	/** The sprite for the right arm */
	private FilteredImage rightArm;
	/** The rotation filter for the right arm */
	private RotationFilter rightArmRotationFilter;
	/** The resting angle which the player's arms rotate towards */
	protected double restingTheta;
	/** The image of the player's name */
	private Image usernameImg;
	/** The health box for the player */
	private Box healthBox;
	/** The damage box, part of the health bar, for the player */
	private Box damageBox;

	
	/** Initializes a player
	 * 
	 * @param address - the IP address for the player
	 * @param port - the port for the player
	 * @param config - The configuration of the player
	 * @param posX   - The x coordiate position of the player
	 * @param posY   - The y coordinate position of the player
	 */
	public Player(InetAddress ipAddress, int port, PlayerConfig config, float posX, float posY) {
		super(config.getUsername(), posX, posY, 
				PlayerSizes.WIDTH.size,
				PlayerSizes.HEIGHT.size,
				PlayerSizes.DEPTH.size);
		
		this.ipAddress = ipAddress;
		this.port = port;
		
		this.config = config;

		this.side = PlayerSides.CENTER;
		this.direction = PlayerDirections.FORWARD;
		this.restingTheta = side.restAngle;
		this.speed = INITIAL_PLAYER_SPEED;

		this.showHealthFor = SHOW_HEALTH_FOR;
		this.maxHealth = INITIAL_MAX_HEALTH;
		this.health = config.getStartHealth();
		
		this.showDialogueFor = 0f;
		this.dialogueImage = null;
		this.dialogueShadowImage = null;
		
		this.movementAllowed = false;
		this.shootingAllowed = false;
		this.isMoving = false;
		this.isPrimaryAiming = false;
		this.isSecondaryAiming = false;
		
		int healthWidth = (int) ((health / maxHealth) * PlayerSizes.HEALTH_BAR_WIDTH.size);
		this.healthBox = new Box(Math.min(PlayerSizes.HEALTH_BAR_WIDTH.size, healthWidth), // Width
				PlayerSizes.HEALTH_BAR_HEIGHT.size, // Height
				PlayerColors.HEALTH.color, // Color
				true); // Full box
		this.damageBox = new Box(PlayerSizes.HEALTH_BAR_WIDTH.size - healthBox.getWidth(), // Width
				PlayerSizes.HEALTH_BAR_HEIGHT.size, // Height
				PlayerColors.DAMAGE.color, // Color
				true); // Full box
		
		
		//Make hitboxes
		SATCircle headshotArea = new SATCircle(this, PlayerSizes.HEAD_RADIUS.size, 
				PlayerOffsets.CENTER_HEAD_X.offset, PlayerOffsets.CENTER_HEAD_Y.offset);
		this.physicsComponent.addCollidableArea(headshotArea);
		SATPolygon bodyArea = new SATPolygon(this);
		//Add physics vectors in a clockwise conventional format (as is dually necessary)
		bodyArea.addVector( //Top Left -> Top Right
				PlayerOffsets.HITBOX_X.offset, PlayerOffsets.HITBOX_Y.offset, 
				PlayerOffsets.HITBOX_X.offset + PlayerSizes.HITBOX_WIDTH.size, PlayerOffsets.HITBOX_Y.offset);
		bodyArea.addVector( //Top Right -> Bottom Right
				PlayerOffsets.HITBOX_X.offset + PlayerSizes.HITBOX_WIDTH.size, PlayerOffsets.HITBOX_Y.offset,
				PlayerOffsets.HITBOX_X.offset + PlayerSizes.HITBOX_WIDTH.size, PlayerOffsets.HITBOX_Y.offset 
					+ PlayerSizes.HITBOX_HEIGHT.size);
		bodyArea.addVector( //Bottom Right -> Bottom Left
				PlayerOffsets.HITBOX_X.offset + PlayerSizes.HITBOX_WIDTH.size, PlayerOffsets.HITBOX_Y.offset 
					+ PlayerSizes.HITBOX_HEIGHT.size,
					PlayerOffsets.HITBOX_X.offset, PlayerOffsets.HITBOX_Y.offset + PlayerSizes.HITBOX_HEIGHT.size);
		bodyArea.addVector( //Bottom Left -> Top Left 
				PlayerOffsets.HITBOX_X.offset, PlayerOffsets.HITBOX_Y.offset + PlayerSizes.HITBOX_HEIGHT.size,
				PlayerOffsets.HITBOX_X.offset, PlayerOffsets.HITBOX_Y.offset);
		
		this.physicsComponent.addCollidableArea(bodyArea);
	}

	/** Initialize the player
	 * 
	 * @param gameDriver - The driver for the game
	 */
	@Override
	public void init(GameDriver gameDriver) {		
		// Set the player's gear up
		this.gearManager = new GearManager(gameDriver, this);

		// Build Skin
		skin = new FilteredSpriteSheet("/player/skin.png", 32, 64);
		ColorSwapFilter skinColorFilter = new ColorSwapFilter();
		skinColorFilter.addColorSwap(0xff00ff, config.getSkinColor());
		skin.addFilter(skinColorFilter); // Add filter
		skin.buildAll(); // Apply skin color filter to all sprites

		// Build Shirt
		shirt = new FilteredImage(SHIRT.getWidth(), SHIRT.getHeight(), SHIRT.getPixels());
		ColorSwapFilter shirtColorFilter = new ColorSwapFilter();
		shirtColorFilter.addColorSwap(0xff00ff, config.getShirtColor());
		shirt.addFilter(shirtColorFilter); // Add filter
		shirt.build(); // Apply shirt color filter to sprite

		// Build Pants
		pants = new FilteredSpriteSheet(PANTS.getPixels(), PANTS.getSheetWidth(), PANTS.getSheetHeight(),
				PANTS.getTileWidth(), PANTS.getTileHeight());
		ColorSwapFilter pantsColorFilter = new ColorSwapFilter();
		pantsColorFilter.addColorSwap(0xff00ff, config.getPantsColor());
		pants.addFilter(pantsColorFilter); // Add filter
		pants.buildAll(); // Apply pants color filter to sprite sheet

		// Build Left Arm
		leftArm = new FilteredImage("/player/leftarm.png");
		ColorSwapFilter leftArmColorFilter = new ColorSwapFilter(leftArm);
		leftArmColorFilter.addColorSwap(0xff00ff, config.getSkinColor());
		leftArmRotationFilter = new RotationFilter(leftArm, Math.atan(-90),
				new Point(leftArm.getHalfWidth(), leftArm.getHalfHeight()));
		leftArm.addFilter(leftArmColorFilter);
		leftArm.addFilter(leftArmRotationFilter);
		leftArm.build();

		// Build Right Arm
		rightArm = new FilteredImage("/player/rightarm.png");
		ColorSwapFilter rightArmColorFilter = new ColorSwapFilter(rightArm);
		rightArmColorFilter.addColorSwap(0xff00ff, config.getSkinColor());
		rightArmRotationFilter = new RotationFilter(rightArm, Math.atan(-90),
				new Point(rightArm.getHalfWidth(), rightArm.getHalfHeight()));
		rightArm.addFilter(rightArmColorFilter);
		rightArm.addFilter(rightArmRotationFilter);
		rightArm.build();

		// Build Shadow
		shadow = new FilteredSpriteSheet(SHADOW.getPixels(), SHADOW.getSheetWidth(), SHADOW.getSheetHeight(),
				SHADOW.getTileWidth(), SHADOW.getTileHeight());
		ColorSwapFilter shadowColorFilter = new ColorSwapFilter();
		if (config.getPrivilege() == PlayerPrivileges.ADMIN) {
			shadowColorFilter.addColorSwap(0xff00ff, PlayerColors.ADMIN_SHADOW.color);
		} else if (config.getPrivilege() == PlayerPrivileges.MODERATOR) {
			shadowColorFilter.addColorSwap(0xff00ff, PlayerColors.MODERATOR_SHADOW.color);
		} else {
			shadowColorFilter.addColorSwap(0xff00ff, PlayerColors.SHADOW.color);
		}
		AlphaFilter shadowAlphaFilter = new AlphaFilter();
		shadowAlphaFilter.setAlphaPercent(50);
		shadow.addFilter(shadowColorFilter);
		shadow.addFilter(shadowAlphaFilter);
		shadow.buildAll();

		// Build eyes
		eye = new Box(2, 2, 0xff000000 | config.getEyeColor(), true);

		// Build Username image
		switch (config.getPrivilege()) {
		case ADMIN:
			usernameImg = generateUsernameImage(UsernameIcons.DEVELOPER, config.getUsername());
			break;
		case MODERATOR:
			usernameImg = generateUsernameImage(UsernameIcons.MODERATOR, config.getUsername());
			break;
		default:
			usernameImg = generateUsernameImage(config.getUsername());
			break;
		}

		// Equip Mod Crown if privileged
		if (this.getConfig().getPrivilege() == PlayerPrivileges.ADMIN 
				|| this.getConfig().getPrivilege() == PlayerPrivileges.MODERATOR) {
			gearManager.equipModCrown();
		}

		//Enable movement and shooting
		this.movementAllowed = true;
		this.shootingAllowed = true;
	}

	/** Update the player
	 * 
	 * @param gameDriver - The driver for the game
	 */
	@Override
	public void update(GameDriver gameDriver) {
		super.update(gameDriver);

		//Change the time to show the health for when not in combat
		if (showHealthFor > 0) {
			showHealthFor -= GameDriver.UPDATE_DT;
		}
		
		// Change the time to show the dialogue for when the player is speaking
		if (showDialogueFor > 0) {
			showDialogueFor -= GameDriver.UPDATE_DT;
		}

		//Movement
		if (movementAllowed) {
			if (isMoving) {
				float xInc = (float) Math.cos(movementTheta) * GameDriver.UPDATE_DT * speed;
				float yInc = (float) Math.sin(-movementTheta) * GameDriver.UPDATE_DT * speed;
				
				if (gameDriver.getLevel().hasBoundaries()) {

					if (isHorizontalPathClear(gameDriver.getLevel().getBoundaries(), posX + xInc)) {
						//Advance horizontally, path is clear
						posX += xInc;
					}
					
					if (isVerticalPathClear(gameDriver.getLevel().getBoundaries(), posY + yInc)) {
						//Advance vertically, path is clear
						posY += yInc;
					}
				}
			}
		}
		
		// Update Glove
		if (gearManager.hasPrimaryGlove()) {
			gearManager.getPrimaryGlove().tick(gameDriver);
		}
		if (gearManager.hasSecondaryGlove()) {
			gearManager.getSecondaryGlove().tick(gameDriver);
		}
		
		//Shooting
		if (shootingAllowed) {
			
			//Primary Glove
			if (isPrimaryAiming) {
				double aimTheta = calcTheta(posX, posY, (float) shotDestination.x, (float) shotDestination.y);
				tweenLeftArmTo(aimTheta, GameDriver.UPDATE_DT);
				if (gearManager.hasPrimaryGlove()) {
					shootPrimarySpellTo(gameDriver, aimTheta);
				}
			} else {
				tweenLeftArmTo(restingTheta, GameDriver.UPDATE_DT);	
			}
			
			//Secondary Glove
			if (isSecondaryAiming) {
				double aimTheta = calcTheta(posX, posY, (float) shotDestination.x, (float) shotDestination.y);
				tweenRightArmTo(aimTheta, GameDriver.UPDATE_DT);
				if (gearManager.hasSecondaryGlove()) {
					shootSecondarySpellTo(gameDriver, aimTheta);
				}
			} else {
				tweenRightArmTo(restingTheta, GameDriver.UPDATE_DT);	
			}
			
			
		}
		
		offX = (int) posX - PlayerSizes.HALF_WIDTH.size;
		offY = (int) posY - PlayerSizes.HEIGHT.size;
		
		// Check if we're dead
		if (health <= 0) {
			dead = true;
		}
	}

	/**
	 * This will be what is rendered when this player is called to render
	 * 
	 * @param gameDriver - The driver for the game
	 * @param renderer   - The renderer for the game
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		super.render(gameDriver, renderer);
		
		drawPlayer(gameDriver, renderer);
		
		drawDialogue(gameDriver, renderer);
	}
	
	
	/** The interaction of collision logic is here
	 * 
	 * @param collisionEvent - the event of a collision
	 */
	@Override
	public void handleCollision(CollisionEvent collisionEvent) {
		AbstractPhysicalGameObject force = collisionEvent.getForce();
		
		if (force instanceof AbstractProjectile) {
			AbstractProjectile projectile = (AbstractProjectile) collisionEvent.getForce();
			if (projectile.getSender() != this) {
				inflict((int) collisionEvent.getPulseMagnitude());
			}
		} else if (force instanceof Player) {
			Player player = (Player) force;
			player.say("Watch it!");
		}
	}
	
	/** Helper method to draw the player
	 * 
	 * @param renderer - the renderer which draws the player
	 */
	private void drawPlayer(GameDriver gameDriver, Renderer renderer) {
		// Define render order as our feet
		int renderOrder = (int) posY;

		// Draw Shadow (at 2 fps)
		int frame = (int) ((gameDriver.getGameTime() / GameDriver.NANO_SECOND) % 2);
		renderer.draw(shadow, frame, 0, renderOrder, offX, ((int) posY) + PlayerOffsets.SHADOW.offset);

		// Draw Arms first if direction is backwards
		if (direction == PlayerDirections.BACKWARD) {
			drawArms(renderer);
		}

		// Draw Skin
		renderer.draw(skin, side.spriteIndex, 0, renderOrder, offX, offY);

		// Draw Eyes if facing forward
		if (direction == PlayerDirections.FORWARD) {
			// Add depth to eyes!
			int adjustment = 0;
			if (side == PlayerSides.LEFT) {
				adjustment--; // Move eyes left one pixel if left
			} else if (side == PlayerSides.RIGHT) {
				adjustment++; // Move eyes right one pixel if right
			}
			renderer.draw(eye, renderOrder, (int) offX + PlayerOffsets.LEFT_EYE_X.offset + adjustment,
					(int) offY + PlayerOffsets.EYES_Y.offset);
			renderer.draw(eye, renderOrder, (int) offX + PlayerOffsets.RIGHT_EYE_X.offset + adjustment,
					(int) offY + PlayerOffsets.EYES_Y.offset);
		}

		// Draw Equipment
		// Legs > Shoes > Chest > Gloves > Helmet is the correct render ordering
		// Legs
		if (!gearManager.hasLegArmor()) {
			// Display default pants
			renderer.draw(pants, side.spriteIndex, 0, renderOrder, 
					(int) offX, (int) posY + PlayerOffsets.DEFAULT_PANTS.offset);
		} else {
			// Display equipped leg armor
			AbstractArmor equippedLegs = gearManager.getLegArmor();
			renderer.draw(equippedLegs.getSpriteSheet(), side.spriteIndex, direction.spriteIndex, renderOrder,
					(int) posX + equippedLegs.getPosXOffset(), (int) posY + equippedLegs.getPosYOffset());
		}
		// Shoes
		if (gearManager.hasShoes()) {
			// Display equipped shoes
			AbstractArmor equippedBoots = gearManager.getShoes();
			renderer.draw(equippedBoots.getSpriteSheet(), side.spriteIndex, direction.spriteIndex, renderOrder,
					(int) posX + equippedBoots.getPosXOffset(), (int) posY + equippedBoots.getPosYOffset());
		}
		// Chest
		if (!gearManager.hasChestArmor()) {
			// Display default shirt
			renderer.draw(shirt, renderOrder, (int) offX + PlayerOffsets.DEFAULT_SHIRT_X.offset,
					(int) offY + PlayerOffsets.DEFAULT_SHIRT_Y.offset);
		} else {
			// Display equipped chest armor
			AbstractArmor equippedChest = gearManager.getChestArmor();
			renderer.draw(equippedChest.getSpriteSheet(), side.spriteIndex, direction.spriteIndex, renderOrder,
					(int) posX + equippedChest.getPosXOffset(), (int) posY + equippedChest.getPosYOffset());
		}
		// Helmet
		if (gearManager.hasHelmet()) {
			// Display equipped helmet
			AbstractArmor equippedHelmet = gearManager.getHelmet();
			renderer.draw(equippedHelmet.getSpriteSheet(), side.spriteIndex, direction.spriteIndex, renderOrder,
					(int) posX + equippedHelmet.getPosXOffset(), (int) posY + equippedHelmet.getPosYOffset());
		}
		// END OF DRAWING EQUIPMENT

		// Draw Arms on top if forward
		if (direction == PlayerDirections.FORWARD) {
			drawArms(renderer);
		}

		// Draw Health bar
		if (showHealthFor > 0) {
			renderer.draw(damageBox, renderOrder, (int) offX + healthBox.getWidth(),
					(int) offY - PlayerSizes.HEALTH_BAR_HEIGHT.size + PlayerOffsets.HEALTH_BAR.offset);
			renderer.draw(healthBox, renderOrder, (int) offX,
					(int) offY - PlayerSizes.HEALTH_BAR_HEIGHT.size + PlayerOffsets.HEALTH_BAR.offset);
		}

		// Draw Username Image
		renderer.draw(usernameImg, renderOrder, (int) posX - usernameImg.getHalfWidth(),
				(int) posY + PlayerOffsets.USERNAME.offset);
	}

	/** Helper method to draw the player's arms
	 * 
	 * @param renderer - the renderer which draws the player
	 */
	private void drawArms(Renderer renderer) {
		// Define render order as our feet
		int renderOrder = (int) posY;
		
		renderer.draw(leftArm, renderOrder, (int) posX + PlayerOffsets.LEFT_ARM_X.offset,
				(int) offY + PlayerOffsets.ARMS_Y.offset);
		renderer.draw(rightArm, renderOrder, (int) posX + PlayerOffsets.RIGHT_ARM_X.offset,
				(int) offY + PlayerOffsets.ARMS_Y.offset);

		if (gearManager.hasPrimaryGlove()) {
			// Display equipped primary glove
			AbstractGlove equippedGlove = gearManager.getPrimaryGlove();
			renderer.draw(equippedGlove.getSpriteSheet(), 0, 0, renderOrder, 
					(int) posX + PlayerOffsets.LEFT_ARM_X.offset,
					offY + PlayerOffsets.ARMS_Y.offset);
		}
		if (gearManager.hasSecondaryGlove()) {
			// Display equipped secondary glove
			AbstractGlove equippedGlove = gearManager.getSecondaryGlove();
			renderer.draw(equippedGlove.getSpriteSheet(), 0, 0, renderOrder, 
					(int) posX + PlayerOffsets.RIGHT_ARM_X.offset,
					offY + PlayerOffsets.ARMS_Y.offset);
		}
	}
	
	
	/** Helper method to draw dialogue above a player's head
	 * 
	 * @param gameDriver
	 * @param renderer
	 */
	private void drawDialogue(GameDriver gameDriver, Renderer renderer) {
		// Draw Health bar
		if (showDialogueFor > 0) {
			renderer.draw(dialogueShadowImage, (int) posY, 
					(int) posX - dialogueImage.getHalfWidth() + 1, 
					offY - dialogueImage.getHeight()+ PlayerOffsets.DIALOGUE.offset + 1);
			renderer.draw(dialogueImage, (int) posY, 
					(int) posX - dialogueImage.getHalfWidth(), 
					offY - dialogueImage.getHeight()+ PlayerOffsets.DIALOGUE.offset);
		}
	}
	//
	//==================
	// Public static functions
	//==================
	//
	
	/** Create a username image for the game world with an icon
	 * 
	 * @param icon - the icon for the player
	 * @param username - the username
	 * @return a username image
	 */
	public static Image generateUsernameImage(UsernameIcons icon, String username) {
		//Retrieve icon
		Image lineIcon = null;
		if (icon == null) {
			//No icon, so return something else
			return generateUsernameImage(username);
		} else {
			lineIcon = icon.icon;
		}
		//Generate username image
		Image usernameImage = Font.SMALL.getStringImage(username, PlayerColors.USERNAME.color);
		
		//Calculate total width now
		int totalWidth = usernameImage.getWidth();
		if (lineIcon != null) {
			totalWidth += lineIcon.getWidth();
			totalWidth += PlayerSizes.USERNAME_ICON_PADDING.size;
		}
		
		//Create portrait image data buffer
		int[] usernameImageBuffer = new int[totalWidth * Font.SMALL.SIZE];
		int currentX = 0;
		//Copy icon
		if (lineIcon != null) {
			for (int y = 0; y < Math.min(lineIcon.getHeight(), Font.SMALL.SIZE); y++) {
				int yIconWidth = y * lineIcon.getWidth();
				int yTotalWidth = (y + (Font.SMALL.SIZE / 2) - lineIcon.getHalfHeight()) * totalWidth;
				if (yTotalWidth < 0) {
					continue; //Do not draw this line. Clip it.
				} else {
					for (int x = 0; x < lineIcon.getWidth(); x++) {
						usernameImageBuffer[x + yTotalWidth] = lineIcon.getPixels()[x + yIconWidth];
					}
				}
			}
			currentX += lineIcon.getWidth() + PlayerSizes.USERNAME_ICON_PADDING.size;
		}
		//Copy username image
		for (int y = 0; y < Font.SMALL.SIZE; y++) {
			int yUsernameWidth = y * usernameImage.getWidth();
			int yTotalWidth = y * totalWidth;
			for (int x = 0; x < usernameImage.getWidth(); x++) {
				usernameImageBuffer[x + yTotalWidth + currentX] = usernameImage.getPixels()[x + yUsernameWidth];
			}
		}
		
		//Export
		Image exportImage = new Image(totalWidth, Font.SMALL.SIZE, usernameImageBuffer);
		return exportImage;
	}
	
	/** Create a username image for the game world with no icon
	 * 
	 * @param username - the username to render
	 * @return a username image
	 */
	public static Image generateUsernameImage(String username) {
		return Font.SMALL.getStringImage(username, PlayerColors.USERNAME.color);
	}
	
	
	//
	//==================
	// Helper methods
	//==================
	//
	
	/**
	 * Calculate an angle in radians from two points
	 * 
	 * @param x1 - start x
	 * @param y1 - start y
	 * @param x2 - end x
	 * @param y2 - end y
	 * @return the angle in radians (-pi > theta > pi)
	 */
	protected double calcTheta(float x1, float y1, float x2, float y2) {
		float deltaX = x2 - x1;
		float deltaY = y2 - y1;

		// Calculate angle theta
		return Math.atan2(-deltaY, deltaX);
	}
	
	/** Check if the horizontal path from the player to a coordinate is blocked
	 * 
	 * @param destX - the destination x
	 * @return true if the path is clear, false otherwise
	 */
	protected boolean isHorizontalPathClear(MapBoundaries bounds, float destX) {
		
		float dx = (destX - posX);
		if (dx < 0) {
			//Player moved left
			int x = (int) destX;
			for (int y = ((int) posY) - shadow.getTileHeight() / 2; 
					y < ((int) posY) + shadow.getTileHeight() / 2; y++) {
				if (bounds.isBlocked(x - PlayerSizes.HALF_WIDTH.size, // Left
						y)) {
					return false;
				}
			}
		} else if (dx > 0) {
			//Player moved right
			int x = (int) destX;
			for (int y = ((int) posY) - shadow.getTileHeight() / 2; 
					y < ((int) posY) + shadow.getTileHeight() / 2; y++) {
				if (bounds.isBlocked(x + PlayerSizes.HALF_WIDTH.size, // Left
						y)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/** Check if the vertical path from the player to a coordinate is blocked
	 * 
	 * @param destY - the destination y
	 * @return true if the path is clear, false otherwise
	 */
	protected boolean isVerticalPathClear(MapBoundaries bounds, float destY) {
		float dy = (destY - posY);
		if (dy < 0) {
			//Player moved up
			int y = (int) destY;
			for (int x = ((int) posX) - PlayerSizes.HALF_WIDTH.size; 
					x < ((int) posX) + PlayerSizes.HALF_WIDTH.size; x++) {
				if (bounds.isBlocked(x, y - shadow.getTileHeight() / 2)) {
					return false;
				}
			}
		} else if (dy > 0) {
			//Player moved down
			int y = (int) destY;
			for (int x = ((int) posX) - PlayerSizes.HALF_WIDTH.size; 
					x < ((int) posX) + PlayerSizes.HALF_WIDTH.size; x++) {
				if (bounds.isBlocked(x, y + shadow.getTileHeight() / 2)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	//
	//==================
	//Public Manipulation Functions
	//==================
	//
	
	/** Update the connection for the player
	 * 
	 * @param ipAddress - the IP Address of the player
	 * @param port - the port of the player
	 */
	public void updateConnection(InetAddress ipAddress, int port) {
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	
	/** Make the user say something with overhead dialogue
	 * 
	 * @param message - text for the player to say
	 */
	public void say(String message) {
		this.dialogueImage = Font.SMALL.getStringImage(message, ChatColor.DIALOGUE.color);
		this.dialogueShadowImage = new FilteredImage(
				dialogueImage.getWidth(), dialogueImage.getHeight(), dialogueImage.getPixels());
		TotalColorFilter shadowColorFilter = new TotalColorFilter(dialogueShadowImage);
		shadowColorFilter.setColor(0x000000); //Pitch black
		this.dialogueShadowImage.addFilter(shadowColorFilter);
		this.dialogueShadowImage.build();
		this.showDialogueFor = SHOW_DIALOGUE_FOR;
	}
	
	/** Enable the player to move */
	public void enableMovement() {
		this.movementAllowed = true;
	}
	
	/** Disable the player from moving */
	public void disableMovement() {
		this.movementAllowed = false;
	}
	
	/** Makes the player face a point
	 * 
	 * @param worldX - the x coordinate in the level
	 * @param worldY - the y coordinate in the level
	 */
	public void lookAt(float worldX, float worldY) {
		// Update direction to point where we are shooting
		if (worldY > posY) {
			if (direction != PlayerDirections.FORWARD) {
				direction = PlayerDirections.FORWARD;
			}
		} else {
			if (direction != PlayerDirections.BACKWARD) {
				direction = PlayerDirections.BACKWARD;
			}
		}

		// Update side to point where we are shooting
		if (worldX > posX + 25) { // > 50px to Right
			side = PlayerSides.RIGHT;
			restingTheta = side.restAngle;
		} else if (worldX < posX - 25) { // < 50px to Left
			side = PlayerSides.LEFT;
			restingTheta = side.restAngle;
		} else { // Center
			side = PlayerSides.CENTER;
			restingTheta = side.restAngle;
		}
	}

	/**
	 * Heal the player
	 * 
	 * @param amount - the amount to heal
	 */
	public void heal(int amount) {
		health = Math.min(maxHealth, health + amount);

		// Adjust health bar widths
		int healthWidth = Math.min(PlayerSizes.HEALTH_BAR_WIDTH.size,
				(int) ((health / maxHealth) * PlayerSizes.HEALTH_BAR_WIDTH.size));
		healthBox.setWidth(healthWidth);
		damageBox.setWidth(PlayerSizes.HEALTH_BAR_WIDTH.size - healthWidth);
		
		showHealthFor = 5f;
		
		DeveloperLog.printLog(this.getConfig().getUsername() + " healed " + amount + " health");
	}

	/**
	 * Inflict damage to the player
	 * 
	 * @param damage - the amount to damage
	 */
	public void inflict(int damage) {
		health = Math.max(0, health - damage);

		// Adjust health bar widths
		int healthWidth = Math.min(PlayerSizes.HEALTH_BAR_WIDTH.size,
				(int) ((health / maxHealth) * PlayerSizes.HEALTH_BAR_WIDTH.size));
		healthBox.setWidth(healthWidth);
		damageBox.setWidth(PlayerSizes.HEALTH_BAR_WIDTH.size - healthWidth);
		
		showHealthFor = 5f;
		
		DeveloperLog.printLog(this.getConfig().getUsername() + " was dealt " + damage + " damage");
	}

	/**
	 * Returns whether an item can be picked up by the player
	 * 
	 * @param item - the item to test
	 * @return true if the player can pickup the item, false otherwise
	 */
	public boolean canPickUp(AbstractItem item) {
		if (gearManager.isInventoryFull()) {
			return false;
		}

		int itemX = item.getOffX() + item.getIcon().getHalfWidth();
		int itemY = item.getOffY() + item.getIcon().getHeight();
		
		int dy = ((int) posY - itemY);
		int dx = ((int) posX - itemX);

		double distance = Math.sqrt(dy * dy + dx * dx);

		if (distance < PICKUP_RANGE) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Shoot the primary spell from the user's glove towards an angle
	 * 
	 * @param gameDriver - the driver for the game
	 * @param theta      - the angle to shoot from the primary hand
	 */
	public void shootSecondarySpellTo(GameDriver gameDriver, double theta) {
		if (gearManager.hasSecondaryGlove()) {
//			// Hand position
//			int handX = (int) (rightArm.getHalfWidth() * Math.cos(theta) + rightArm.getHalfWidth());
//			int handY = (int) (rightArm.getHalfWidth() * Math.sin(-theta) + rightArm.getHalfHeight());
//
//			// Glove position
//			int rGloveX = (int) (posX + handX) + PlayerOffset.RIGHT_ARM_X.offset;
//			int rGloveY = (int) (offY + PlayerOffset.ARMS_Y.offset + handY);
			
			AbstractGlove glove = gearManager.getSecondaryGlove();
			glove.shoot(gameDriver, this, new Point((int) posX, (int) posY), theta);
		}
	}

	/**
	 * Shoot the primary spell from the user's glove towards an angle
	 * 
	 * @param gameDriver - the driver for the game
	 * @param theta      - the angle to shoot from the primary hand
	 */
	public void shootPrimarySpellTo(GameDriver gameDriver, double theta) {
		if (gearManager.hasPrimaryGlove()) {
//			// Hand position
//			int handX = (int) (leftArm.getHalfWidth() * Math.cos(theta) + leftArm.getHalfWidth());
//			int handY = (int) (leftArm.getHalfWidth() * Math.sin(-theta) + leftArm.getHalfHeight());
//
//			// Glove position
//			int lGloveX = (int) (posX + handX) + PlayerOffset.LEFT_ARM_X.offset;
//			int lGloveY = (int) (offY + PlayerOffset.ARMS_Y.offset + handY);
			
			AbstractGlove glove = gearManager.getPrimaryGlove();
			glove.shoot(gameDriver, this, new Point((int) posX, (int) posY), theta);
		}
		
//		leftArm.clean();
//		leftArmRotationFilter.setTheta(theta);
//		leftArm.build();
//		if (gearManager.hasPrimaryGlove()) {
//			gearManager.getPrimaryGlove().setTheta(theta);
//		}
	}

	/** Helper method to tween the right arm towards an angle theta (in rads)
	 * 
	 * @param theta     - the angle to tween towards
	 * @param UPDATE_DT - the update DT of the game
	 */
	public void tweenRightArmTo(double theta, final double UPDATE_DT) {
		//If we are already tweened to the direction, we return
		if (rightArmRotationFilter.getTheta() == theta) {
			return; //We are already in this position
		}
		
		//Offset by 2pi to get absolute difference
		double current = (2 * Math.PI + rightArmRotationFilter.getTheta());
		double target = (2 * Math.PI + theta);
		double diff = current - target;
		
		//Get our adjustment value
		double adjustment = UPDATE_DT / 10 * speed;
		
		//Get new angle
		double newTheta;
		//Test if we are within snap range
		if (Math.abs(current - target) < adjustment) {
			newTheta = theta;
		} else {
			if (diff > Math.PI || diff > -Math.PI && diff < 0) {
				newTheta = (rightArmRotationFilter.getTheta() + adjustment);
			} else {
				newTheta = rightArmRotationFilter.getTheta() - adjustment;
			}
		}
		
		//Build new sprites
		rightArm.clean();
		rightArmRotationFilter.setTheta(newTheta);
		rightArm.build();
		if (gearManager.hasSecondaryGlove()) {
			gearManager.getSecondaryGlove().setTheta(newTheta);
		}
	}

	/**
	 * Helper method to tween the left arm towards an angle theta (in rads)
	 * 
	 * @param theta     - the angle to tween towards
	 * @param UPDATE_DT - the update DT of the game
	 */
	public void tweenLeftArmTo(double theta, final double UPDATE_DT) {
		//If we are already tweened to the direction, we return
		if (leftArmRotationFilter.getTheta() == theta) {
			return; //We are already in this position
		}
		
		//Offset by 2pi to get absolute difference
		double current = (2 * Math.PI + leftArmRotationFilter.getTheta());
		double target = (2 * Math.PI + theta);
		double diff = current - target;
		
		//Get our adjustment value
		double adjustment = UPDATE_DT / 10 * speed;
		
		//Get new angle
		double newTheta;
		//Test if we are within snap range
		if (Math.abs(current - target) < adjustment) {
			newTheta = theta;
		} else {
			if (diff > Math.PI || diff > -Math.PI && diff < 0) {
				newTheta = (leftArmRotationFilter.getTheta() + adjustment);
			} else {
				newTheta = leftArmRotationFilter.getTheta() - adjustment;
			}
		}
		
		//Build new sprites
		leftArm.clean();
		leftArmRotationFilter.setTheta(newTheta);
		leftArm.build();
		if (gearManager.hasPrimaryGlove()) {
			gearManager.getPrimaryGlove().setTheta(newTheta);
		}
	}
	
	
	//
	//==================
	// Getters, Setters, and Information
	//==================
	//
	
	/** Return the IP Address of the player
	 * 
	 * @return the IP Address for the player
	 */
	public InetAddress getIPAddress() {
		return this.ipAddress;
	}
	
	/** Return the port of the player
	 * 
	 * @return the port for the player
	 */
	public int getPort() {
		return this.port;
	}
	
	/** Returns the player's {@link PlayerConfig} information
	 * 
	 * @return the player's {@link PlayerConfig} information
	 * @see PlayerConfig
	 */
	public PlayerConfig getConfig() {
		return config;
	}

	/** Sets the player's {@link PlayerConfig} information
	 * 
	 * @param config - the new configuration
	 * @see PlayerConfig
	 */
	public void setConfig(PlayerConfig config) {
		this.config = config;
	}
	
	/** Return the player's chat icon
	 * 
	 * @return the player's chat icon
	 */
	public ChatIcon getChatIcon() {
		ChatIcon icon;
		switch (config.getPrivilege()) {
		case ADMIN:
			icon = ChatIcon.DEVELOPER;
			break;
		case MODERATOR: 
			icon = ChatIcon.MODERATOR;
			break;
		case NORMAL:
			default:
				icon = ChatIcon.NONE;
				break;
		}
		
		return icon;
	}
	
	/**
	 * Return the gear manager of the player
	 * 
	 * @return the gear manager for this player
	 */
	public GearManager getGearManager() {
		return this.gearManager;
	}
	
	/** Return whether the player is prohibited from movement
	 * 
	 * @return true if the player can move, false otherwise
	 */
	public boolean canMove() {
		return this.movementAllowed;
	}

	/**
	 * Returns the theta of the left arm
	 * 
	 * @return the left arm angle in radians
	 */
	public double getLeftArmTheta() {
		return this.leftArmRotationFilter.getTheta();
	}

	/**
	 * Returns the theta of the right arm
	 * 
	 * @return the right arm angle in radians
	 */
	public double getRightArmTheta() {
		return this.rightArmRotationFilter.getTheta();
	}
	
} 
