package edu.cs495.game.net.packets;

import java.net.InetAddress;

import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.game.net.GameNetwork;

/**
 * An logout packet for networking
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class LogoutPacket extends ReliablePacket {

	/** The username of the player logging out */
	private String username;
	
	/**
	 * Initialize a logout packet by parsing content data
	 * 
	 * @param contents - the contents in this packet
	 */
	public LogoutPacket(String contents) {
		super(contents);
		
		if (this.type != PacketTypes.INVALID) {
			this.type = PacketTypes.LOGOUT;
			this.username = this.contents;
			DeveloperLog.printLog("LogoutPacket data read: '" + this.contents + "'  (" + hashCode() + ")");
		}
	}
	
	/**
	 * Create a logout packet from parameters
	 * 
	 * @param server - the server which will wait for this reliable packet
	 * @param config - the configuration of a new player
	 * @see ReliablePacket
	 */
	public LogoutPacket(GameNetwork network, InetAddress toIP, int toPort, String username) {
		super(network, toIP, toPort);

		this.type = PacketTypes.LOGOUT;		
		this.username = this.contents = username;
		
		DeveloperLog.printLog("LoginPacket data built: '" + this.contents + "'  (" + hashCode() + ")");
	}
	
	public String getUsername() {
		return this.username;
	}

}
