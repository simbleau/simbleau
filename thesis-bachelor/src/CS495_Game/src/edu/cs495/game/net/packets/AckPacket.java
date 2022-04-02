package edu.cs495.game.net.packets;

import edu.cs495.engine.developer.DeveloperLog;

/** An acknowledgement packet
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class AckPacket extends AbstractPacket {
	
	/** The number of values that should exist in this packet
	 * Respectively being the acknowledgement id and checksum
	 */
	public static final int NUM_VALUES = 3;
	
	/** The ID of this acknowledgement */
	private int acknowledgementId;
	
	/** The time this acknowledgement was sent */
	private long acknowledgementSendTime;
	
	/** A hashcode of what the sender received - used to confirm authenticity */
	private int hashReceipt;
	
	/**
	 * Initialize an acknowledgement packet by parsing content data
	 * 
	 * @param contents - the contents in this packet
	 */
	public AckPacket(String contents) {
		this.contents = contents;
		try {
			String[] values = contents.split(AbstractPacket.CONTENT_DELIMETER);
			
			if (values.length == NUM_VALUES) {
				this.acknowledgementId = Integer.parseInt(values[0]);
				this.acknowledgementSendTime = Long.parseLong(values[1]);
				this.hashReceipt = Integer.parseInt(values[2]);
				this.type = PacketTypes.ACK;	
				DeveloperLog.printLog("AckPacket data read: '" + contents + "'  (" + hashCode() + ")");
			} else {
				this.type = PacketTypes.INVALID;
			}
		} catch (NumberFormatException e) {
			this.type = PacketTypes.INVALID;
		}
	}
	
	/**
	 * Create a login packet from parameters
	 * 
	 * @param id - the sequence to acknowledge
	 * @param config - the configuration of a new player
	 * @see ReliablePacket
	 */
	public AckPacket(int sequence, Long sendTime, int hashCode) {
		this.acknowledgementId = sequence;
		this.acknowledgementSendTime = sendTime;
		this.hashReceipt = hashCode;
		
		StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append(acknowledgementId);
		dataBuilder.append(AbstractPacket.CONTENT_DELIMETER);
		dataBuilder.append(acknowledgementSendTime);
		dataBuilder.append(AbstractPacket.CONTENT_DELIMETER);
		dataBuilder.append(hashReceipt);
		this.contents = dataBuilder.toString();
		
		this.type = PacketTypes.ACK;
		
		DeveloperLog.printLog("AckPacket data built: '" + contents + "'  (" + hashCode() + ")");
	}

	/** Returns whether this acknowledgement packet does acknowledge the
	 * given reliable packet 
	 * 
	 * @param reliablePacket - the packet to test for acknowledgement
	 * @return true if this packet acknowledges the given reliablePacket
	 */
	public boolean isAcknowledging(ReliablePacket reliablePacket) {
		if (reliablePacket.sequence == acknowledgementId
				&& reliablePacket.hashCode() == hashReceipt) {
			return true;
		} else {
			return false;
		}
	}
}
