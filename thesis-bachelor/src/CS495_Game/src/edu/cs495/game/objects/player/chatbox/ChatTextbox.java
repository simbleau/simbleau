/**
 * 
 */
package edu.cs495.game.objects.player.chatbox;

import java.awt.event.KeyEvent;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Font;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.game.objects.player.LocalPlayer;

/** A chat textbox line to type in
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class ChatTextbox extends AbstractGameObject  {
	
	/** The color of text in the textbox */
	private static final int TEXT_COLOR = 0xffffffff;
	
	/** The color of the border of the textbox */
	private static final int BORDER_COLOR =  0xff2f2a2a;
	
	/** The color of the background of the textbox */
	private static final int BG_COLOR = 0xff323232;

	/** The amount of characters the chat textbox holds */
	private static final int CHAT_BUFFER_SIZE = 25;
	
	/** The width of the textbox */
	private static final int TEXTBOX_WIDTH = 200;
	
	/** The text cursor */
	private static final String CURSOR = "*";
	/** The image of the cursor */
	private static final Image CURSOR_VISIBLE = Font.CHAT.getStringImage(CURSOR, TEXT_COLOR);
	/** The image of the cursor when it's invisible */
	private static final Image CURSOR_INVISIBLE = Font.CHAT.getStringImage(CURSOR, 0);

	/** The background of the textbox image */
	private Image textboxBackgroundImage;
	
	/** The image of text in the buffer before the cursor */
	private Image beforeText;
	
	/** The image of text in the buffer after the cursor */
	private Image afterText;
	
	/** The chatline portrait for this textbox */
	private Image chatLinePortrait;
	
	/** The user authenticated for this chat textbox */
	private LocalPlayer chatUser;
	
	/** The chat text buffer itself */
	private char[] chatBuffer;
	
	/** The index of our text cursor location */
	private int chatPosition;
	
	/** The frame which dictates whether or not to display the cursor (blink) */
	private int frame;
	
	/** The chatview which we push our own text to */
	private ChatConsole parentConsole;
	
	/** Initialize the chat textbox */
	public ChatTextbox(ChatConsole parentConsole) {
		super("(Chat Textbox)", parentConsole.screenX, parentConsole.screenY, 0, 0);
		this.parentConsole = parentConsole;
		this.chatBuffer = new char[CHAT_BUFFER_SIZE];
		this.chatPosition = 0;
		this.chatUser = parentConsole.chatUser;
		this.textboxBackgroundImage = null;
		this.chatLinePortrait = null;
		this.beforeText = null;
		this.afterText = null;
		this.frame = 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#init(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void init(GameDriver gameDriver) {
		//Init chat portrait 
		if (this.chatUser == null) {
			this.chatLinePortrait = ChatLine.getChatPortrait(">");
		} else {
			switch (chatUser.getConfig().getPrivilege()) {
			case ADMIN:
				this.chatLinePortrait = 
				ChatLine.getChatPortrait(ChatIcon.DEVELOPER, chatUser.getConfig().getUsername());
				break;
			case MODERATOR:
				this.chatLinePortrait = 
				ChatLine.getChatPortrait(ChatIcon.MODERATOR, chatUser.getConfig().getUsername());
				break;
			default:
				this.chatLinePortrait = ChatLine.getChatPortrait(chatUser.getConfig().getUsername());
				break;
			}
		}
		
		//Init background
		initTextBoxBackgroundImage();
		
		//Build textbox buffer string images
		buildTextboxFrames();
		
		//Update width and height
		this.width = textboxBackgroundImage.getWidth();
		this.height = textboxBackgroundImage.getHeight();
	}

	/** Initialize the background for the textbox 
	 * 
	 */
	private void initTextBoxBackgroundImage() {
		//Make textbox background
		int[] pixelData = new int[TEXTBOX_WIDTH * (Font.CHAT.SIZE + ChatLine.PADDING * 2)];
	
		//Draw borders
		for (int y = 0; y < Font.CHAT.SIZE + (ChatLine.PADDING * 2); y++) {
			pixelData[y * TEXTBOX_WIDTH] = BORDER_COLOR;
			pixelData[y * TEXTBOX_WIDTH + (TEXTBOX_WIDTH - 1)] = BORDER_COLOR; 
		}
		for (int x = 0; x < TEXTBOX_WIDTH; x++) {
			pixelData[x] = BORDER_COLOR;
			pixelData[(Font.CHAT.SIZE + (ChatLine.PADDING * 2) - 1) * TEXTBOX_WIDTH  + x] = BORDER_COLOR; 
		}
		//Fill background
		for (int y = 1; y < Font.CHAT.SIZE + 3; y++) {
			int yWidth = y * TEXTBOX_WIDTH;
			for (int x = 1; x < TEXTBOX_WIDTH - 1; x++) {
				pixelData[yWidth + x] = BG_COLOR;
			}
		}
				
		this.textboxBackgroundImage = new Image(TEXTBOX_WIDTH, Font.CHAT.SIZE + (ChatLine.PADDING * 2), pixelData);
	}

	/** Pack the chat buffer into two images, before and after the chat position */
	private void buildTextboxFrames() {
		StringBuilder bufferString = new StringBuilder();
		for (int i = 0; i < CHAT_BUFFER_SIZE; i++) {
			if (chatBuffer[i] != 0) {
				bufferString.append(chatBuffer[i]);
			}
		}
		
		String before = bufferString.substring(0, chatPosition);
		String after = bufferString.substring(chatPosition, bufferString.length());
		
		this.beforeText = Font.CHAT.getStringImage(before, TEXT_COLOR);
		this.afterText = Font.CHAT.getStringImage(after, TEXT_COLOR);
	}


	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#update(edu.cs495.engine.GameDriver)
	 */
	@Override
	public void update(GameDriver gameDriver) {
		this.posX = parentConsole.getPosX();
		this.posY = parentConsole.getPosY();
		
		this.frame = (int) ((System.nanoTime() / GameDriver.NANO_SECOND) % 2);
		
		if (chatUser.getInput().isKeyDown(KeyEvent.VK_BACK_SPACE)) {
			backspaceBuffer();
		} else if (chatUser.getInput().isKeyDown(KeyEvent.VK_LEFT)) {
			leftBuffer();
		} else if (chatUser.getInput().isKeyDown(KeyEvent.VK_RIGHT)) {
			rightBuffer();
		} else if (chatUser.getInput().isKeyDown(KeyEvent.VK_ENTER)) {
			sendBuffer();
		} else {
			for (int i = 32; i < 127; i++) {
				if (chatUser.getInput().isKeyDown(i)) {
					char newChar = getChar(i, chatUser.getInput().isKeyActive(KeyEvent.VK_SHIFT));
					appendBuffer(newChar);
				}
			}
			//These characters needs to be listened to although not within normal bounds
			if (chatUser.getInput().isKeyDown(192)) { //This identifies ` and ~
				char newChar = getChar(192, chatUser.getInput().isKeyActive(KeyEvent.VK_SHIFT));
				appendBuffer(newChar);
			} 
			if (chatUser.getInput().isKeyDown(222)) { //This identifies ' and "
				char newChar = getChar(222, chatUser.getInput().isKeyActive(KeyEvent.VK_SHIFT));
				appendBuffer(newChar);
			}
		}
	}
	

	/* (non-Javadoc)
	 * @see edu.cs495.engine.game.AbstractGameObject#render(edu.cs495.engine.GameDriver, edu.cs495.engine.gfx.Renderer)
	 */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		renderer.drawOverlay(textboxBackgroundImage, Integer.MAX_VALUE, 
				parentConsole.screenX, parentConsole.screenY - textboxBackgroundImage.getHeight());
		
		int currentX = parentConsole.screenX + ChatLine.PADDING;
		int midY = parentConsole.screenY - textboxBackgroundImage.getHalfHeight();
		//Chat portrait
		renderer.drawOverlay(chatLinePortrait, Integer.MAX_VALUE, 
				currentX, midY - chatLinePortrait.getHalfHeight());
		currentX += chatLinePortrait.getWidth() + ChatLine.PADDING;
		
		//Delimeter
		renderer.drawOverlay(ChatLine.DELIMETER, Integer.MAX_VALUE, 
				currentX, midY - ChatLine.DELIMETER.getHalfHeight());
		currentX += ChatLine.DELIMETER.getWidth() + ChatLine.PADDING;
		
		//Before text
		renderer.drawOverlay(beforeText, Integer.MAX_VALUE, 
				currentX, midY - beforeText.getHalfHeight());
		currentX += beforeText.getWidth();
		
		//Chat position
		switch (frame) {
		case 0:
			renderer.drawOverlay(CURSOR_VISIBLE, Integer.MAX_VALUE, 
					currentX, midY - CURSOR_VISIBLE.getHalfHeight());
			currentX += CURSOR_VISIBLE.getWidth();
			break;
		case 1:
			currentX += CURSOR_INVISIBLE.getWidth();
			break;
		}
		
		//after text
		renderer.drawOverlay(afterText, Integer.MAX_VALUE, 
				currentX, midY - afterText.getHalfHeight());
		currentX += afterText.getWidth();
	}
	
	/** Converts the key event to a formatted character for the textbox.
	 * Java unfortunately uses unicode char values but the keyboard keys come in
	 * ascii value, so it's a pain to cleanse the input. I didn't find it
	 * worthwhile to set up constants for all the magic numbers that ensue,
	 * so tread carefully if editing this code.
	 * 
	 * @param keyCode - the keycode in ASCII int index
	 * @param shiftDown - whether the shift is down
	 * @return the converted character from ASCII index and shift
	 */
	public static char getChar(int keyCode, boolean shiftDown) {
		//Numerics
		if (keyCode >= 48 && keyCode <= 57) {
			if (!shiftDown) {
				//Parse the number
				return (char) ((keyCode - 48) + 0x30);
			} else {
				//Parse the character
				if (keyCode == 49) {
					return '!';
				} else if (keyCode == 50) {
					return '@';
				} else if (keyCode == 51) {
					return '#';
				} else if (keyCode == 52) {
					return '$';
				} else if (keyCode == 53) {
					return '%';
				} else if (keyCode == 54) {
					return '^';
				} else if (keyCode == 55) {
					return '&';
				} else if (keyCode == 56) {
					return '*';
				} else if (keyCode == 57) {
					return '(';
				} else if (keyCode == 48) {
					return ')';
				} else return ' ';
			}
		} 
		//Letters
		else if (keyCode >= 65 && keyCode <= 90) {
			if (shiftDown) {
				//Uppercase
				return (char) ((keyCode - 65) + 0x41);
			} else {
				//Lowercase
				return (char) ((keyCode - 65) + 0x61);	
			}
		}
		// ~, `
		else if (keyCode == 192) {
			if (shiftDown) {
				return '~';
			} else {
				return '`';
			}
		}
		// -, _
		else if (keyCode == 45) {
			if (shiftDown) {
				return '_';
			} else {
				return '-';
			}
		}
		// +, =
		else if (keyCode == 61) {
			if (shiftDown) {
				return '+';
			} else {
				return '=';
			}
		}
		// {, [
		else if (keyCode == 91) {
			if (shiftDown) {
				return '{';
			} else {
				return '[';
			}
		}
		// }, ]
		else if (keyCode == 93) {
			if (shiftDown) {
				return '}';
			} else {
				return ']';
			}
		}
		// \, |
		else if (keyCode == 92) {
			if (shiftDown) {
				return '|';
			} else {
				return '\\';
			}
		}
		// ;, :
		else if (keyCode == 59) {
			if (shiftDown) {
				return ':';
			} else {
				return ';';
			}
		}
		// ', "
		else if (keyCode == 222) {
			if (shiftDown) {
				return '"';
			} else {
				return '\'';
			}
		}
		// <, ,
		else if (keyCode == 44) {
			if (shiftDown) {
				return '<';
			} else {
				return ',';
			}
		}
		// >, .
		else if (keyCode == 46) {
			if (shiftDown) {
				return '>';
			} else {
				return '.';
			}
		}
		// ?, /
		else if (keyCode == 47) {
			if (shiftDown) {
				return '?';
			} else {
				return '/';
			}
		}
		else return ' '; //Invalid char
		
	
	}


	/** Append a character to the text buffer, or do nothing if it's full
	 * 
	 * @param next - the next character to append
	 */
	public void appendBuffer(char next) {
		if (chatPosition < CHAT_BUFFER_SIZE) {
			chatBuffer[chatPosition] = next;
			chatPosition++;	
			buildTextboxFrames();
		}
	}
	
	/** Attempt to move the chat position left, or do nothing if out of bounds */
	public void leftBuffer() {
		if (chatPosition > 0) {
			chatPosition--;
			buildTextboxFrames();
		}
	}
	
	/** Attempt to move the chat position right, or do nothing if out of bounds */
	public void rightBuffer() {
		if (chatPosition < CHAT_BUFFER_SIZE
				&& chatPosition < getBufferString().length()) {
			chatPosition++;
			buildTextboxFrames();
		}
	}
	
	/** Attempt to backspace at the chat position, or do nothing if out of bounds */
	public void backspaceBuffer() {
		if (chatPosition > 0) {
			chatPosition--;
			chatBuffer[chatPosition] = '\0'; //Null the current value
			buildTextboxFrames();
		}
	}
	
	/** Send the chat buffer to the chatview and flush the buffer 
	 * TODO send it to the server to be sent to other players from here later
	 * */
	public void sendBuffer() {
		ChatLine line = retrieveLine();
		parentConsole.push(line);
		parentConsole.chatLineInterpreter.interpret(line);
		flushBuffer();
	}
	
	/** Flush the chat buffer and empty it out */
	public void flushBuffer() {
		//Reset chat buffer
		this.chatPosition = 0;
		for (int i = 0; i < CHAT_BUFFER_SIZE; i++) {
			chatBuffer[i] = '\0'; //Nullify all characters
		}
		
		//Rebuild textbox string images
		buildTextboxFrames();
	}
	
	/** Pack the chat buffer contents into a {@link ChatLine} object
	 * 
	 * @return the ChatLine model of the chat buffer contents
	 */
	public ChatLine retrieveLine() {
		String bufferString = getBufferString();
		
		int icon = 0;
		switch (chatUser.getConfig().getPrivilege()) {
		case ADMIN:
			icon = ChatIcon.DEVELOPER.identifier;
			break;
		case MODERATOR:
			icon = ChatIcon.MODERATOR.identifier;
			break;
		default:
			break;
		}
		
		int type = ChatMessageType.PUBLIC.identifier;
		
		ChatLine line = new ChatLine(icon, type, ChatLine.DEFAULT_TTL, parentConsole.gameDriver.getGameTime(), chatUser.getConfig().getUsername(), bufferString);
		return line;
	}
	
	/** Evaluate the chat buffer to determine the characters within
	 * 
	 * @return a string representing the chatbuffer :)
	 */
	private String getBufferString() {
		StringBuilder bufferString = new StringBuilder();
		for (int i = 0; i < CHAT_BUFFER_SIZE; i++) {
			if (chatBuffer[i] != 0) {
				bufferString.append(chatBuffer[i]);
			}
		}
		
		return bufferString.toString();
	}

}
