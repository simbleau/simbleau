/**
 * 
 */
package edu.cs495.game.objects.player.chatbox;

import java.awt.event.KeyEvent;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.input.Input;
import edu.cs495.game.objects.player.LocalPlayer;
import edu.cs495.game.objects.player.Player;

/** A chatbox for the game
 * 
 * @version March 2019
 * @author Spencer Imbleu
 */
public class ChatConsole extends AbstractGameObject implements ChatListener {
	
	/** Padding between chat line items (such as the delimeter, message, username) except the icon */
	static final int PADDING = 2;
	
	/** Splits items in chat line data */
	static final int ITEM_DELIMETER = '\0';
	
	/** The game driver for this chat console */
	protected GameDriver gameDriver;
	
	/** The X coordinate (local to screen, as the chat is an overlay) */
	protected int screenX;
	
	/** The Y coordinate (local to screen, as the chat is an overlay) */
	protected int screenY;
	
	/** The amount of time to show the textview */
	protected float timeToShow;
	
	/** Whether or not to display the textbox */
	protected boolean displayTextbox;
	
	/** The chat user for this console */
	protected LocalPlayer chatUser;
	
	/** The view of chat for the player */
	protected ChatView chatView;
	
	/** The textbox a user can use to type in chat */
	protected ChatTextbox chatTextbox;
	
	/** The chatline interpreter (runs commands sent to the chatbox) */
	protected ChatLineInterpreter chatLineInterpreter;

	/** Initialize a chat console
	 * 
	 * @param consoleX
	 * @param consoleY
	 */
	public ChatConsole(int screenX, int screenY, LocalPlayer chatUser) {
		super("(Chat)", screenX, screenY, 0, 0);
		this.screenX = screenX;
		this.screenY = screenY;
		this.gameDriver = null;
		this.displayTextbox = false;
		this.chatUser = chatUser;
		this.chatTextbox = new ChatTextbox(this);
		this.chatView = new ChatView(this);
		this.chatLineInterpreter = new ChatLineInterpreter(this);
	}

	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		this.gameDriver = gameDriver;
		chatTextbox.init(gameDriver);
		chatView.init(gameDriver);
		
		pushImportantMessage("Welcome to the game!");
		pushGameMessage("This is a game message!");
	}

	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#update(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void update(GameDriver gameDriver) {
		// Toggle chat console
		Input localInput = chatUser.getInput();
		if (localInput.isKeyDown(KeyEvent.VK_TAB)) {
			if (isTyping()) {
				closeChatTextbox();
			} else {
				openChatTextbox();
			}
		}
		
		if (isTyping()) {
			if (chatUser.canMove()) {
			chatUser.disableMovement();
			}
			//Display the textbox and the chatview pushed up
			chatTextbox.update(gameDriver);
			if (!chatView.isPushedUp()) {
				chatView.togglePushup(true);
			}
		} else {
			if (!chatUser.canMove()) {
			chatUser.enableMovement();
			}
			//Display the chatview pushed down since the textbox isn't visible
			if (chatView.isPushedUp()) {
				chatView.togglePushup(false);
			}
		}
		chatView.update(gameDriver);
	}

	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#render(edu.cs495.engine.GameDriver, edu.cs495.engine.gfx.Renderer)
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		
		if (isTyping()) { 
			chatTextbox.render(gameDriver, renderer);
		}
		
		chatView.render(gameDriver, renderer);
	}
	
	/** Push an important message to the chat view
	 * 
	 * @param message - the message to push
	 */
	public void pushImportantMessage(String message) {
		ChatLine processingLine = new ChatLine(
				ChatIcon.NONE.identifier, 
				ChatMessageType.IMPORTANT.identifier, 
				ChatLine.DEFAULT_TTL, 
				gameDriver.getGameTime(), 
				"IMPORTANT", 
				message);
		chatView.pushChatLine(processingLine);
	}
	
	/** Push a public message to the chat view
	 * 
	 * @param player - the player who talked
	 * @param message - the message to push
	 */
	public void pushPublicMessage(Player player, String message) {
		ChatLine processingLine = new ChatLine(
				player.getChatIcon().identifier, 
				ChatMessageType.PUBLIC.identifier, 
				ChatLine.DEFAULT_TTL, 
				gameDriver.getGameTime(), 
				player.getConfig().getUsername(), 
				message);
		chatView.pushChatLine(processingLine);
	}
	
	/** Push a friendly message to the chat view
	 * 
	 * @param player - the player who talked
	 * @param message - the message to push
	 */
	public void pushFriendMessage(Player player, String message) {
		ChatLine processingLine = new ChatLine(
				player.getChatIcon().identifier, 
				ChatMessageType.FRIEND.identifier, 
				ChatLine.DEFAULT_TTL, 
				gameDriver.getGameTime(), 
				player.getConfig().getUsername(), 
				message);
		chatView.pushChatLine(processingLine);
	}
	
	
	/** Push a game message to the chat view
	 * 
	 * @param player - the player who talked
	 * @param message - the message to push
	 */
	public void pushGameMessage(String message) {
		ChatLine processingLine = new ChatLine(
				ChatIcon.NONE.identifier, 
				ChatMessageType.GAME.identifier, 
				ChatLine.DEFAULT_TTL, 
				gameDriver.getGameTime(), 
				"GAME", 
				message);
		chatView.pushChatLine(processingLine);
	}

	/** Receive a chatline to push to the chat view
	 * 
	 * @param chatLine - the chatLine to push
	 */
	public void push(ChatLine chatLine) {
		chatView.pushChatLine(chatLine);
	}
	
	/** Returns whether the player is typing (the textbox is visible)
	 * 
	 * @return true if the player is typing, false otherwise
	 */
	public boolean isTyping() {
		return displayTextbox;
	}
	
	/** Open the chat textbox */
	public void openChatTextbox() {
		displayTextbox = true;
	}
	
	/** Close the chat textbox */
	public void closeChatTextbox() {
		displayTextbox = false;
	}

	/** Clear the chat history */
	public void clearChat() {
		chatView.chatHistory.clear();
	}
	
	/** Set the console's X position (relative to screen)
	 *
	 * @param consoleX - a new position X relative to the screen
	 */
	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}
	
	/** Set the console's Y position (relative to screen)
	 *
	 * @param consoleY - a new position X relative to the screen
	 */
	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}
}
