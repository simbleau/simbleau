/**
 * 
 */
package edu.cs495.game.objects.equipment.armor.helmets;


/** A blue variant of the {@link PointyHat} 
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class BluePointyHat extends PointyHat{

	/** The primary color for the armor */
	private static final int PRIMARY_COLOR = 0x5073b5;
	
	/** The secondary color for the armor */
	private static final int SECONDARY_COLOR = 0x0f3a89;
	
	/** Initialize the armor */
	public BluePointyHat() {
		super("Blue Pointy Hat", PRIMARY_COLOR, SECONDARY_COLOR);
	}
}