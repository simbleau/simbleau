package edu.cs495.game.net.packets;

/**
 * A player update packet for networking
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class PlayerUpdatePacket extends AbstractPacket {

	public PlayerUpdatePacket(String contents) {
		this.type = PacketTypes.PLAYER_UPDATE;
		this.contents = contents;
	}

}
