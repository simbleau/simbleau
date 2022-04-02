package edu.cs495.game.net.packets;

/** PacketTypes contains the identifiers of all packet types
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public enum PacketTypes {
	
	/** An invalid packet */
	 INVALID(-1), 
	 
	 /** A packet signalling acknowledgement/handshake */
	 ACK(0),
	 
	 /** A packet signalling negative acknowledgement/handshake */
	 NEG_ACK(1),
	 
	 /** A packet signalling the server ended */
	 SERVER_END(2),
	 
	 /** A packet signalling a new connection */
	 LOGIN(3), 
	 
	 /** A packet signalling a lost connection */
	 LOGOUT(4), 
	 
	 /**A chat line packet */
	 CHAT(5),
	 
	 /** A packet signalling a player update */
	 PLAYER_UPDATE(6),
	 
	 /** Spawn of something in the game world */
	 SPAWN(7),
	 
	 /** Remove something in the game world */
	 REMOVE(8);

	/** The identifier for this packet */
    public final int identifier;

    /** Initialize the packet type with its integer identifier
     * 
     * @param identifier - the integer which identifies this packet type
     */
    private PacketTypes(int identifier) {
        this.identifier = identifier;
    }

}
