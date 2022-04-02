/**
 * 
 */
package edu.cs495.game.objects.equipment.armor.helmets;

/** A yellow variant of the {@link PointyHat} 
 * 
 * @author Spencer Imbleau
 * @version March 2019
 */
public class YellowPointyHat extends PointyHat{

	/** The primary color for the armor */
	private static final int PRIMARY_COLOR = 0x1d1d1d;
	
	/** The secondary color for the armor */
	private static final int SECONDARY_COLOR = 0xeff124;
	
	/** Initialize the armor */
	public YellowPointyHat() {
		super("Yellow Pointy Hat", PRIMARY_COLOR, SECONDARY_COLOR);
	}
}