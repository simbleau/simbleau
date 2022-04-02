package edu.cs495.game.net.packets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.game.net.GameClient;
import edu.cs495.game.net.GameNetwork;
import edu.cs495.game.net.NetworkRequest;
import edu.cs495.game.net.PacketTimeoutThread;

/**
 * An reliable packet for networking. A reliable packet is one which will
 * start its own timer when sent and confirm its unacknowledgement/acknowledgement after
 * a timeout thread executes when the packet is sent.
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public abstract class ReliablePacket extends AbstractPacket {

	/** The amount of digits in the sequence */
	public static final int SEQUENCE_DIGITS = 5;

	/** The maximum amount of sequence numbers */
	public static final int SEQUENCE_CEILING = (int) Math.pow(10, SEQUENCE_DIGITS);

	/** The nano seconds to wait for acknowledgement */
	public static final long WAIT_TIME_NANO = 5 * GameDriver.NANO_SECOND;

	/** The nano seconds to wait for acknowledgement */
	public static final long WAIT_TIME_MS = 5 * 1000;

	/** The sequence of this packet */
	protected int sequence;
	
	/** Is this packet acknowledged */
	private boolean acknowledged;
	
	/** The IP of the acknowledger */
	private InetAddress acknowledgerIP;
	
	/** The port through which this packet was acknowledged */
	private int acknowledgerPort;
	
	/** Determines whether this acknowledgement packet is alive */
	private boolean alive;

	/** The timeout thread */
	private PacketTimeoutThread timeoutThread;

	/** Initialize a reliable packet by parsing content data
	 * 
	 * @param contents - the contents of a reliable packet
	 */
	protected ReliablePacket(String contents) {
		if (contents.length() > SEQUENCE_DIGITS) {
			try {
				this.type = null;
				this.sequence = Integer.parseInt(contents.substring(0, SEQUENCE_DIGITS));
				this.contents = contents.substring(SEQUENCE_DIGITS);
				this.alive = false;
				this.timeoutThread = null;
			} catch (NumberFormatException e) {
				this.type = PacketTypes.INVALID;
				this.contents = contents;
			}
		} else {
			this.type = PacketTypes.INVALID;
			this.contents = contents;
		}
	}

	/**
	 * Start the packet for reliable data transfer
	 * 
	 * @param network - the network which is keeping track of this packet's acknowledgement
	 */
	protected ReliablePacket(GameNetwork network, InetAddress toIP, int toPort) {
		this.sequence = network.nextSequence();
		this.acknowledged = false;
		
		this.alive = true;
		this.timeoutThread = new PacketTimeoutThread(network, new NetworkRequest(this, toIP, toPort));
	}
	
	/** Reliably send this {@link ReliablePacket}
	 * 
	 * @param ipAddress - the ip to reliably send this packet to
	 * @param port - the port to reliably send this packet to
	 * @param network - the network to send this packet from
	 */
	@Override
	public void sendTo(InetAddress ipAddress, int port, GameNetwork network) throws IOException {
		//Initialize who needs to acknowledge this
		this.acknowledgerIP = ipAddress;
		this.acknowledgerPort = port;
		
		//Debug text
		DeveloperLog.printLog(
				((network instanceof GameClient) ? "Client" : "Server") 
				+ " is sending a reliable " + type.name() + " packet (SEQ #" + this.sequence + ") to " 
						+ acknowledgerIP.getHostAddress() + ":" + acknowledgerPort
				+ " --> '" + this.contents + "' (" + hashCode() + ")");
		
		//Queue this packet as unacknowledged by the network
		network.getWaitingPackets().add(this);
		
		byte[] data = getData();
		DatagramPacket datagram = new DatagramPacket(data, data.length);
		datagram.setAddress(acknowledgerIP);
		datagram.setPort(acknowledgerPort);
		network.getSocket().send(datagram);
		
		//Start timeout thread
		timeoutThread.start();
	}
	
	public void retransmit(GameNetwork network) throws IOException {
		//Debug text
		DeveloperLog.errLog(
				((network instanceof GameClient) ? "Client" : "Server") 
				+ " is retransmitting reliable " + type.name() + " packet (SEQ #" + this.sequence + ") to " 
						+ acknowledgerIP + ":" + acknowledgerPort
				+ " --> '" + this.contents + "' (" + hashCode() + ")");
		
		byte[] data = getData();
		DatagramPacket datagram = new DatagramPacket(data, data.length);
		datagram.setAddress(acknowledgerIP);
		datagram.setPort(acknowledgerPort);
		network.getSocket().send(datagram);
	}

	/** Return the data of a reliable packet
	 * 
	 * @return reliable packet data
	 */
	@Override
	public byte[] getData() {
		StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append(String.format("%0" + IDENTIFIER_LENGTH + "d", this.type.identifier));
		dataBuilder.append(String.format("%0" + SEQUENCE_DIGITS + "d", this.sequence));
		dataBuilder.append(contents);

		byte[] dataBytes = dataBuilder.toString().getBytes();
		return dataBytes;
	}
	
	/** Acknowledge this packet */
	public void acknowledge() {
		this.acknowledged = true;
	}
	
	/**
	 * Wait for this packet to either die or become acknowledged
	 * This method blocks.
	 * 
	 * @param network - The network this packet is unacknowledged on
	 * @throws IOException
	 */
	public void blockUntilFinal() {
		try {
			while (alive && !acknowledged) {
				Thread.sleep(1);
			}
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		alive = false;
	}
	
	/** Returns the IP of the person supposed to acknowledge this packet (for security)
	 * 
	 * @return the IP of the person supposed to acknowledge this packet
	 */
	public InetAddress getAcknowledgerIP() {
		return this.acknowledgerIP;
	}

	/** Returns the port of the person supposed to acknowledge this packet (for security)
	 * 
	 * @return the port of the person supposed to acknowledge this packet
	 */
	public int getAcknowledgerPort() {
		return this.acknowledgerPort;
	}

	/** Return whether the packet is acknowledged or not
	 * 
	 * @return true if the packet is acknowledged, false otherwise
	 */
	public boolean isAcknowledged() {
		return this.acknowledged;
	}

	
	/** Mark this packet as dead */
	public void kill() {
		this.alive = false;
	}

	/** Return whether the packet is alive or dead
	 * 
	 * @return true if the packet is alive, false otherwise
	 */
	public boolean isAlive() {
		return this.alive;
	}

	/** Return this packet's sequence number
	 * 
	 * @return the packet sequence
	 */
	public int getSequence() {
		return this.sequence;
	}
}
