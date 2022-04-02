/**
 * 
 */
package edu.cs495.game.objects.equipment.armor.helmets;

/** A red variant of the {@link PointyHat} 
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class RedPointyHat extends PointyHat{

	/** The primary color for the armor */
	private static final int PRIMARY_COLOR = 0xe31c1c;
	
	/** The secondary color for the armor */
	private static final int SECONDARY_COLOR = 0x1d1d1d;
	
	/** Initialize the armor */
	public RedPointyHat() {
		super("Red Pointy Hat", PRIMARY_COLOR, SECONDARY_COLOR);
	}
}