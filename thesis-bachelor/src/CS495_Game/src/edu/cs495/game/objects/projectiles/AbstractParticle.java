/**
 * 
 */
package edu.cs495.game.objects.projectiles;

import java.awt.Point;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.satcollision.SATCircle;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Box;
import edu.cs495.engine.gfx.obj.FilteredImage;
import edu.cs495.engine.gfx.obj.Light;
import edu.cs495.engine.gfx.obj.filters.AlphaFilter;
import edu.cs495.engine.gfx.obj.filters.RotationFilter;
import edu.cs495.game.objects.player.Player;

/** A particle projectile (low tier)
 * 
 * @author Spencer Imbleau
 */
public class AbstractParticle extends AbstractProjectile {
	
	/** The depth of the object's hitbox on the y axis */
	private static final int OBJECT_DEPTH = 4;
	
	/** The default speed of a particle */
	protected static final int DEFAULT_SPEED = 100;
	
	/** The color of the shadow */
	protected static final int SHADOW_COLOR = 0x232323;

	/** The size of the particle */
	protected int particleRadius;
	/** The partle shadow for this blast */
	protected FilteredImage particleShadow;
	/** The particle shadow alpha filter */
	protected AlphaFilter psAlphaFilter;
	
	/** The color of the particle */
	protected int argb;
	/** The sprite sheet for this blast spell */
	protected FilteredImage particleImage;
	/** The alpha filter for the sprite sheet */
	protected AlphaFilter alphaFilter;
	/** The rotation filter for the sprite sheet */
	protected RotationFilter rotationFilter;

	/** Bloom if there is lighting effects */
	protected Light bloom;	
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
	/** The render order of this particle */
	protected float renderOrder;
	

	/** Initialize an abstract blast game object
	 * 
	 * @param blastSpriteSheetPath - the blast sprite sheet
	 * @param tileWidth - the tile width
	 * @param tileHeight - the tile height
	 * @param argb - the color of the particle
	 * @param theta - the angle to travel
	 * @param speed - the speed to travel
	 * @param aliveTime - the time this projectile can go on for
	 * @param origin - the point of origin for this projectile
	 */
	public AbstractParticle (int particleRadius, int argb, double theta, int speed,
			float ttl, Point origin, Player sender) {
		super("*PARTICLE*", origin.x, origin.y, 
				particleRadius * 2, particleRadius * 2, OBJECT_DEPTH, sender);
		this.particleRadius = particleRadius;
		this.argb = argb;
		this.bloom = new Light(particleRadius * 8, (argb & 0x00ffffff) , 1); //masked off the alpha 
		this.speed = speed;
		this.theta = theta;
		this.xInc = 0f;
		this.yInc = 0f;
		this.ttl = ttl;
		this.renderOrder = origin.y;
		
		//Build physics component
		SATCircle hitCircle = new SATCircle(this, particleRadius / 2, width, height);
		this.physicsComponent.addCollidableArea(hitCircle);		
	}
	
	/** Initializing the blast spell
	 * @param gameDriver : the game driver for the projectile
	 */
	public void init(GameDriver gameDriver) {				
		//Make movement increment projections
		this.xInc = (float) Math.cos(theta) * GameDriver.UPDATE_DT * speed;
		this.yInc = (float) Math.sin(-theta) * GameDriver.UPDATE_DT * speed;
		
		//Initialize a proper offX, offY to render at
		this.offX = (int) posX - width;
		this.offY = (int) posY - particleRadius + Player.SHOOT_HEIGHT;
		
		//Initialize the particle image
		int[] boxPixels = new Box(width, height, argb, true).getImageFormat().getPixels();
		int[] paddedBoxPixels = new int[width * 2 * height * 2];
		int yStartWidth = (height / 2) * (width * 2);
		for (int y = 0; y < height; y++) {
			int yWidth = y * width;
			for (int x = 0; x < width; x++) {
				paddedBoxPixels[x + (yWidth * 2) + yStartWidth] = boxPixels[x + yWidth];
			}
		}
		
		this.particleImage = new FilteredImage(width * 2, height * 2, paddedBoxPixels);
		//Initialize alpha filter (for the projectile) & set to 100%
		this.alphaFilter = new AlphaFilter();
		this.alphaFilter.setAlphaPercent(100);
		//Initialize projectile rotation filter
		this.rotationFilter = new RotationFilter();
		this.rotationFilter.setImage(particleImage);
		this.rotationFilter.setAnchor(new Point(width, height));
		this.rotationFilter.setTheta(theta);
		//Build particle
		this.particleImage.addFilter(this.rotationFilter);
		this.particleImage.addFilter(this.alphaFilter);
		this.particleImage.build();
		
		//Initialize a particle shadow
		this.particleShadow = new FilteredImage(
				width + 2,
				(height / 2) + 2, 
				new Box(width + 2, (height / 2) + 2, 0xff000000, true).getImageFormat().getPixels());
		//Initialize shadow alpha filter
		this.psAlphaFilter = new AlphaFilter();
		this.psAlphaFilter.setImage(particleShadow);
		this.psAlphaFilter.setAlphaPercent(alphaFilter.getAlphaPercent() - 70); //40% less
		//Build particle shadow
		this.particleShadow.addFilter(this.psAlphaFilter);
		this.particleShadow.build();
	}
	
	/** Move and update the blast spell
	 * 
	 * @param gameDriver : the game driver for this projectile
	 */
	public void update(GameDriver gameDriver) {
		super.update(gameDriver);
		
		//If the particle is dead, start to fade alpha and possibly kill it
		if (ttl <= 0) {
			alphaFilter.setAlphaPercent(alphaFilter.getAlphaPercent() - 1);
			if (alphaFilter.getAlphaPercent() <= 0) {
				kill();
				return;
			}
		}
		
		//Rotate the projectile (illusion of spinning)
		double iTheta = rotationFilter.getTheta(); //Initial theta
		double fTheta = (iTheta + (GameDriver.UPDATE_DT / 3 * Math.max(speed, 5))) % (2 * Math.PI); //Final theta
		this.rotationFilter.setTheta(fTheta);
		this.particleImage.clean();
		this.particleImage.build();
		
		//Create shadow
		int shadowTaxPercent = 90; //Amount opacity
			//Set opacity to default
			if (psAlphaFilter.getAlphaPercent() != 100 - shadowTaxPercent) {
				this.psAlphaFilter.setAlphaPercent(100 - shadowTaxPercent);
				this.particleShadow.clean();
				this.particleShadow.build();
			}
		
		// Move towards theta
		posX += xInc;
		posY += yInc;
		renderOrder += yInc;

		// Fix offset value
		this.offX = (int) posX - width;
		this.offY = (int) posY - particleRadius - Player.SHOOT_HEIGHT;

		// Slowly kill projectile over time
		ttl -= GameDriver.UPDATE_DT;
	}
	
	/** Render the particle
	 * 
	 * @param gameDriver - the game driver of the projectile
	 * @param renderer - the renderer of the projectile
	 */
	public void render(GameDriver gameDriver, Renderer renderer) {
		super.render(gameDriver, renderer);
		
		//Draw Shadow
		renderer.draw(particleShadow, 
				Integer.MIN_VALUE, 
				(int) posX - particleShadow.getHalfWidth(), 
				(int) posY - particleShadow.getHalfHeight());
		
		//Draw light emission
		if (gameDriver.getLevel().hasLighting()) {
			renderer.draw(bloom, 
					(int) renderOrder, 
					(int) offX + (bloom.radius / 8), //8 gives the center
					(int) offY + (bloom.radius / 8));
		}
		
		//Draw
		renderer.draw(
				particleImage,
				(int) renderOrder, 
				(int) offX, 
				(int) offY); 
	}

}