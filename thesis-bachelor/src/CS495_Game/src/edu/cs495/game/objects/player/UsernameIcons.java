package edu.cs495.game.objects.player;

import edu.cs495.engine.gfx.obj.Image;

/** Enumerated username icons for the display name under a player */
public enum UsernameIcons {
	
	/** The developer chat line icon */
	DEVELOPER("/player/ui/chat_gold_crown2.png"),
	
	/** The moderator chat line icon */
	MODERATOR("/player/ui/chat_silver_crown2.png");
	
	/** The image icon */
	public final Image icon;
	
	/** Initialize the icon
	 * 
	 * @param iconPath - path to the icon image file 
	 */
	UsernameIcons(final String iconPath) {
		this.icon = new Image(iconPath);
	}
}
