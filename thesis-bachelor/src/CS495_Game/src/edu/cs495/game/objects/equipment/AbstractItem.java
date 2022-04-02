package edu.cs495.game.objects.equipment;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.game.AbstractLevel;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.FilteredImage;
import edu.cs495.engine.gfx.obj.Font;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.game.Game;
import edu.cs495.game.components.TooltipComponent;
import edu.cs495.game.objects.player.GearManager;
import edu.cs495.game.objects.player.Player;

/** An item is an object which can be stowed in the inventory
 * and dropped on the ground
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public abstract class AbstractItem extends AbstractGameObject{
	
	/** The time before this item disappears if it's dropped */
	public final static int MAX_ALIVE_TIME = 600;

	/** The name of this game item */
	protected String name;
	
	/** Tool tip text */
	protected String toolTip;
	
	/** Is the item on the ground of a level */
	protected boolean isOnGround;	
	
	/** The resource path of the icon */
	protected String iconPath;
	
	/** The inventory bucket icon */
	protected FilteredImage icon;
	
	/** The time to live for an item */
	protected float ttl;
	
	/** A description of the use action done when calling the use() function */
	protected String useText;
	
	/** A description of the alternate action done when calling the use() function */
	protected String altText;
	
	/** What to display in the top-left corner when the cursor is hovering the
	 * item in the inventory - Can be examine/use text */
	protected Image useTextImage;
	
	/** altText in image format */
	protected Image altTextImage;
	
	/** What to display in the top-left corner when the cursor is hovering the
	 * item in the inventory and shift is held - Acts as drop text */
	protected Image dropTextImage;
	
	

	/** Initialize an abstract item
	 * 
	 * @param name - the name of the item
	 * @param useText - the text describing the use of this item
	 * @param altText - the text describing the alternative action of this item
	 * @param iconPath - path of the icon
	 */
	public AbstractItem(String name, String toolTip, float posX, float posY, String iconPath, String useText, String altText) {
		super("(" + name + ")", 
				posX, 
				posY,
				0,
				0);
		this.name = name;
		this.toolTip = toolTip;
		this.isOnGround = false;
		this.ttl = MAX_ALIVE_TIME;
		this.iconPath = iconPath;
		this.useText = useText;
		this.altText = altText;
		
	}
	

	/** Initialize the item
	 * 
	 * @param gameDriver - the driver for the game
	 */
	@Override
	public void init(GameDriver gameDriver) {		
		
		//Set Use/Drop Text
		this.dropTextImage = Font.SMALL.getStringImage("Drop > " + this.name, 0xffffff00);
		this.useTextImage = Font.SMALL.getStringImage(useText, 0xffffff00);
		this.altTextImage = Font.SMALL.getStringImage(altText, 0xffffff00);
						
		//Init Icon
		FilteredImage icon = new FilteredImage(iconPath);
		this.icon = icon;
				
		TooltipComponent tooltipComponent = new TooltipComponent(
				((Game)gameDriver.getGame()).getPlayer(), 
						this, toolTip, 0xffffff00);
		tooltipComponent.init(gameDriver);
		this.components.add(tooltipComponent);
	}

	/** Update the item
	 * 
	 * @param gameDriver - the driver for the game
	 */
	@Override
	public void update(GameDriver gameDriver) {
		super.update(gameDriver);
		
		//Check for death
		if (isOnGround) {
		
			ttl -= GameDriver.UPDATE_DT;
			
			//If dead
			if (ttl <= 0) {
				kill();
			}
		}
	}
	
	/** Render the item
	 * 
	 * @param gameDriver - the driver for the game
	 * @param renderer - the renderer for the game
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		super.render(gameDriver, renderer);
		
		if (isOnGround) {
			renderer.draw(icon, (int) offX, (int) offY);
		}
	}
	
	/** Abstract use - Each item can have a useful function! */
	public abstract void use(GameDriver gameDriver, Player player);
	
	/** Abstract alternative action */
	public abstract void alt(GameDriver gameDriver, Player player);
	
	/** Spawn this item on the map level
	 * 
	 * @param posX - the position X coordinate
	 * @param posY - the position Y coordinate
	 */
	public void spawn(AbstractLevel level, int posX, int posY) {
		//Set position
		this.offX = posX - (icon.getWidth() / 2);
		this.offY = posY - (icon.getHalfHeight());
		
		//Set it on the ground with max time to live
		this.ttl = MAX_ALIVE_TIME;
		this.isOnGround = true;
		
		//Subscribe the item for updates
		level.addGameObject(this);
	
		//Console debug
		DeveloperLog.printLog("Spawned " + this.tag + " at " + posX + ", " +posY);
	}
	
	/** Drop this item from the player's inventory to the map
	 * 
	 * @param posX - the position X coordinate
	 * @param posY - the position Y coordinate
	 */
	public void drop(AbstractLevel level, GearManager parentGearManager, int posX, int posY) {
		if (parentGearManager.inventoryContains(this)) {
			//Remove from inv
			boolean success = parentGearManager.remove(this);
			if (success) {
				spawn(level, posX, posY);
			}
		}
	}
	
	/** Pickup this item, or attempt to, to the inventory
	 * 
	 * @param gearManager - the inventory gear manager
	 */
	public void pickUpBy(GameDriver gameDriver, GearManager gearManager) {
		//Check if pickup was successful
		if (gearManager.take(this)) {
			//Successful, so we're no longer on the ground
			this.isOnGround = false;
			//Unregister for world updates since we're off the world now
			gameDriver.getLevel().removeGameObject(this);
		}
	}
	
	//Getters and setters below
	
	/** Returns the use text of the item
	 * 
	 * @return the use text of the item
	 */
	public String getUseText(){
		return this.useText;
	}
	
	/** Sets and updates the useText and useTextImage
	 *
	 * @param useText - new use text
	 */
	protected void setUseText(String useText) {
		this.useText = useText;
		this.useTextImage = Font.SMALL.getStringImage(useText, 0xffffff00);
	}
	
	/** Returns the use use text string in image format
	 * 
	 * @return the use text image
	 */
	public Image getUseTextImage(){
		return this.useTextImage;
	}
	
	/** Returns the alt text of the item
	 * 
	 * @return the alt text of the item
	 */
	public String getAltText(){
		return this.altText;
	}
	
	/** Sets and updates the altText and altTextImage
	 *
	 * @param altText - new alt text
	 */
	protected void setAltText(String altText) {
		this.altText = altText;
		this.altTextImage = Font.SMALL.getStringImage(altText, 0xffffff00);
	}
	
	/** Returns the alt text string in image format
	 * 
	 * @return the alt text image
	 */
	public Image getAltTextImage(){
		return this.altTextImage;
	}
	
	/** Returns the drop text string in image format
	 * 
	 * @return the drop text image
	 */
	public Image getDropTextImage(){
		return this.dropTextImage;
	}
	
	/** Returns the use name of the item
	 * 
	 * @return the use name of the item
	 */
	public String getName(){
		return this.name;
	}
	
	
	/** @return The inventory bucket icon */
	public Image getIcon() {
		return icon;
	}
	/** Returns whether the item is on the ground
	 * 
	 * @return true if the item is on the ground, false otherwise
	 */
	public boolean isOnGround() {
		return isOnGround;
	}

}
