package edu.cs495.game.net;

import java.net.InetAddress;

import edu.cs495.game.net.packets.AbstractPacket;

/** A network request
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class NetworkRequest {

	/** The IP of the target */
	public final InetAddress toIP;
	
	/** The port of the target */
	public int toPort;
	
	/** The packet to send to the target */
	public final AbstractPacket packet;
	
	/** Initialize a network request
	 * 
	 * @param packet - the packet to send
	 * @param toIP - the IP to send the packet to
	 * @param toPort - the port to send the packet to
	 */
	public NetworkRequest(AbstractPacket packet, InetAddress toIP, int toPort) {
		this.packet = packet;
		this.toIP = toIP;
		this.toPort = toPort;
	}
	
}
