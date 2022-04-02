package edu.cs495.game.objects.equipment.armor.chests;

/** A green robe top
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class GreenRobeTop extends RobeTop{

		/** The primary color for the armor */
		private static final int PRIMARY_COLOR = 0x06be06;
		
		/** The secondary color for the armor */
		private static final int SECONDARY_COLOR = 0x333d33;
		
		/** Initialize the armor */
		public GreenRobeTop() {
			super("Green Robe", PRIMARY_COLOR, SECONDARY_COLOR);
		}
}