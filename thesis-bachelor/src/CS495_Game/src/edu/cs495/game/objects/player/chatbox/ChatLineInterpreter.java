package edu.cs495.game.objects.player.chatbox;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;


import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.game.net.GameClient;
import edu.cs495.game.net.GameNetwork;
import edu.cs495.game.net.GameServer;
import edu.cs495.game.net.packets.ChatPacket;
import edu.cs495.game.objects.player.Player;
import edu.cs495.game.Game;

public class ChatLineInterpreter {

	/** All chat commands start with this icon */
	public static final String COMMAND_PREFIX = "!";

	/** The character which splits arguments */
	public static final String ARG_SPLITTER = " ";

	/** The console we are interpreting for */
	ChatConsole console;

	/**
	 * Initialize a chat line interpreter. This class interprets chat commands and
	 * runs them.
	 * 
	 * @param console - the console which we are interpreting for
	 */
	public ChatLineInterpreter(ChatConsole console) {
		this.console = console;
	}

	/**
	 * Interpret a chat line.
	 * 
	 * @param chatLine - the chat line to interpret
	 */
	public void interpret(ChatLine chatLine) {
		// Check the message
		if (chatLine.MESSAGE.length() > 0) {

			// Check that the command starts with our command prefix '!'
			if (chatLine.MESSAGE.startsWith(COMMAND_PREFIX)) {

				//Handle command
				String command = chatLine.MESSAGE.substring(COMMAND_PREFIX.length());
				String[] commandArgs = command.split(ARG_SPLITTER);

				// Run command
				switch (commandArgs[0].toUpperCase()) {
				case "SERVER":
					serverCommand(commandArgs);
					break;

				case "JOIN":
					joinCommand(commandArgs);
					break;
					
				case "END":
					endCommand(commandArgs);
					break;
					
				default:
					console.pushImportantMessage("Invalid command: " + commandArgs[0]);
					break;
				}
			} else {
				forwardChatLine(chatLine);
			}
		}
	}
	
	private void forwardChatLine(ChatLine chatLine) {
		if (console.chatUser.isOnline()) {
			GameNetwork network = console.chatUser.getNetwork();
			if (network instanceof GameServer) {
				GameServer server = (GameServer) network;
				forwardChatLineToNetwork(server, chatLine);
			} else if (network instanceof GameClient) {
				GameClient client = (GameClient) network;
				if (client.isLoggedIn()) {
					forwardChatLineToNetwork(client, chatLine);
				}
			}
		}
		console.chatUser.say(chatLine.MESSAGE);
	}
	
	/** Forward the chatline to everyone on the server
	 * 
	 * @param server - the server to forward the chatline to
	 * @param chatLine - the chatline itself
	 */
	private void forwardChatLineToNetwork(GameServer server, ChatLine chatLine) {
		for (int i = 0; i < server.getConnectedPlayers().size(); i++) {
			Player player = server.getConnectedPlayers().get(i);
			ChatPacket chatPacket = new ChatPacket(server, player.getIPAddress(), player.getPort(), chatLine);
			try {
				chatPacket.sendTo(player.getIPAddress(), player.getPort(), server);
			} catch (IOException ioe) {
				DeveloperLog.errLog("ServerSendError: " + ioe.getMessage());
			}
		}
	}
	
	/** Forward the chatline to the server
	 * 
	 * @param client - the client
	 * @param chatLine - the chatline itself
	 */
	private void forwardChatLineToNetwork(GameClient client, ChatLine chatLine) {
		if (client.isLoggedIn()) {
		ChatPacket chatPacket = new ChatPacket(client, client.getServerAddress(), GameNetwork.PORT, chatLine);
			try {
				chatPacket.send(client);
			} catch (IOException e) {
				DeveloperLog.errLog("ClientChatException : " + e.getMessage());
			}
		}
	}

	/**
	 * Helper method which runs the 'server' command
	 * 
	 * @param commandArgs - arguments of the command
	 */
	private void serverCommand(String[] commandArgs) {
		if (commandArgs.length == 1) {
			if (console.chatUser.isOnline()) {
				console.pushGameMessage("The network is busy. Type !end to end network.");
			} else {
				try {
					GameServer server = new GameServer((Game) console.gameDriver.getGame());
					console.chatUser.setNetwork(server);
					server.start();
				} catch (SocketException e) {
					DeveloperLog.errLog("ServerSocketException : " + e.getMessage());
					console.pushImportantMessage("Failed to start server on port " + GameServer.PORT);
				}
			}
		} else {
			console.pushImportantMessage("Incorrect usage. Proper: '!server'");
		}
	}

	private void joinCommand(String[] commandArgs) {
		if (commandArgs.length == 2) {
			if (console.chatUser.isOnline()) {
				console.pushGameMessage("The network is busy. Type !end to end network.");
			} else {
				try {
					GameClient client = new GameClient((Game) console.gameDriver.getGame(), commandArgs[1]);
					console.chatUser.setNetwork(client);
					client.start();
				} catch (SocketException e) {
					DeveloperLog.errLog("ClientSocketException: " + e.getMessage());
					console.pushImportantMessage("Client failed to reach the server " 
					+ commandArgs[1] + ":" + GameNetwork.PORT + ". Please try again.");
				} catch (UnknownHostException e) {
					DeveloperLog.errLog("ClientSocketException: " + e.getMessage());
					console.pushImportantMessage("The desired server '" + commandArgs[1] + "' is an unknown host.");
				}
			}
		} else {
			console.pushImportantMessage("Incorrect usage. Proper: '!join <ip>'");
		}
	}

	private void endCommand(String[] commandArgs) {
		if (commandArgs.length <= 2) {
			//Flags
			boolean forceKill = false;
			if (commandArgs.length == 2) {
				if (commandArgs[1].equalsIgnoreCase("-f")){
					forceKill = true;
				} else {
					console.pushImportantMessage("Invalid flag: '" + commandArgs[1] + "'. Expected '-f'");
					return;
				}
			}
			
			//End network
			if (console.chatUser.isOnline()) {
				if (forceKill) {
					console.chatUser.getNetwork().killNetwork();
				} else {
					console.chatUser.getNetwork().endNetwork();
				}
			} else {
				console.pushGameMessage("You cannot end your connection because you are not online.");
			}
		} else {
			console.pushImportantMessage("Incorrect usage. Proper: '!end'");
		}
	}
}
