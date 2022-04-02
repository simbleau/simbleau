package edu.cs495.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.game.net.packets.AbstractPacket;
import edu.cs495.game.net.packets.AckPacket;
import edu.cs495.game.net.packets.ReliablePacket;
import edu.cs495.game.objects.player.Player;
import edu.cs495.game.Game;

/** GameNetwork models an abstract network which handles
 * networking responsibilities on behalf of the user
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public abstract class GameNetwork extends Thread {
	
	/** The maximum amount of players in the network */
	public static final int MAX_PLAYERS = 8;
	
	/** the port for connection */
	public static final int PORT = 8310;
	
	/** The amount of network attempts used before moving on if we need a receipt */
	public static final int NETWORK_ATTEMPTS = 3;
	
	/** The game this is networking */
	protected Game game;

	/** Determines whether or not we are running */
	protected boolean running;
	
	/** The sequence this network is on */
	protected int sequence;
	
	/** The socket connection */
	protected DatagramSocket socket;
	
	/** The packets awaiting acknowledgement or timeout */
	protected List<ReliablePacket> waitingPackets;
	
	/** The connected players */
	protected List<Player> connectedPlayers;
	
	/** Initialize a game network
	 * 
	 * @param game - the game to network
	 */
	public GameNetwork(Game game) {
		this.game = game;
		this.running = false;
		this.sequence = 0;
		this.waitingPackets = new ArrayList<>();
		this.connectedPlayers = new LinkedList<>();
	}

	/** Signal the network to cleanup (Can be blocked) */
	public abstract void endNetwork();
	
	/** Forcibly end the network */
	public void killNetwork() {
		//If we are logged in, we should send a logout to the server
		if (running) {
			endNetwork();
		}

		//Clean up network
		if (!this.socket.isClosed()) {
			this.socket.close();		
		}	
		if (game.getPlayer().isOnline()) {
			this.game.getPlayer().setNetwork(null);
		}
		
		this.interrupt();
	}
	
	/** Send packet to any network responsibilities
	 * 
	 * @param packet - the packet to send
	 * @throws IOException 
	 */
	public abstract void send(AbstractPacket packet) throws IOException;

	/** Receive the next packet - this function blocks
	 * 
	 * @param packet - the packet to send
	 * @throws IOException 
	 */
	public NetworkResponse receive() throws IOException {
		//Buffer
		byte[] data = new byte[AbstractPacket.MTU];
		DatagramPacket datagram = new DatagramPacket(data, data.length);
		
		//Receive
		socket.receive(datagram);
		
		//Parse
		AbstractPacket packet = AbstractPacket.parsePacket(datagram.getData());
		
		DeveloperLog.printLog(((this instanceof GameClient) ? "Client" : "Server") + " received "  + packet.getPacketType().name() + " packet from " 
				+ datagram.getAddress().getHostAddress() + ":" + datagram.getPort() 
				+ " --> '" + packet.getContents() + "' (" + packet.hashCode() + ")");
		
		//Return
		return new NetworkResponse(packet, datagram.getAddress(), datagram.getPort());
	}

	/** Send an acknowledgement packet for a reliable packet to a desired destination
	 * 
	 * @param reliablePacket - the packet to acknowledge
	 * @param ipAddress - the ip address to send acknowledgement to
	 * @param port - the port to sent acknowledgement to
	 */
	public void acknowledge(ReliablePacket reliablePacket, InetAddress ipAddress, int port) throws IOException {
    	AckPacket ackPacket = new AckPacket(
				reliablePacket.getSequence(), game.getDriver().getGameTime(), reliablePacket.hashCode());

		ackPacket.sendTo(ipAddress, port, this);
	}
	
	
	/** Returns whether the network is running
	 * 
	 * @return true if the network is online running, otherwise false
	 */
	public boolean isRunning() {
		return this.running;
	}
	
	/** Return this network's socket
	 * 
	 * @return the socket this network uses
	 */
	public DatagramSocket getSocket() {
		return this.socket;
	}

	/** Return the game itself this network is operating for
	 * 
	 * @return the game itself
	 */
	public Game getGame() {
		return this.game;
	}
	
	/** Advance the sequence and return the sequence
	 * 
	 * @return the next sequence
	 */
	public int nextSequence() {
		this.sequence = (this.sequence + 1) % ReliablePacket.SEQUENCE_CEILING;
		return this.sequence;
	}
	
	/** Return this network's list of unacknowledged/waiting packets
	 * 
	 * @return the network's list of unacknowledged/waiting packets
	 */
	public List<ReliablePacket> getWaitingPackets(){
		return this.waitingPackets;
	}
	
	/** Return the local host address
	 * 
	 * @return the local host address
	 */
	public String getLocalHostAddress() {
		if (socket.isConnected()) {
			return socket.getLocalAddress().getHostAddress();
		} else {
			return "localhost";
		}
	}
	
	/** Find a player in the {@link #connectedPlayers} with a matching IP and port
	 * 
	 * @param ipAddress - the IP of the connected player
	 * @param port - the port of the connected player
	 * @return a player if they exist and are connected, or null
	 */
	protected Player findConnection(InetAddress ipAddress, int port) {
		for (int i = 0; i < connectedPlayers.size(); i++) {
			Player connectedPlayer = connectedPlayers.get(i);
			if (connectedPlayer.getIPAddress().equals(ipAddress) 
					&& connectedPlayer.getPort() == port) {
				return connectedPlayer;
			}
		}
		
		return null; //No player found
	}
	
	/** Find a player in the {@link #connectedPlayers} with a matching username
	 * 
	 * @param username - the username to search for
	 * @return a player if they exist and are connected, or null
	 */
	protected Player findConnection(String username) {
		for (int i = 0; i < connectedPlayers.size(); i++) {
			Player connectedPlayer = connectedPlayers.get(i);
			if (connectedPlayer.getConfig().getUsername().equalsIgnoreCase(username)) {
				return connectedPlayer;
			}
		}
		
		return null; //No player found
	}
	
	
}
