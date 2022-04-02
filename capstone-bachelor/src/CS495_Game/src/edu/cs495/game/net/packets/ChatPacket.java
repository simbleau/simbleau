package edu.cs495.game.net.packets;

import java.net.InetAddress;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.game.net.GameNetwork;
import edu.cs495.game.objects.player.chatbox.ChatLine;

/**
 * An chat packet for networking
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class ChatPacket extends ReliablePacket {

	/**
	 * The number of values that should exist in this packet Respectively being the
	 * values for a chatline
	 */
	public static final int NUM_VALUES = 4;

	/** The chat line contained in this packet */
	private ChatLine chatLine;

	/**
	 * Initialize a chat packet
	 * 
	 * @param chatLine - the chat line to create this packet from
	 */
	public ChatPacket(GameNetwork network, InetAddress toIP, int toPort, ChatLine chatLine) {
		super(network, toIP, toPort);

		this.chatLine = chatLine;

		StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append(chatLine.ICON);
		dataBuilder.append(chatLine.CHAT_TYPE);
		dataBuilder.append(chatLine.TTL / GameDriver.NANO_SECOND);
		dataBuilder.append(AbstractPacket.CONTENT_DELIMETER);
		dataBuilder.append(chatLine.TIME_SENT);
		dataBuilder.append(AbstractPacket.CONTENT_DELIMETER);
		dataBuilder.append(chatLine.NAME);
		dataBuilder.append(AbstractPacket.CONTENT_DELIMETER);
		dataBuilder.append(chatLine.MESSAGE);
		this.contents = dataBuilder.toString();

		this.type = PacketTypes.CHAT;

		DeveloperLog.printLog("ChatPacket data built: '" + contents + "'  (" + hashCode() + ")");
	}

	/**
	 * Initialize a chat packet with passed contents
	 * 
	 * @param contents - the data of a chatline
	 */
	public ChatPacket(String contents) {
		super(contents);

		if (this.type != PacketTypes.INVALID) {
			try {
				String[] values = this.contents.split(AbstractPacket.CONTENT_DELIMETER);

				if (values.length == NUM_VALUES) {
					// Capture icon, message type, and time to live
					int icon = values[0].charAt(0) - '0';
					int chatType = values[0].charAt(1) - '0';
					int ttl = Integer.parseInt(values[0].substring(2));
					// Capture time sent
					long timeSentLong = Long.parseLong(values[1]);
					// Capture username
					String username = values[2];
					// Capture message
					String message = values[3];

					// Set chatline
					ChatLine chatLine = new ChatLine(icon, chatType, ttl, timeSentLong, username, message);
					chatLine.init();

					this.chatLine = chatLine;
					this.type = PacketTypes.CHAT;

					DeveloperLog.printLog("ChatPacket data read: '" + this.contents + "'  (" + hashCode() + ")");
				} else {
					this.type = PacketTypes.INVALID;
				}
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				this.type = PacketTypes.INVALID;
			}
		}
	}
	
	/** Return the chatline contained in this packet
	 * 
	 * @return the chatline contained in this packet
	 */
	public ChatLine getChatLine() {
		return this.chatLine;
	}

}
