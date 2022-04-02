package edu.cs495.game.net.packets;

import java.net.InetAddress;

import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.game.net.GameNetwork;
import edu.cs495.game.objects.player.PlayerConfig;
import edu.cs495.game.objects.player.PlayerPrivileges;

/**
 * An login packet for networking
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class LoginPacket extends ReliablePacket {

	/** The amount of values that should exist in a login packet */
	private static final int NUM_VALUES = 7;

	/** The configuation file for the player described during login */
	private PlayerConfig config;

	/**
	 * Initialize a login packet by parsing content data
	 * 
	 * @param contents - the contents in this packet
	 */
	public LoginPacket(String contents) {
		super(contents);

		if (this.type != PacketTypes.INVALID) {
			// Parse values
			String[] contentSplitter = this.contents.split(AbstractPacket.CONTENT_DELIMETER);
			if (contentSplitter.length == NUM_VALUES) {
				// Parse
				try {
					String username = contentSplitter[0];
					PlayerPrivileges privilege = PlayerPrivileges.lookupPrivilege(Integer.parseInt(contentSplitter[1]));
					int skinColor = Integer.parseInt(contentSplitter[2]);
					int shirtColor = Integer.parseInt(contentSplitter[3]);
					int pantsColor = Integer.parseInt(contentSplitter[4]);
					int eyeColor = Integer.parseInt(contentSplitter[5]);
					float startHealth = Float.parseFloat(contentSplitter[6]);
	
					this.config = new PlayerConfig(username, skinColor, shirtColor, pantsColor, eyeColor, startHealth,
							privilege);
					this.type = PacketTypes.LOGIN;
	
					DeveloperLog.printLog("LoginPacket data read: '" + this.contents + "'  (" + hashCode() + ")");
				} catch (NumberFormatException nfe) {
					DeveloperLog.errLog("LoginPacketCreationError: " + nfe.getMessage());
					this.type = PacketTypes.INVALID;
				}
			} else {
				this.type = PacketTypes.INVALID;
			}
		}
	}

	/**
	 * Create a login packet from parameters
	 * 
	 * @param network - the network which will wait for this reliable packet
	 * @param config - the configuration of a new player
	 * @see ReliablePacket
	 */
	public LoginPacket(GameNetwork network, InetAddress toIP, int toPort, PlayerConfig config) {
		super(network, toIP, toPort);
		
		this.type = PacketTypes.LOGIN;
		this.config = config;

		StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append(config.getUsername());
		dataBuilder.append(AbstractPacket.CONTENT_DELIMETER);
		dataBuilder.append(config.getPrivilege().identifier);
		dataBuilder.append(AbstractPacket.CONTENT_DELIMETER);
		dataBuilder.append(config.getSkinColor());
		dataBuilder.append(AbstractPacket.CONTENT_DELIMETER);
		dataBuilder.append(config.getShirtColor());
		dataBuilder.append(AbstractPacket.CONTENT_DELIMETER);
		dataBuilder.append(config.getPantsColor());
		dataBuilder.append(AbstractPacket.CONTENT_DELIMETER);
		dataBuilder.append(config.getEyeColor());
		dataBuilder.append(AbstractPacket.CONTENT_DELIMETER);
		dataBuilder.append(config.getStartHealth());
		this.contents = dataBuilder.toString();

		DeveloperLog.printLog("LoginPacket data built: '" + this.contents + "'  (" + hashCode() + ")");
	}

	/**
	 * Return the login player's configuration
	 * 
	 * @return the login player's configuration
	 */
	public PlayerConfig getConfig() {
		return this.config;
	}

}
