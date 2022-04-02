/**
 * 
 */
package edu.cs495.game.net;

import java.net.InetAddress;

import edu.cs495.game.net.packets.AbstractPacket;

/** A network response
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class NetworkResponse {
	
	/** The IP from the target */
	public final InetAddress fromIP;
	
	/** The port from the target */
	public int fromPort;
	
	/** The packet received from the target */
	public final AbstractPacket packet;
	
	/** Initialize a network response
	 * 
	 * @param packet - the packet received
	 * @param fromIP - the IP which sent the packet
	 * @param fromPort - the port which sent the packet
	 */
	public NetworkResponse(AbstractPacket packet, InetAddress fromIP, int fromPort) {
		this.packet = packet;
		this.fromIP = fromIP;
		this.fromPort = fromPort;
	}

}
