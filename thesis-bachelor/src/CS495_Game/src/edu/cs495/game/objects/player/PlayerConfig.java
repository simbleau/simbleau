/**
 * 
 */
package edu.cs495.game.objects.player;

/** Player configuration sheet
 * @author Spencer Imbleau
 */
public class PlayerConfig {
	
	/** The player's username */
	private String username; 
	
	/** The skin color of the player */
	private int skinColor;
	
	/** The color of a shirt worn by default if the player has no chest armor */
	private int shirtColor;
	
	/** The color of pants worn by default if there is no leg armor */
	private int pantsColor;
	
	/** The color of the player's eyes */
	private int eyeColor;
	
	/** The health to initialize a player with */
	private float startHealth;
	
	/** Privilege level */
	private PlayerPrivileges privilege;
	
	/** Initializes the player configuration
	 * @param username : the username of the player 
	 * @param skinColor : skin color, in ARGB format
	 * @param shirtColor : shirt color, in ARGB format
	 * @param pantsColor : pants color, in ARGB format
	 * @param eyeColor : eye color, in ARGB format
	 * @param startHealth : starting health of the player
	 * @param privilege : the level of privilege the player has
	 */
	public PlayerConfig(String username, int skinColor, int shirtColor, int pantsColor, int eyeColor, float startHealth, PlayerPrivileges privilege) {
		this.username = username;
		this.skinColor = skinColor;
		this.shirtColor = shirtColor;
		this.pantsColor = pantsColor;
		this.eyeColor = eyeColor;
		this.startHealth = startHealth;
		this.privilege = privilege;
	}

	/** @return the username of the player */
	public String getUsername() {
		return username;
	}

	/** @return the color of the player's skin
	 */
	public int getSkinColor() {
		return skinColor;
	}

	/** @return the color of the player's shirt
	 */
	public int getShirtColor() {
		return shirtColor;
	}

	/** @return the color of the player's pants
	 */
	public int getPantsColor() {
		return pantsColor;
	}

	/** @return the eye color the player */
	public int getEyeColor() {
		return eyeColor;
	}
	
	/** @return the starting health of the player
	 */
	public float getStartHealth() {
		return startHealth;
	}
	
	/** @return the privilege level of the user */
	public PlayerPrivileges getPrivilege() {
		return this.privilege;
	}
	
	
	
	
}
