/**
 * 
 */
package edu.cs495.game.objects.equipment.armor.helmets;

/** A green variant of the {@link PointyHat} 
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class GreenPointyHat extends PointyHat{

	/** The primary color for the armor */
	private static final int PRIMARY_COLOR = 0x06be06;
	
	/** The secondary color for the armor */
	private static final int SECONDARY_COLOR = 0x333d33;
	
	/** Initialize the armor */
	public GreenPointyHat() {
		super("Green Pointy Hat", PRIMARY_COLOR, SECONDARY_COLOR);
	}
}