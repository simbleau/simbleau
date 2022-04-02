package edu.cs495.game.net;

import java.io.IOException;
import java.net.InetAddress;

import edu.cs495.game.net.packets.AbstractPacket;
import edu.cs495.game.net.packets.AckPacket;
import edu.cs495.game.net.packets.ChatPacket;
import edu.cs495.game.net.packets.LoginPacket;
import edu.cs495.game.net.packets.LogoutPacket;
import edu.cs495.game.net.packets.NegAckPacket;
import edu.cs495.game.net.packets.PlayerUpdatePacket;
import edu.cs495.game.net.packets.RemovePacket;
import edu.cs495.game.net.packets.ServerEndPacket;
import edu.cs495.game.net.packets.SpawnPacket;

/** A packet handler is an interface which handles the logic for
 * a packet
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public interface PacketHandler {

	/** Handle a packet
	 * 
	 * @param packet - a packet to handle
	 */
	public default void handlePacket(AbstractPacket packet, InetAddress ipAddress, int port) throws IOException {
		switch (packet.getPacketType()) {

		case ACK:
			AckPacket ackPacket = (AckPacket) packet;
			handleAckPacket(ackPacket, ipAddress, port);
			break;
		case NEG_ACK:
			NegAckPacket negAckPacket = (NegAckPacket) packet;
			handleNegAckPacket(negAckPacket, ipAddress, port);
			break;
		case SERVER_END:
			ServerEndPacket serverEndPacket = (ServerEndPacket) packet;
			handleServerEndPacket(serverEndPacket, ipAddress, port);
			break;
		case LOGIN:
			LoginPacket loginPacket = (LoginPacket) packet;
			handleLoginPacket(loginPacket, ipAddress, port);
			break;
		case LOGOUT:
			LogoutPacket logoutPacket = (LogoutPacket) packet;
			handleLogoutPacket(logoutPacket, ipAddress, port);
			break;
		case CHAT:
			ChatPacket chatPacket = (ChatPacket) packet;
			handleChatPacket(chatPacket, ipAddress, port);
			break;
		case PLAYER_UPDATE:
			PlayerUpdatePacket playerUpdatePacket = (PlayerUpdatePacket) packet;
			handlePlayerUpdatePacket(playerUpdatePacket, ipAddress, port);
			break;
		case REMOVE:
			RemovePacket removePacket = (RemovePacket) packet;
			handleRemovePacket(removePacket, ipAddress, port);
			break;
		case SPAWN:
			SpawnPacket spawnPacket = (SpawnPacket) packet;
			handleSpawnPacket(spawnPacket, ipAddress, port);
			break;
		case INVALID:
			break;
		}
	}
	
	public void handleAckPacket(AckPacket ackPacket, InetAddress fromIP, int fromPort) throws IOException;
	public void handleNegAckPacket(NegAckPacket negAckPacket, InetAddress fromIP, int fromPort) throws IOException;
	public void handleServerEndPacket(ServerEndPacket serverEndPacket, InetAddress fromIP, int fromPort) throws IOException;
	public void handleLoginPacket(LoginPacket loginPacket, InetAddress fromIP, int fromPort) throws IOException;
	public void handleLogoutPacket(LogoutPacket logoutPacket, InetAddress fromIP, int fromPort) throws IOException;
	public void handleChatPacket(ChatPacket chatPacket, InetAddress fromIP, int fromPort) throws IOException;
	public void handlePlayerUpdatePacket(PlayerUpdatePacket playerUpdatePacket, InetAddress fromIP, int fromPort) throws IOException;
	public void handleSpawnPacket(SpawnPacket spawnPacket, InetAddress fromIP, int fromPort) throws IOException;
	public void handleRemovePacket(RemovePacket removePacket, InetAddress fromIP, int fromPort) throws IOException;
}
