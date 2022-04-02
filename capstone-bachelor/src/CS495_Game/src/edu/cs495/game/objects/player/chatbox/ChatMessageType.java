/**
 * 
 */
package edu.cs495.game.objects.player.chatbox;

/** Chat message types
 * 
 * @version February 2019
 * @author Spencer
 */
public enum ChatMessageType {
	
	/** The friends chat color and identifier */
	IMPORTANT(0xffff0000, 0xb0400000, 0xb0400000, 0), //Red
	
	/** The game chat color and identifier */
	GAME(0xff00ffff, 0xb0004040, 0xb0004040, 1), //Cyand
	
	/** The public chat color and identifier */
	PUBLIC(0xffffff00, 0xb0404000, 0xb0404000, 2), //Yellow
	
	/** The friend chat color and identifier */
	FRIEND(0xff00ff00, 0xb0004000, 0xb0004000, 3); //Green
	
	/** The text color of this message type (ARGB) */
	public final int text;
	
	/** The border color of this message type (ARGB) */
	public final int border;
	
	/** The fill color of this message type (ARGB) */
	public final int fill;
	
	/** the byte int which identifies a type of chat message.
	 * i.e. a message such as "0Server:Hi", given the first character is
	 * indexed to determine the chat message type, would identify an
	 * {@link #IMPORTANT} message saying "Server:Hi". 
	 */
	public final int identifier;
	
	/** Enum ChatMessageType describes the networking identifiers and color of each
	 * chat message type in game in the {@link ChatConsole}
	 * 
	 * @param color - the color of the incoming text in ARGB format
	 * @param identifier - the byte identifier to signal this color
	 */
	private ChatMessageType(int textARGB, int borderARGB, int fillARGB, int identifier) {
		this.text = textARGB;
		this.border = borderARGB;
		this.fill = fillARGB;
		this.identifier = identifier;
	}
	
}
