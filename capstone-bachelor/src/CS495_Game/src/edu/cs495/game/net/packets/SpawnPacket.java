package edu.cs495.game.net.packets;

/**
 * An spawn packet for networking
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class SpawnPacket extends AbstractPacket {

	public SpawnPacket(String contents) {
		this.type = PacketTypes.SPAWN;
		this.contents = contents;
	}

}
