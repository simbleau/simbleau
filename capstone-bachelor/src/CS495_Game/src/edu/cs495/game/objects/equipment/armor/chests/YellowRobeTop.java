package edu.cs495.game.objects.equipment.armor.chests;

/** A Yellow robe top
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class YellowRobeTop extends RobeTop{

	/** The primary color for the armor */
	private static final int PRIMARY_COLOR = 0x1d1d1d;
	
	/** The secondary color for the armor */
	private static final int SECONDARY_COLOR = 0xeff124;
		
		/** Initialize the armor */
		public YellowRobeTop() {
			super("Yellow Robe", PRIMARY_COLOR, SECONDARY_COLOR);
		}
}
