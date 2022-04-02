/**
 * 
 */
package edu.cs495.game.net.packets;

import java.net.InetAddress;

import edu.cs495.game.net.GameServer; 

/** A negative acknowledgement packet
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class ServerEndPacket extends ReliablePacket {
	
	/** Initialize a server end packet */
	public ServerEndPacket(String contents) {
		super(contents);
		
		if (this.type != PacketTypes.INVALID) {
			this.type = PacketTypes.SERVER_END;
		}
	}

	/** Create a server end packet
	 * 
	 * @param server - the server being ended
	 */
	public ServerEndPacket(GameServer server, InetAddress toIP, int toPort) {
		super(server, toIP, toPort);
		this.type = PacketTypes.SERVER_END;
		//Make the contents some RNG string so that we have something unique
		//to checksum for data security
		this.contents = "" + server.getGame().getDriver().getRNG().nextLong();
	}

}
