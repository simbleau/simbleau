package edu.cs495.game.net;

import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.game.net.packets.ReliablePacket;

/**
 * TimeoutThread is passed to signal a problem with the network and evidence to
 * restart or close the game network.
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class PacketTimeoutThread extends Thread {

	/** The network which this reliable packet is being sent on */
	private GameNetwork network;

	/** The reliable network request itself */
	private NetworkRequest reliableRequest;

	/**
	 * Create a timeout thread which will end the network if we do not receive our
	 * reliable data back after a certain amount of attempts described in
	 * {@link GameNetwork#NETWORK_ATTEMPTS}
	 * 
	 * @param network        - the network to teardown if the reliable packet is not
	 *                       acknowledged
	 * @param reliablePacket - the reliable packet which requires acknowledgement
	 */
	public PacketTimeoutThread(GameNetwork network, NetworkRequest reliableRequest) {
		this.network = network;
		this.reliableRequest = reliableRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		ReliablePacket reliablePacket = (ReliablePacket) reliableRequest.packet;
		try {
			int attempt = 1;
			while (network.running && !reliablePacket.isAcknowledged() && attempt <= GameNetwork.NETWORK_ATTEMPTS) {
				// Wait for timeout
				Thread.sleep(ReliablePacket.WAIT_TIME_MS);

				if (!reliablePacket.isAcknowledged()) {
					DeveloperLog.errLog("Reliable Packet[" + reliablePacket.getPacketType().name() 
							+ "] with sequence #"+ reliablePacket.getSequence() + " died unacknowledged on "
							+ ((attempt != GameNetwork.NETWORK_ATTEMPTS) ? "attempt " + attempt : "final attempt"));

					// Retransmit the login packet if this wasn't the last attempt
					if (attempt < GameNetwork.NETWORK_ATTEMPTS) {
						reliablePacket.retransmit(network);
						attempt++;
						network.game.getPlayer().getConsole().pushImportantMessage(reliablePacket.getPacketType().name()
								+ " packet was never acknowledged. Attempt " + attempt + " starting...");
					} else {
						break;
					}
				}
			}

			reliablePacket.kill(); // Kill this packet, confirming its status

			// If we didn't receive reliable data, we will have to end the network.
			if (!reliablePacket.isAcknowledged()) {
				if (network.running) {
					DeveloperLog.printLog(
							"Reliable Packet[" + reliablePacket.getPacketType().name() 
							+ "] failed to receive acknowledgement " + GameNetwork.NETWORK_ATTEMPTS 
							+ " times. Ending network.");

					network.game.getPlayer().getConsole()
							.pushImportantMessage(reliablePacket.getPacketType().name() + " packet went unacknowledged "
									+ GameNetwork.NETWORK_ATTEMPTS + " times. Force ending network...");

					network.killNetwork();
				}
			}

		} catch (Exception e) {
			DeveloperLog.printLog("Unexpected Error: " + e.getStackTrace().toString());

			network.game.getPlayer().getConsole()
					.pushImportantMessage("Network encountered a fatal error. Killing network...");

			network.killNetwork();
		}
	}

}
