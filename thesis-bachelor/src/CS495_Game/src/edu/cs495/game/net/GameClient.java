package edu.cs495.game.net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.game.net.packets.AbstractPacket;
import edu.cs495.game.net.packets.AckPacket;
import edu.cs495.game.net.packets.ChatPacket;
import edu.cs495.game.net.packets.LoginPacket;
import edu.cs495.game.net.packets.LogoutPacket;
import edu.cs495.game.net.packets.NegAckPacket;
import edu.cs495.game.net.packets.PlayerUpdatePacket;
import edu.cs495.game.net.packets.ReliablePacket;
import edu.cs495.game.net.packets.RemovePacket;
import edu.cs495.game.net.packets.ServerEndPacket;
import edu.cs495.game.net.packets.SpawnPacket;
import edu.cs495.game.Game;
import edu.cs495.game.objects.player.LocalPlayer;
import edu.cs495.game.objects.player.Player;

/**
 * A game client for networking
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class GameClient extends GameNetwork implements PacketHandler {

	/** The address of the server */
	private InetAddress serverAddress;

	/** Informs whether we are logged in */
	private boolean loggedIn;

	/**
	 * Initialize the game client
	 * 
	 * @param game      - the game itself
	 * @param ipAddress - the ip address of the server
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public GameClient(Game game, String serverIP) throws UnknownHostException, SocketException {
		super(game);

		this.socket = new DatagramSocket();
		this.serverAddress = InetAddress.getByName(serverIP);
		this.loggedIn = false;
	}

	/**
	 * Returns whether the client is logged in
	 * 
	 * @return true if the client is logged into a server
	 */
	public boolean isLoggedIn() {
		return this.loggedIn;
	}

	/* (non-Javadoc)
	 * @see edu.cs495.engine.net.GameNetwork#endNetwork()
	 */
	@Override
	public void endNetwork() {
		this.running = false;
		
		// If we are logged in, we should send a logout to the server
		if (loggedIn) {
			LogoutPacket logoutPacket = new LogoutPacket(this, serverAddress, PORT, this.game.getPlayer().getConfig().getUsername());
			try {
				send(logoutPacket);
			} catch (IOException e) {
				DeveloperLog.errLog("ClientLogoutException: " + e.getMessage());
			}
			
			// Remove any multiplayer players
			for (int i = 0; i < game.getLevel().getObjects().size(); i++) {
				AbstractGameObject obj = game.getLevel().getObjects().get(i);
				if (obj instanceof Player && !(obj instanceof LocalPlayer)) {
					game.getLevel().removeGameObject(obj);
					i--;
				}
			}
		}
	}


	/** Run the client's thread logic */
	@Override
	public void run() {
		// Send login packet
		LoginPacket loginPacket = new LoginPacket(this, serverAddress, PORT, game.getPlayer().getConfig());
		try {
			loginPacket.sendTo(serverAddress, PORT, this);			
			this.running = true;
			game.getPlayer().getConsole().pushGameMessage(
					"Client sent login request to " + serverAddress.getHostAddress() + ":" + PORT + ".");
		} catch (IOException ioe) {
			DeveloperLog.errLog("ClientLoginSendError: " + ioe.getMessage());
			game.getPlayer().getConsole().pushImportantMessage(
					"Failed to contact server " + serverAddress.getHostAddress() + ":" + PORT + ", Please try again.");
			endNetwork();
		}

		// Main client networking loop
		try {
			while (running) {
				// Collect response
				NetworkResponse response = receive();
				//Handle packet
				new Thread() {
					@Override
					public void run() {
						// If logged in, handle the packet normally.
						if (loggedIn) {
							// Handle packet normally
							try {
								handlePacket(response.packet, response.fromIP, response.fromPort);
							} catch (IOException e) {
								DeveloperLog.errLog("ClientPacketError: " + e.getMessage());
							}
						} else {
							// Check if we received a login acknowledgement
							loggedIn = isLoginAcknowledgement(loginPacket, response);
							if (loggedIn) {
								DeveloperLog.printLog("Client joined server!");
								game.getPlayer().getConsole().pushImportantMessage(
										"Joined server: " + serverAddress.getHostAddress() + ":" + GameServer.PORT);
							} else {
								
								game.getPlayer().getConsole()
										.pushImportantMessage("Received bad acknowledgement. Please try again.");
								endNetwork();
							}
						}
					}
				}.start();
			}
			
			//Notify successful ending
			game.getPlayer().getConsole().pushImportantMessage("Client network shut down.");
			
		} catch (IOException ioe) {
			DeveloperLog.errLog("ClientSocketReceiveException: " + ioe.getMessage());
			game.getPlayer().getConsole().pushImportantMessage("Client network error. Networking ending...");
		}
		
		//Clean up network
		if (!this.socket.isClosed()) {
			this.socket.close();		
		}	
		if (game.getPlayer().isOnline()) {
			this.game.getPlayer().setNetwork(null);
		}
		
	}

	/**
	 * Forward data to the server
	 * 
	 * @param packet - the packet to send to the server
	 * @throws IOException
	 */
	@Override
	public void send(AbstractPacket packet) throws IOException {
		packet.sendTo(serverAddress, GameNetwork.PORT, this);
	}

	@Override
	public void handleAckPacket(AckPacket ackPacket, InetAddress fromIP, int fromPort) throws IOException {
		for (int i = 0; i < waitingPackets.size(); i++) {
			ReliablePacket waitingPacket = waitingPackets.get(i);
			if (ackPacket.isAcknowledging(waitingPacket)) {
				if (fromIP.equals(waitingPacket.getAcknowledgerIP())
						&& fromPort == waitingPacket.getAcknowledgerPort()) {
					waitingPacket.acknowledge();
				} else {
					DeveloperLog.errLog(
							"Illegal cross acknowledgement sent from " + fromIP.getHostAddress() + ":" + fromPort);
				}
			}
		}
	}

	@Override
	public void handleNegAckPacket(NegAckPacket negAckPacket, InetAddress fromIP, int fromPort) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerEndPacket(ServerEndPacket serverEndPacket, InetAddress fromIP, int fromPort)
			throws IOException {
		acknowledge(serverEndPacket, fromIP, fromPort);

		// Remove any multiplayer players
		for (int i = 0; i < game.getLevel().getObjects().size(); i++) {
			AbstractGameObject obj = game.getLevel().getObjects().get(i);
			if (obj instanceof Player && !(obj instanceof LocalPlayer)) {
				game.getLevel().removeGameObject(obj);
				i--;
			}
		}

		// Release socket
		this.socket.close();

		// Empty the network
		this.game.getPlayer().setNetwork(null);

		// Inform user the server stopped
		game.getPlayer().getConsole().pushImportantMessage("The server owner closed the server.");
		
		this.running = false;
		this.loggedIn = false;
	}

	@Override
	public void handleLoginPacket(LoginPacket loginPacket, InetAddress fromIP, int fromPort) throws IOException {
		acknowledge(loginPacket, fromIP, fromPort);

		Player newPlayer = new Player(fromIP, fromPort, loginPacket.getConfig(), game.getLevel().getSpawnX(),
				game.getLevel().getSpawnY());
		newPlayer.init(game.getDriver());
		
		this.connectedPlayers.add(newPlayer);
		
		game.getLevel().addGameObject(newPlayer);
		game.getPlayer().getConsole().pushGameMessage(newPlayer.getConfig().getUsername() + " has joined the game!");
	}

	@Override
	public void handleLogoutPacket(LogoutPacket logoutPacket, InetAddress fromIP, int fromPort) throws IOException {
		acknowledge(logoutPacket, fromIP, fromPort);

		Player player = findConnection(logoutPacket.getUsername());
		if (player != null) {
			game.getLevel().removeGameObject(logoutPacket.getUsername());
			game.getPlayer().getConsole().pushGameMessage(logoutPacket.getUsername() + " has left the game!");
		}
	}

	@Override
	public void handleChatPacket(ChatPacket chatPacket, InetAddress fromIP, int fromPort) throws IOException {
		acknowledge(chatPacket, fromIP, fromPort);
		
		//Show dialogue
		Player speakingPlayer = findConnection(chatPacket.getChatLine().NAME);
		if (speakingPlayer != null) {
			speakingPlayer.say(chatPacket.getChatLine().MESSAGE);
		}
		
		//Push chatline
		game.getPlayer().getConsole().push(chatPacket.getChatLine());
	}


	@Override
	public void handlePlayerUpdatePacket(PlayerUpdatePacket playerUpdatePacket, InetAddress fromIP, int fromPort)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleSpawnPacket(SpawnPacket spawnPacket, InetAddress fromIP, int fromPort) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleRemovePacket(RemovePacket removePacket, InetAddress fromIP, int fromPort) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * Helper method to check if an acknowledgement packet was an acknowledgement
	 * for our login packet
	 * 
	 * @param loginPacket - the login packet
	 * @param response    - the server response
	 * @return true if we received login acknowledgement, false otherwise
	 */
	private boolean isLoginAcknowledgement(LoginPacket loginPacket, NetworkResponse response) {
		// We got an acknowledgement
		if (response.packet instanceof AckPacket) {
			AckPacket ackPacket = (AckPacket) response.packet;

			// Verify response
			if (ackPacket.isAcknowledging(loginPacket)) {
				try {
					handleAckPacket(ackPacket, response.fromIP, response.fromPort);
					return true;
				} catch (IOException ioe) {
					DeveloperLog.errLog("ClientLoginAcknowledgementError: " + ioe.getMessage());
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}

	}
	
	/** Returns the client's address for the server
	 * 
	 * @return the address of the server
	 */
	public InetAddress getServerAddress() {
		return this.serverAddress;
	}
}
