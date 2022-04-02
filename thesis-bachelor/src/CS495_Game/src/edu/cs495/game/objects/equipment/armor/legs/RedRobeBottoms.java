package edu.cs495.game.objects.equipment.armor.legs;

/** A red skirt
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class RedRobeBottoms extends RobeBottom {

	/** The primary color for the armor */
	private static final int PRIMARY_COLOR = 0xe31c1c;
	
	/** The secondary color for the armor */
	private static final int SECONDARY_COLOR = 0x1d1d1d;
	
	/** Properly initialize the Robe Top */
	public RedRobeBottoms() {
		super("Red Skirt", PRIMARY_COLOR, SECONDARY_COLOR);
	}
}
