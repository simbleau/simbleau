package edu.cs495.game.objects.equipment.armor.chests;

/** A Red robe top
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class RedRobeTop extends RobeTop{

		/** The primary color for the armor */
		private static final int PRIMARY_COLOR = 0xe31c1c;
		
		/** The secondary color for the armor */
		private static final int SECONDARY_COLOR = 0x1d1d1d;
		
		/** Initialize the armor */
		public RedRobeTop() {
			super("Red Robe", PRIMARY_COLOR, SECONDARY_COLOR);
		}
}