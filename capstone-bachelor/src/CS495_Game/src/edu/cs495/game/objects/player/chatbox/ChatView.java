/**
 * 
 */
package edu.cs495.game.objects.player.chatbox;

import java.util.LinkedList;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Image;

/**
 * @author Spencer
 *
 */
public class ChatView extends AbstractGameObject{
	
	private static final int PADDING = 2;
	
	/** The maximum amount of messages shown at once */
	private static final int DISPLAY_MAX = 7;
	
	/** The lines of chat history to save */
	private static final int FULL_HISTORY_SIZE = 30;
	
	/** The parent chat console */
	protected ChatConsole parentConsole;
	
	/** Determines whether the view is pushed up because the chatbox is visible */
	private boolean pushedUp;
	
	/** The history of chat */
	protected LinkedList<ChatLine> chatHistory;
	
	/** The amount of messages we have received since initialization */
	protected int lifetimeMessages;
	
	/** Initialize the chat view 
	 * 
	 * @param parentConsole - the parent console of this textbox
	 */
	public ChatView(ChatConsole parentConsole) {
		super("(Chat view)", parentConsole.screenX, parentConsole.screenY, 0, 0);
		this.parentConsole = parentConsole;
		this.chatHistory = new LinkedList<>();
		this.lifetimeMessages = 0;
		this.pushedUp = false;
	}
	
	/** Add a chat line to our history and display it
	 * 
	 * @param line - a new line to the history
	 */
	public void pushChatLine(ChatLine line) {
		if (chatHistory.size() > FULL_HISTORY_SIZE) {
			chatHistory.remove(0);
			chatHistory.add(line);
		} else {
			chatHistory.add(line);
		}
	}
	
	/** Toggle whether the chatview is pushed up
	 *
	 * @param value - true if the chatview should be pushed up, false otherwise
	 */
	public void togglePushup(boolean value) {
		this.pushedUp = value;
	}
	

	/** Returns whether the chatview is pushed up
	 * 
	 * @return true if the chatview is pushed up, false otherwise
	 */
	public boolean isPushedUp() {
		return pushedUp;
	}


	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		if (pushedUp) {
			this.posY = parentConsole.getPosY() - parentConsole.chatTextbox.getHeight();
		} else {
			this.posY = parentConsole.getPosY();
		}
		this.chatHistory.clear();
	}


	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#update(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void update(GameDriver gameDriver) {	}


	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#render(edu.cs495.engine.GameDriver, edu.cs495.engine.gfx.Renderer)
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		int linesToDisplay = Math.min(chatHistory.size(), DISPLAY_MAX);
		int currentY;
		if (pushedUp) {
			currentY = parentConsole.screenY - parentConsole.chatTextbox.getHeight();
		} else {
			currentY = parentConsole.screenY;
		}
		
		if (parentConsole.isTyping()) {
			for (int i = chatHistory.size() - 1; i >= (chatHistory.size() - linesToDisplay); i--) {
				Image chatLineImage = chatHistory.get(i).getChatLineImage();
				currentY -= chatLineImage.getHeight() + PADDING;
				renderer.drawOverlay(chatLineImage, Integer.MAX_VALUE, parentConsole.screenX, currentY); 
			}
		} else {
			if (chatHistory.size() > 0) {
				currentY -= chatHistory.get(0).getChatLineImage().getHeight();
			}
			for (int i = chatHistory.size() - 1; i >= (chatHistory.size() - linesToDisplay); i--) {
				ChatLine chatLine = chatHistory.get(i);
				if ((chatLine.TIME_SENT + chatLine.TTL) - gameDriver.getGameTime() > 0) {
					Image chatLineImage = chatLine.getChatLineImage();
					renderer.drawOverlay(chatLineImage, Integer.MAX_VALUE, parentConsole.screenX, currentY); 
					currentY -= chatLineImage.getHeight() + PADDING;
				}
			}
		}
	}


}
