package edu.cs495.game.net.packets;

/** A negative acknowledgement packet
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class NegAckPacket extends AbstractPacket {
	
	public NegAckPacket(String contents) {
		this.type = PacketTypes.NEG_ACK;
		this.contents = contents;
	}

}
