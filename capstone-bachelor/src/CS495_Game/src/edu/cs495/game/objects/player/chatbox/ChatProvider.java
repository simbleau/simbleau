/**
 * 
 */
package edu.cs495.game.objects.player.chatbox;

import java.util.ArrayList;
import java.util.List;

/** This is the Observer design pattern class for the Subject in the relationship
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class ChatProvider {
	
	/** The listeners for chat triggers */
	public List<ChatListener> listeners;
	
	/** Initialize the list of listeners */
	public ChatProvider() {
		listeners = new ArrayList<>();
	}
	
	/** Clear the listeners */
	public void clearListeners() {
		this.listeners.clear();
	}
	
	/** Add a chat listener who gets notified
	 * 
	 * @param listener - a new chat listener
	 */
	public void addChatListener(ChatListener listener) {
		this.listeners.add(listener);
	}
	
	/** Remove a chat listener
	 * 
	 * @param listener - the listener object
	 */
	public void removeChatListener(ChatListener listener) {
		this.listeners.remove(listener);
	}
	
	/** Remove a chat listener
	 * 
	 * @param index - the index of the listener to remove
	 */
	public void removeChatListener(int index) {
		this.listeners.remove(index);
	}
	
	/** Notify all chat listeners of a new chat line
	 * 
	 * @param chatLine - the chat line to send
	 */
	public void notifyListeners(ChatLine chatLine) {
		for (int i = 0; i < listeners.size(); i++) {
			ChatListener listener = listeners.get(i);
			listener.push(chatLine);
		}
	}
	

}
