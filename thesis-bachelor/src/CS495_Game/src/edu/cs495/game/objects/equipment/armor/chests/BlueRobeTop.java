package edu.cs495.game.objects.equipment.armor.chests;

/** A Blue robe top
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class BlueRobeTop extends RobeTop{

		/** The primary color for the armor */
		private static final int PRIMARY_COLOR = 0x5073b5;
		
		/** The secondary color for the armor */
		private static final int SECONDARY_COLOR = 0x0f3a89;
		
		/** Initialize the armor */
		public BlueRobeTop() {
			super("Blue Robe", PRIMARY_COLOR, SECONDARY_COLOR);
		}
}