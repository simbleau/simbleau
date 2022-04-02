/**
 * 
 */
package edu.cs495.game.objects.player.chatbox;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.obj.Font;
import edu.cs495.engine.gfx.obj.Image;

/** This handles one line of text in the chatview
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class ChatLine {	
	/** The separator between a username and the message shared across all chat lines */
	static final Image DELIMETER = Font.CHAT.getStringImage(">", ChatColor.USERNAME.color);
	
	/** The default amount of seconds a message displays */
	static final int DEFAULT_TTL = 7; //7 seconds
	
	/** Padding between chat line items (such as the delimeter, message, username) except the icon */
	static final int PADDING = 2;
	
	/** Padding between the user's icon and username */
	static final int ICON_PADDING = 1;

	/** The icon of the message */
	public final int ICON;

	/** The chat type */
	public final int CHAT_TYPE;

	/** The time in nanoseconds which this chatline was sent */
	public final long TIME_SENT;
	
	/** The time to live in nanoseconds */
	public final long TTL;
	
	/** The player name sender */
	public final String NAME;

	/** The message of the chat line */
	public final String MESSAGE;

	/** The buffer for the actual chat line render image */
	private Image chatLineImage;

	/**
	 * Initialize a chat line
	 * 
	 * @param icon    - the icon identifier
	 * @param type    - the type of the message
	 * @param sent	- the time in nanoseconds when this message was sent
	 * @param ttl - the time in seconds for this message to display
	 * @param name    - the name of the player who messaged
	 * @param message - the message sent by the player
	 */
	public ChatLine(int icon, int type, int ttl, long sent, String name, String message) {
		this.ICON = icon;
		this.CHAT_TYPE = type;
		this.TTL = ttl * GameDriver.NANO_SECOND;
		this.TIME_SENT = sent;
		this.NAME = name;
		this.MESSAGE = message;
		this.chatLineImage = null;
	}

	/** Returns the {@link #chatLineImage} with respect as a singleton
	 * 
	 * @return the {@link #chatLineImage}
	 */
	public Image getChatLineImage() {
		//Check if we need to initialize the image
		if (chatLineImage == null) {
			init();
		}
		
		return this.chatLineImage;
	}

	/** Build and return a chatline portrait image
	 * This image comprises the username and accompanying prefix icon
	 * 
	 * @param icon - an icon to display left of the user's name
	 * @param username - the username
	 * @return an {@link Image} of the chat portrait
	 * @see ChatIcon
	 */
	public static Image getChatPortrait(ChatIcon icon, String username) {
		//Retrieve icon
		Image lineIcon = null;
		if (icon == ChatIcon.NONE) {
			return getChatPortrait(username);
		} else if (icon == ChatIcon.DEVELOPER) {
			lineIcon = ChatIcon.DEVELOPER.icon;
		} else if (icon == ChatIcon.MODERATOR) {
			lineIcon = ChatIcon.MODERATOR.icon;
		} else {
			//Unknown icon, don't display it.
			return getChatPortrait(username);
		}
		//Generate username image
		Image usernameImage = Font.CHAT.getStringImage(username, ChatColor.USERNAME.color);
		
		//Calculate total width now
		int totalWidth = usernameImage.getWidth();
		if (lineIcon != null) {
			totalWidth += lineIcon.getWidth();
			totalWidth += ICON_PADDING;
		}
		
		//Create portrait image data buffer
		int[] portraitBuffer = new int[totalWidth * Font.CHAT.SIZE];
		int currentX = 0;
		//Copy icon
		if (lineIcon != null) {
			for (int y = 0; y < Math.min(lineIcon.getHeight(), Font.CHAT.SIZE); y++) {
				int yIconWidth = y * lineIcon.getWidth();
				int yPortraitWidth = y * totalWidth;
				for (int x = 0; x < lineIcon.getWidth(); x++) {
					portraitBuffer[x + yPortraitWidth] = lineIcon.getPixels()[x + yIconWidth];
				}
			}
			currentX += lineIcon.getWidth() + ICON_PADDING;
		}
		//Copy username image
		for (int y = 0; y < Math.min(usernameImage.getHeight(), Font.CHAT.SIZE); y++) {
			int yUsernameWidth = y * usernameImage.getWidth();
			int yPortraitWidth = y * totalWidth;
			for (int x = 0; x < usernameImage.getWidth(); x++) {
				portraitBuffer[x + yPortraitWidth + currentX] = usernameImage.getPixels()[x + yUsernameWidth];
			}
		}
		
		//Export
		Image chatPortrait = new Image(totalWidth, Font.CHAT.SIZE, portraitBuffer);
		return chatPortrait;
	}
	
	/** Retrieve the chat portrait of a username with no icon
	 * (Syntactic sugar for Font.CHAT.getStringImage(username...))
	 * 
	 * @param username - the username 
	 * @return a chat portrait for the username
	 */
	public static Image getChatPortrait(String username) {
		return Font.CHAT.getStringImage(username, ChatColor.USERNAME.color);
	}

	/** Initialize the chat line */
	public void init() {
		//Initialize the chatline image if it's not built already
			if (chatLineImage == null) {
				
				//Retrieve the chat portrait
				//(This is an image of the user's name (along with accompanying authority symbols)
				Image chatPortrait;
				if (ICON == ChatIcon.DEVELOPER.identifier) {
					chatPortrait = getChatPortrait(ChatIcon.DEVELOPER, NAME);
				} else if (ICON == ChatIcon.MODERATOR.identifier) {
					chatPortrait = getChatPortrait(ChatIcon.MODERATOR, NAME);
				} else {
					chatPortrait = getChatPortrait(NAME);
				}
				
				//Retrieve the message
				ChatMessageType type;
				if (CHAT_TYPE == ChatMessageType.IMPORTANT.identifier) {
					type = ChatMessageType.IMPORTANT;
				} else if (CHAT_TYPE == ChatMessageType.GAME.identifier) {
					type = ChatMessageType.GAME;
				} else if (CHAT_TYPE == ChatMessageType.PUBLIC.identifier) {
					type = ChatMessageType.PUBLIC;
				} else if (CHAT_TYPE == ChatMessageType.FRIEND.identifier) {
					type = ChatMessageType.FRIEND;
				} else {
					//Default: Use public chat
					type = ChatMessageType.PUBLIC;
				}
				Image messageImage = Font.CHAT.getStringImage(MESSAGE, type.text);
				
				//Calculate the total width we will need for our image
				int totalWidth = PADDING; //Padding
				totalWidth += chatPortrait.getWidth();
				totalWidth += PADDING;
				totalWidth += DELIMETER.getWidth();
				totalWidth += PADDING;
				totalWidth += messageImage.getWidth();
				
				int totalHeight = Font.CHAT.SIZE + 4; //Padding
				
				//Build the image
				int []chatLinePixelData = new int[totalWidth * totalHeight];
	
				//Draw borders
				for (int y = 0; y < totalHeight; y++) {
					chatLinePixelData[y * totalWidth] = type.border;
					chatLinePixelData[y * totalWidth + (totalWidth - 1)] = type.border; 
				}
				for (int x = 0; x < totalWidth; x++) {
					chatLinePixelData[x] = type.border;
					chatLinePixelData[(totalHeight - 1) * totalWidth  + x] = type.border; 
				}
				//Background shading
				for (int y = 1; y < totalHeight - 1; y++) {
					int yWidth = y * totalWidth;
					for (int x = 1; x < totalWidth - 1; x++) {
						chatLinePixelData[x + yWidth] = type.fill;
					}
				}
				
				// currentX will keep track of the +x offset as we move right
				// and add elements left to right on the chatline image
				int currentX = PADDING;
				
				//Copy user portrait image
				for (int y = 0; y < Math.min(chatPortrait.getHeight(), Font.CHAT.SIZE); y++) {
					int yPortraitWidth = y * chatPortrait.getWidth();
					int yChatLineWidth = (y + PADDING) * totalWidth;
					for (int x = 0; x < chatPortrait.getWidth(); x++) {
						int colorFromPortrait = chatPortrait.getPixels()[x + yPortraitWidth];
						//Don't copy transparent pixels
						if ((colorFromPortrait & 0xff000000) != 0) {
							chatLinePixelData[x + yChatLineWidth + currentX] = colorFromPortrait;
						}
						
					}
				}
				currentX += chatPortrait.getWidth() + PADDING;
				
				//Copy delimeter
				for (int y = 0; y < Math.min(DELIMETER.getHeight(), Font.CHAT.SIZE); y++) {
					int yDELIMETERWidth = y * DELIMETER.getWidth();
					int yChatLineWidth = (y + PADDING) * totalWidth;
					for (int x = 0; x < DELIMETER.getWidth(); x++) {
						int colorFromDelimeter = DELIMETER.getPixels()[x + yDELIMETERWidth];
						//Don't copy transparent pixels
						if ((colorFromDelimeter & 0xff000000) != 0) {
							chatLinePixelData[x + yChatLineWidth + currentX] = colorFromDelimeter;
						}
					}
				}
				currentX += DELIMETER.getWidth() + PADDING;
				
				//Copy message
				for (int y = 0; y < Math.min(messageImage.getHeight(), Font.CHAT.SIZE); y++) {
					int yMessageWidth = y * messageImage.getWidth();
					int yChatLineWidth = (y + 2) * totalWidth;
					for (int x = 0; x < messageImage.getWidth(); x++) {
						int colorFromMessage = messageImage.getPixels()[x + yMessageWidth];
						//Don't copy transparent pixels
						if ((colorFromMessage & 0xff000000) != 0) {
							chatLinePixelData[x + yChatLineWidth + currentX] = colorFromMessage;
						}
					}
				}
				
				this.chatLineImage = new Image(totalWidth, totalHeight, chatLinePixelData);
			}
	}

}
