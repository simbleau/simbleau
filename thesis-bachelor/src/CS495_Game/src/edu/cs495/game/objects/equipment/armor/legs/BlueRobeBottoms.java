package edu.cs495.game.objects.equipment.armor.legs;

/** A blue skirt
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class BlueRobeBottoms extends RobeBottom{

	/** The primary color for the armor */
	private static final int PRIMARY_COLOR = 0x5073b5;
	
	/** The secondary color for the armor */
	private static final int SECONDARY_COLOR = 0x0f3a89;
		
		/** Initialize the armor */
		public BlueRobeBottoms() {
			super("Blue Skirt", PRIMARY_COLOR, SECONDARY_COLOR);
		}
}