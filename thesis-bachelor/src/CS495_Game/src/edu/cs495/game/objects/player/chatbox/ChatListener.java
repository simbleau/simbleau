/**
 * 
 */
package edu.cs495.game.objects.player.chatbox;

import java.text.ParseException;

/** A chat listener listens for chat dialogue and handles updates
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public interface ChatListener {

	/** Receive a new chat line from a {@link ChatProvider}
	 * 
	 * @param line - a chat lines to data
	 * @throws ParseException 
	 */
	public void push(ChatLine chatLine);
	
}
