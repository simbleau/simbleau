package edu.cs495.game.net.packets;

/**
 * An removal packet for networking
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class RemovePacket extends AbstractPacket {

	public RemovePacket(String contents) {
		this.type = PacketTypes.SPAWN;
		this.contents = contents;
	}
}
