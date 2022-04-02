package edu.cs495.game.objects.player;

/** Describes the  privilege level of the player  */
public enum PlayerPrivileges {
	/** The identifier for a normal player */
		NORMAL(0),
		
		/** The identifier for a moderator */
		MODERATOR(1),
		
		/** The identifier for an admin */
		ADMIN(2);
	
	/** The value of the color */
	public final int identifier;

	/**
	 * Initialize the enumerated value with a color
	 * 
	 * @param color - the color value of the enumeration
	 */
	PlayerPrivileges(int identifier) {
		this.identifier = identifier;
	}
	
	/** Lookup the privilege with an identifier
	 * 
	 * @param id - the id to search
	 * @return the privileges respective to the id, or normal if search failed
	 */
	public static PlayerPrivileges lookupPrivilege(int id) {		
		for (PlayerPrivileges privilege : PlayerPrivileges.values()) {
			if (privilege.identifier == id) {
				return privilege;
			}
		}
		
		return PlayerPrivileges.NORMAL;
	}
}
