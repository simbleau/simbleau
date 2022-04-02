package edu.cs495.game.net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

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
import edu.cs495.game.objects.player.chatbox.ChatLine;

/** A game server for networking
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class GameServer extends GameNetwork implements PacketHandler {
	
	/** Initialize a game server
	 * 
	 * @param game - the game we are server for
	 * @throws SocketException 
	 */
	public GameServer(Game game) throws SocketException {
		super(game);
		
		this.socket = new DatagramSocket(PORT);
		this.running = false;
	}
	
	@Override
	public void endNetwork() {
		running = false;
		
		// Remove any multiplayer players
		if (connectedPlayers.size() > 0 ) {
			//Remove all MP players
			for (int i = 0; i < game.getLevel().getObjects().size(); i++) {
				AbstractGameObject obj = game.getLevel().getObjects().get(i);
				if (obj instanceof Player && !(obj instanceof LocalPlayer)) {
					game.getLevel().removeGameObject(obj);
					i--;
				}
			}
			
			for (int i = 0; i < connectedPlayers.size(); i++) {
				Player connectedPlayer = connectedPlayers.get(i);
				ServerEndPacket serverEndPacket = new ServerEndPacket(this, connectedPlayer.getIPAddress(), connectedPlayer.getPort());
				try {
					serverEndPacket.sendTo(connectedPlayer.getIPAddress(), connectedPlayer.getPort(), this);
				} catch (IOException e) {
					killNetwork();
				}
			}
		}
	}

	/** Run the server's thread logic */
	@Override
	public void run() {
		this.running = true;
		game.getPlayer().getConsole().pushImportantMessage("Started server on port " + PORT);
		
		//Run the server until we decide to stop
		try {
			while (running) {

				NetworkResponse response = receive();
				new Thread() {
					@Override
					public void run() {
						try {
							handlePacket(response.packet, response.fromIP, response.fromPort);
						} catch (IOException ioe) {
							DeveloperLog.errLog("ServerPacketHandleError: " + ioe.getMessage());
						}
					}
				}.start();
				
			}
			//Notify successful ending
			game.getPlayer().getConsole().pushImportantMessage("Server network shutdown.");
			
		} catch (IOException ioe) {
			DeveloperLog.errLog("ServerReceiveError: " + ioe.getMessage());
			game.getPlayer().getConsole().pushImportantMessage("Server network error. Networking ending...");
			killNetwork();
		}
		
		//Close up network
		if (!this.socket.isClosed()) {
			this.socket.close();		
		}	
		if (game.getPlayer().isOnline()) {
			this.game.getPlayer().setNetwork(null);
		}
	}
	
	/** Forward data to all connected users
	 * 
	 * @param packet - the packet to send to the server
	 * @throws IOException
	 */
	@Override
	public void send(AbstractPacket packet) {		
		for (int i = 0; i < connectedPlayers.size(); i++) {
			Player player = connectedPlayers.get(i);
			try {
				packet.sendTo(player.getIPAddress(), player.getPort(), this);
			} catch (IOException ioe) {
				DeveloperLog.errLog("ServerSendError: " + ioe.getMessage());
			}
		}
	}
	
	@Override
	public void handleAckPacket(AckPacket ackPacket, InetAddress fromIP, int fromPort) throws IOException {
		for (int i = 0; i < waitingPackets.size(); i++) {
			ReliablePacket waitingPacket = waitingPackets.get(i);
			if (ackPacket.isAcknowledging(waitingPacket)) {
				if (fromIP.equals(waitingPacket.getAcknowledgerIP())
						&& fromPort == waitingPacket.getAcknowledgerPort()){
					waitingPacket.acknowledge();	
				} else {
					DeveloperLog.errLog("Illegal cross acknowledgement sent from " + fromIP.getHostAddress() + ":" + fromPort);
				}
			}
		}
	}

	@Override
	public void handleNegAckPacket(NegAckPacket negAckPacket, InetAddress fromIP, int fromPort) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void handleServerEndPacket(ServerEndPacket serverEndPacket, InetAddress fromIP, int fromPort) throws IOException {
		//? We never receive packets like this because we are the server.
	}
	
	@Override
	public void handleLoginPacket(LoginPacket newLoginPacket, InetAddress fromIP, int fromPort) throws IOException {
		//Return acknowledgement to the client for their login
		acknowledge(newLoginPacket, fromIP, fromPort);
		
		//Run the synchronization of the new player and current players on a new thread
		addConnection(newLoginPacket, fromIP, fromPort);
	}
	
	@Override
	public void handleLogoutPacket(LogoutPacket logoutPacket, InetAddress fromIP, int fromPort) throws IOException {
		//Acknowledge the logout packet
		acknowledge(logoutPacket, fromIP, fromPort);
		
		//Run the removal of the player on a new thread
		removeConnection(logoutPacket.getUsername());
	}

	@Override
	public void handleChatPacket(ChatPacket chatPacket, InetAddress fromIP, int fromPort) throws IOException {
		//Acknowledge packet
		acknowledge(chatPacket, fromIP, fromPort);
		
		//Get chatline and push it to console view
		ChatLine chatLine = chatPacket.getChatLine();
		
		//Show dialogue
		Player speakingPlayer = findConnection(chatLine.NAME);
		if (speakingPlayer != null) {
			speakingPlayer.say(chatLine.MESSAGE);
		}
		game.getPlayer().getConsole().push(chatLine);
		
		//Forward the message to all clients
		for (int i = 0; i < connectedPlayers.size(); i++) {
			Player player = connectedPlayers.get(i);
			if (!player.getConfig().getUsername().equalsIgnoreCase(chatLine.NAME)) {
				ChatPacket packet = new ChatPacket(this, player.getIPAddress(), player.getPort(), chatLine);
				packet.sendTo(player.getIPAddress(), player.getPort(), this);
			}
		}
	}

	@Override
	public void handlePlayerUpdatePacket(PlayerUpdatePacket playerUpdatePacket, InetAddress fromIP, int fromPort) throws IOException {
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
	
	/** Helper method which adds a new connected player to the game
	 * 
	 * @param player - the player to add 
	 * @param packet - the login packet
	 */
	public void addConnection(LoginPacket newLoginPacket, InetAddress fromIP, int fromPort) throws IOException {
		//Compile a list of players in the lobby already
		List<LoginPacket> loginPackets = new LinkedList<>();
		LoginPacket serverLogin = new LoginPacket(this, fromIP, fromPort, game.getPlayer().getConfig());
		loginPackets.add(serverLogin);
		for (int i = 0; i < connectedPlayers.size(); i++) {
			Player connectedPlayer = connectedPlayers.get(i);
			LoginPacket connectedPlayerLogin = new LoginPacket(this, fromIP, fromPort, connectedPlayer.getConfig());
			loginPackets.add(connectedPlayerLogin);
		}
		
		//Send the new player all of the current players (synchronize the new player)
		for (LoginPacket loginPacket : loginPackets) {
			loginPacket.sendTo(fromIP, fromPort, this);
		}
		//Determine if the new player acknowledged all of the current players
		boolean newPlayerSynced = true;
		for (LoginPacket loginPacket : loginPackets) {
			loginPacket.blockUntilFinal();
			if (!loginPacket.isAcknowledged()) {
				newPlayerSynced = false; 
				break;
			}
		}
		if (!newPlayerSynced) {
			//The new player is a laggy, we aren't adding him to the server
			for (LoginPacket loginPacket : loginPackets) {
				//Remove the queue'd waiting packets
				waitingPackets.remove(loginPacket);
			}
			return;
		} else {
			//The new player is synced, so we can officially tell everyone else he's here.
			
			//Compile a list of requests for the new player we will need to see acknowledgement for from current players
			List<NetworkRequest> requests = new LinkedList<>();
			for (int i = 0; i < connectedPlayers.size(); i++) {
				Player connectedPlayer = connectedPlayers.get(i);
				LoginPacket newLogin = new LoginPacket(this, connectedPlayer.getIPAddress(), connectedPlayer.getPort(), newLoginPacket.getConfig());
				NetworkRequest request = new NetworkRequest(newLogin, connectedPlayer.getIPAddress(), connectedPlayer.getPort());
				requests.add(request);
			}
			
			//Send the requests
			for (NetworkRequest request : requests) {
				request.packet.sendTo(request.toIP, request.toPort, this);
			}
			
			//Initialize our new player
			Player newPlayer = new Player(fromIP, fromPort, newLoginPacket.getConfig(), game.getLevel().getSpawnX(), game.getLevel().getSpawnY());
			newPlayer.init(game.getDriver());
	        this.connectedPlayers.add(newPlayer);
			game.getLevel().addGameObject(newPlayer);
			game.getPlayer().getConsole().pushGameMessage(newPlayer.getConfig().getUsername() + " has joined");
			
			//Check that the current players have synchronized the new player
			for (NetworkRequest request : requests) {
				LoginPacket loginPacket = (LoginPacket) request.packet;
				loginPacket.blockUntilFinal();
				if (!loginPacket.isAcknowledged()) {
					//Remove the laggy SOB who isn't acknowledging this new player
					Player laggyConnectedPlayer = findConnection(request.toIP, request.toPort);
					if (laggyConnectedPlayer != null) {
						LogoutPacket logoutPacket = new LogoutPacket(laggyConnectedPlayer.getConfig().getUsername());
						removeConnection(logoutPacket.getUsername());
					}
				}
			}
		}
    }

	/** Helper method which removes a connected player from the game
	 * 
	 * @param packet - the logout packet
	 */
    public void removeConnection(String username) throws IOException {    	
    	//Find the player to remove
    	boolean removed = false;
    	for (int i = 0; i < connectedPlayers.size(); i++) {
    		Player connectedPlayer = connectedPlayers.get(i);
    		if (connectedPlayer.getConfig().getUsername().equalsIgnoreCase(username)) {
    			//They are the player being removed
    			connectedPlayers.remove(connectedPlayer);
    			this.game.getLevel().removeGameObject(connectedPlayer.getConfig().getUsername());
    			game.getPlayer().getConsole().pushGameMessage(connectedPlayer.getConfig().getUsername() + " has left");
    			removed = true;
    			break;
    		}
    	}
    	//If we didn't find a player to remove, quit
    	if (!removed) {
    		return;
    	}
    	
    	//Compile a list of logout requests we will check acknowledgement for 
    	List<NetworkRequest> logoutRequests = new LinkedList<>();
    	for (int i = 0; i < connectedPlayers.size(); i++) {
    		Player connectedPlayer = connectedPlayers.get(i);
    		
			//They need to be updated of the player's departure
    		LogoutPacket logoutPacket = new LogoutPacket(this, connectedPlayer.getIPAddress(), connectedPlayer.getPort(), username);
    		NetworkRequest logoutRequest = 
    				new NetworkRequest(logoutPacket, connectedPlayer.getIPAddress(), connectedPlayer.getPort());
    		logoutRequests.add(logoutRequest);
    	
    	}
    	
    	//Send all logout requests
    	for (NetworkRequest logoutRequest : logoutRequests) {
    		LogoutPacket logoutPacket = (LogoutPacket) logoutRequest.packet;
    		logoutPacket.sendTo(logoutRequest.toIP, logoutRequest.toPort, this);
    	}
    	
    	//Await all responses from connected players
    	for (NetworkRequest logoutRequest : logoutRequests) {
    		LogoutPacket logoutPacket = (LogoutPacket) logoutRequest.packet;
    		logoutPacket.blockUntilFinal();
    		if (!logoutPacket.isAcknowledged()) {
    			//Remove this laggy SOB as well since they didn't acknowledge the player's removal
    			Player laggyConnectedPlayer = findConnection(logoutRequest.toIP, logoutRequest.toPort);
    			if (laggyConnectedPlayer != null) {
    				connectedPlayers.remove(laggyConnectedPlayer);
    			}
    		}
    	}
    	
    }

	/**
	 * @return the connectedPlayers
	 */
	public List<Player> getConnectedPlayers() {
		return connectedPlayers;
	}
    
    
    
}
