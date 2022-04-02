package edu.cs495.game.components;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractComponent;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.game.objects.player.LocalPlayer;

public class GloveListenerComponent extends AbstractComponent {

	/** The parent player who can wield gloves */
	private LocalPlayer parent;
	
	/** Initialize this glove listener component
	 * 
	 * @param gloveUser - the user we will listen for glove updates for
	 */
	public GloveListenerComponent(LocalPlayer localPlayer) {
		super(localPlayer);
	}
	
	@Override
	public void init(GameDriver gameDriver) {
		// Do nothing
	}

	@Override
	public void update(GameDriver gameDriver) {
		if (parent.getGearManager().hasPrimaryGlove()) {
			parent.getGearManager().getPrimaryGlove().tick(gameDriver);
		}
		if (parent.getGearManager().hasSecondaryGlove()) {
			parent.getGearManager().getSecondaryGlove().tick(gameDriver);
		}
	}

	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		//Do nothing
		//Would be cool to add a UI element for the cooldowns here, no?
	}

}
