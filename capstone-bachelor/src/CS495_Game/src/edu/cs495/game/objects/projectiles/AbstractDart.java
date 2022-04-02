/**
 * 
 */
package edu.cs495.game.objects.projectiles;

import java.awt.Point;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.satcollision.SATCircle;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.FilteredImage;
import edu.cs495.engine.gfx.obj.FilteredSpriteSheet;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.gfx.obj.Light;
import edu.cs495.engine.gfx.obj.filters.AlphaFilter;
import edu.cs495.engine.gfx.obj.filters.ColorSwapFilter;
import edu.cs495.engine.gfx.obj.filters.RotationFilter;
import edu.cs495.game.objects.player.Player;

/**
 * An abstract dart spell
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class AbstractDart extends AbstractProjectile {
	
	/** The depth of the object's hitbox on the y axis */
	private static final int OBJECT_DEPTH = 4;
	
	/** The default speed of a blast */
	protected static final int DEFAULT_SPEED = 100;

	/** The static blast shadow for a blast spell */
	protected static final Image STATIC_DART_SHADOW = new Image("/equipment/spells/dart_shadow.png");

	/** The amount of shadow frames */
	protected static final int SHADOW_FRAMES = 6;

	/** The color of the shadow */
	protected static final int SHADOW_COLOR = 0x232323;

	/** The amount of lighting variation percent for dynamic lighting only */
	protected static final int SHADOW_TWINKLE_PERCENT = 40; // 0% <= SHADOW_VARIATION% <= 100%

	/** The amount of frames in the animation */
	protected int totalFrames;
	/** The frame of animation */
	protected int frame;

	/** The path of the sprite sheet for this blast */
	protected String dartSheetPath;

	/** The blast shadow for this blast */
	protected FilteredImage dartShadow;
	/** The blast shadow alpha filter */
	protected AlphaFilter dsAlphaFilter;

	/** The sprite sheet for this dart spell */
	protected FilteredSpriteSheet dartSpriteSheet;
	/* The width of the dart tile */
	protected int tileWidth;
	/** The height of the dart tile */
	protected int tileHeight;
	/** The alpha filter for the sprite sheet */
	protected AlphaFilter alphaFilter;
	/** The rotation filter for the sprite sheet */
	protected RotationFilter rotationFilter;

	/** Bloom if there is lighting effects */
	private Light bloom;
	/** The speed of the projectile */
	protected int speed;
	/** The angle which the projectile moves */
	protected double theta;
	/** The run slope increment */
	protected float xInc;
	/** The rise slope increment */
	protected float yInc;
	/** The time this projectile spends alive without collision */
	protected float ttl;

	/**
	 * Initialize an abstract blast game object
	 * 
	 * @param blastSpriteSheetPath - the blast sprite sheet
	 * @param tileWidth            - the tile width
	 * @param tileHeight           - the tile height
	 * @param bloomRGB             - the bloom RGB color (RGB!! No alpha!)
	 * @param theta                - the angle to travel
	 * @param speed                - the speed to travel
	 * @param aliveTime            - the time this projectile can go on for
	 * @param origin               - the point of origin for this projectile
	 */
	public AbstractDart(String blastSpriteSheetPath, int tileWidth, int tileHeight, int bloomRGB, double theta,
			int speed, float ttl, Point origin, Player sender) {
		super("*DART*", origin.x, origin.y, tileWidth, tileHeight, OBJECT_DEPTH, sender);
		this.dartSheetPath = blastSpriteSheetPath;
		this.bloom = new Light(50, bloomRGB, 1);
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.speed = speed;
		this.theta = theta % (Math.PI * 2);
		this.posX = origin.x;
		this.posY = origin.y;
		this.xInc = 0f;
		this.yInc = 0f;
		this.ttl = ttl;
		
		// Set physics
		SATCircle hitCircle = new SATCircle(this, tileWidth / 4, tileWidth / 2, tileWidth / 2);
		this.physicsComponent.addCollidableArea(hitCircle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cs495.game.objects.projectiles.AbstractBlast#init(edu.cs495.engine.
	 * GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		// Initialize the sprite sheet
		this.dartSpriteSheet = new FilteredSpriteSheet(dartSheetPath, tileWidth, tileHeight);

		// Initialize animation frame
		this.totalFrames = dartSpriteSheet.getSheetWidth() / dartSpriteSheet.getTileWidth();
		this.frame = 0;

		// Make movement increment projections
		this.xInc = (float) Math.cos(theta) * GameDriver.UPDATE_DT * speed;
		this.yInc = (float) Math.sin(-theta) * GameDriver.UPDATE_DT * speed;

		// Center the projectile
		this.offX = (int) posX - (dartSpriteSheet.getTileWidth() / 2);
		this.offY = (int) posY - (dartSpriteSheet.getTileHeight() / 2 + Player.SHOOT_HEIGHT);

		// Initialize alpha filter (for the projectile) & set to 100%
		this.alphaFilter = new AlphaFilter();
		this.alphaFilter.setAlphaPercent(100);

		// Initialize a shadow color replacement filter
		ColorSwapFilter bsColorFilter = new ColorSwapFilter();
		bsColorFilter.addColorSwap(0xff00ff, SHADOW_COLOR);

		// Initialize shadow alpha filter
		this.dsAlphaFilter = new AlphaFilter();
		// Set shadow of blast to 40% less opaque as the blast itself
		this.dsAlphaFilter.setAlphaPercent(alphaFilter.getAlphaPercent() - 40);

		// Initialize projectile rotation filter
		this.rotationFilter = new RotationFilter(new Image(tileWidth, tileHeight, dartSpriteSheet.get(frame, 0)), // Img
				theta, // Initial Angle
				new Point(tileWidth / 2, tileHeight / 2)); // Anchor point = center

		// Initialize a blast shadow using the static shadow's info
		this.dartShadow = new FilteredImage(STATIC_DART_SHADOW.getWidth(), STATIC_DART_SHADOW.getHeight(),
				STATIC_DART_SHADOW.getPixels().clone());

		// Apply and build filters
		this.dartSpriteSheet.addFilter(this.rotationFilter);
		this.dartSpriteSheet.addFilter(this.alphaFilter);
		this.dartSpriteSheet.buildAll();

		this.dartShadow.addFilter(bsColorFilter);
		this.dartShadow.addFilter(this.dsAlphaFilter);
		this.dartShadow.build();
	}

	/**
	 * Move and update the blast spell
	 * 
	 * @param gameDriver - the game driver for this projectile
	 */
	public void update(GameDriver gameDriver) {
		super.update(gameDriver);
		
		//Update frame
		if (totalFrames > 1) {
			this.frame = (int) ((gameDriver.getGameTime() / (GameDriver.NANO_SECOND / totalFrames)) % totalFrames);
		}
		
		
		// If the particle is dead, start to fade alpha and possibly kill it
		if (ttl <= 0) {
			alphaFilter.setAlphaPercent(alphaFilter.getAlphaPercent() - 1);
			if (alphaFilter.getAlphaPercent() <= 0) {
				kill();
				return;
			}
		}
		
		this.dartSpriteSheet.clean(frame, 0);
		this.dartSpriteSheet.build(frame, 0);

		// Twinkle the shadow for blooming light effect if there's lighting in this
		// level
		int shadowTaxPercent = 60; // Amount opacity
		if (gameDriver.getLevel().hasLighting()) {
			// Frame = the tenth of a second
			double shadowFrame = (gameDriver.getGameTime() / (GameDriver.NANO_SECOND / SHADOW_FRAMES)) % SHADOW_FRAMES; // get
																													// the
																													// current
																													// frame
			int opacityAdjustment = -shadowTaxPercent + (int) (shadowFrame / SHADOW_FRAMES * SHADOW_TWINKLE_PERCENT);
			int opacity = Math.max(0, alphaFilter.getAlphaPercent() + opacityAdjustment);

			// Rebuild filter
			this.dsAlphaFilter.setAlphaPercent(opacity);
			this.dartShadow.clean();
			this.dartShadow.build();
		} else {
			// Set opacity to default
			if (dsAlphaFilter.getAlphaPercent() != 100 - shadowTaxPercent) {
				this.dsAlphaFilter.setAlphaPercent(100 - shadowTaxPercent);
				this.dartShadow.clean();
				this.dartShadow.build();
			}
		}

		// Move towards theta
		posX += xInc;
		posY += yInc;

		// Fix offset value
		this.offX = (int) posX - (dartSpriteSheet.getTileWidth() / 2);
		this.offY = (int) posY - (dartSpriteSheet.getTileHeight() / 2) - Player.SHOOT_HEIGHT;

		// Slowly kill projectile over time
		ttl -= GameDriver.UPDATE_DT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cs495.game.objects.projectiles.AbstractBlast#render(edu.cs495.engine.
	 * GameDriver, edu.cs495.engine.gfx.Renderer)
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		super.render(gameDriver, renderer);
		
		// Draw Shadow
		renderer.draw(dartShadow, Integer.MIN_VALUE, (int) offX, (int) posY - STATIC_DART_SHADOW.getHalfHeight());

		// Draw light emission
		if (gameDriver.getLevel().hasLighting()) {
			renderer.draw(bloom, (int) posY, (int) offX + (bloom.radius / 8), // 8 gives the center
					(int) offY + (bloom.radius / 8));
		}

		// Draw
		renderer.draw(dartSpriteSheet, frame, 0, (int) posY, (int) offX, (int) offY);
	}

}