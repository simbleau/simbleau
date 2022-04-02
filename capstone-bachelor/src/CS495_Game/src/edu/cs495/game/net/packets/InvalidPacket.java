package edu.cs495.game.net.packets;

/** An invalid packet for networking
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class InvalidPacket extends AbstractPacket {

	/** Initialize the invalid packet with no data (it doesn't need data) */
	public InvalidPacket() {
		this.type = PacketTypes.INVALID;
		this.contents = "";
	}
	
	/** Initialize an invalid packet with data (mainly for book-keeping/debug)
	 * 
	 * @param data - data for book keeping
	 */
	public InvalidPacket(String contents) {
		this.type = PacketTypes.INVALID;
		this.contents = contents;
	}

}
