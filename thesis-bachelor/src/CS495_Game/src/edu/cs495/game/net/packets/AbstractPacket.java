package edu.cs495.game.net.packets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.game.net.GameClient;
import edu.cs495.game.net.GameNetwork;

/** An abstract packet for networking
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public abstract class AbstractPacket {
	
	/** The maximum size of a packet */
	public static final int MTU = 1500;
	
	/** The character which breaks up values */
	public static final String CONTENT_DELIMETER = "/";
	
	/** The amount of digits which occupy the identifier */
	public static final int IDENTIFIER_LENGTH = 2;
	
	/** The type of packet */
	protected PacketTypes type;
	
	/** The contents of the packet */
	protected String contents;

	/** Parse a packet into its respective type
	 * 
	 * @param data - the data for the packet
	 * @return an initialized packet
	 */
	public static AbstractPacket parsePacket(byte[] data) {
		String message = stringify(data);
		if (message.length() < IDENTIFIER_LENGTH) {
			return new InvalidPacket();
		}
		
		DeveloperLog.printLog("Parsing packet message: '" + message + "'");
		
		String identifier = message.substring(0, IDENTIFIER_LENGTH);
		String packetContents = message.substring(IDENTIFIER_LENGTH);
		
		PacketTypes packetType = AbstractPacket.getPacketType(identifier);
		
		switch (packetType) {
		
		case ACK:
			return new AckPacket(packetContents);
		case NEG_ACK:
			return new NegAckPacket(packetContents);
		case SERVER_END:
			return new ServerEndPacket(packetContents);
		case LOGIN:
			return new LoginPacket(packetContents);
		case LOGOUT:
			return new LogoutPacket(packetContents);
		case CHAT:
			return new ChatPacket(packetContents);
		case PLAYER_UPDATE:
			return new PlayerUpdatePacket(packetContents);
		case SPAWN:
			return new SpawnPacket(packetContents);
		case REMOVE:
			return new RemovePacket(packetContents);
		case INVALID:
			return new InvalidPacket(packetContents);
		}
		
		return new InvalidPacket(packetContents);
	}
	
	/** Parse the packet id string to return a {@link PacketTypes}
	 * 
	 * @param packetIdString - a string representing the packet identifier
	 * @return
	 */
	private static PacketTypes getPacketType(String packetIdString) {
		try {
			int packetIdentifier = Integer.parseInt(packetIdString);
			for (PacketTypes packetType : PacketTypes.values()) {
				if (packetType.identifier == packetIdentifier) {
					return packetType;
				}
			}
		} catch (NumberFormatException nfe) {
			DeveloperLog.errLog("PacketTypeLookupException: " + nfe.getMessage());
		}
		return PacketTypes.INVALID;
	}
	
	/** Return the packet type of this packet
	 * 
	 * @return the packet type of this packet
	 */
	public PacketTypes getPacketType() {
		return this.type;
	}
	
	/** Return the contents of the packet
	 * 
	 * @return the contents sent in the packet
	 */
	public String getContents() {
		return this.contents;
	}
	
	/** Return the entire bytes of this packet
	 * 
	 * @return the bytes of this packet
	 */
	public byte[] getData() {
		StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append(String.format("%0" + IDENTIFIER_LENGTH + "d", this.type.identifier));
		dataBuilder.append(contents);
		
		byte[] dataBytes = dataBuilder.toString().getBytes();
		return dataBytes;
	}
	
	/** Send this packet to a network
	 * 
	 * @param network - the network to send this packet to
	 * @throws IOException 
	 */
	public void send(GameNetwork network) throws IOException {
		network.send(this);
	}
	
	/** Helper method which sends a packet to a destination
	 * 
	 * @param ipAddress - the IP to send to
	 * @param port - the port to send to
	 * @param network - the network to send from
	 * @throws IOException 
	 */
	public void sendTo(InetAddress ipAddress, int port, GameNetwork network) throws IOException {
		byte[] data = getData();
		DatagramPacket datagram = new DatagramPacket(data, data.length);
		
		datagram.setAddress(ipAddress);
		datagram.setPort(port);
		
		DeveloperLog.printLog(
				((network instanceof GameClient) ? "Client" : "Server") 
				+ " is sending a " + type.name() + " packet to " + ipAddress.getHostAddress() + ":" + port
				+ " --> '" + this.contents + "' (" + hashCode() + ")");
		network.getSocket().send(datagram);
	}

	/** Generate and return a hash code checksum for this packet
	 * 
	 * @return a hash code checksum for this packet
	 */
	@Override
	public int hashCode() {
		return getContents().hashCode();
	}

	
	
	/** Read the data of a packet
	 * 
	 * @param data - the bytes in a packet
	 * @return the data within the packet as a string
	 */
	private static String stringify(byte[] data) {
		String message = new String(data).trim();
		if (message.isEmpty()) {
			return "";
		} else {
			return message;	
		}
	}
}
