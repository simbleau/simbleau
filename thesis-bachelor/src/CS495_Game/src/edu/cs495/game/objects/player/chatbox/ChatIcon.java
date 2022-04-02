/**
 * 
 */
package edu.cs495.game.objects.player.chatbox;

import edu.cs495.engine.gfx.obj.Image;

/** Chat Icons for a chat line
 * 
 * @version February 2019
 * @author Spencer
 */
public enum ChatIcon {
	
	/** No Icon */
	NONE(0),

	/** The developer chat line icon */
	DEVELOPER("/player/ui/chat_gold_crown.png", 2),
	
	/** The moderator chat line icon */
	MODERATOR("/player/ui/chat_silver_crown.png", 1);
	
	/** The image icon */
	public final Image icon;
	
	/** The identifier for the chat icon */
	public final int identifier;
	
	/** Initialize the chat icon */
	ChatIcon(final String iconPath, final int identifier) {
		this.icon = new Image(iconPath);
		this.identifier = identifier;
	}
	
	/** Initialize the chat icon as no-icon */
	ChatIcon(final int identifier) {
		this.icon = null;
		this.identifier = identifier;
	}
}
