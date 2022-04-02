/**
 * 
 */
package edu.cs495.game.levels;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.game.AbstractLevel;
import edu.cs495.engine.game.MapBoundaries;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.game.objects.equipment.armor.chests.BlueRobeTop;
import edu.cs495.game.objects.equipment.armor.chests.GreenRobeTop;
import edu.cs495.game.objects.equipment.armor.chests.RedRobeTop;
import edu.cs495.game.objects.equipment.armor.chests.YellowRobeTop;
import edu.cs495.game.objects.equipment.armor.helmets.ModCrown;
import edu.cs495.game.objects.equipment.armor.legs.BlueRobeBottoms;
import edu.cs495.game.objects.equipment.armor.legs.GreenRobeBottoms;
import edu.cs495.game.objects.equipment.armor.legs.RedRobeBottoms;
import edu.cs495.game.objects.equipment.armor.legs.YellowRobeBottoms;
import edu.cs495.game.objects.player.PlayerPrivileges;
import edu.cs495.game.objects.scenery.Fence;
import edu.cs495.game.objects.scenery.Teepee;

/** The surface world level
 * @author Spencer Imbleau
 */
public class SurfaceWorld extends AbstractLevel{

	/** The spawn point X */
	private static final float SPAWN_X = 100f;
	
	/** The spawn point Y */
	private static final float SPAWN_Y = 100f;

	
	/** Runs one-off operations before the game loop continues 
	 * @param gameDriver a reference to the Game Driver*/
	@Override
	public void init(GameDriver gameDriver) {
		gameDriver.getWindow().setJFrameTitle("Developer Test Map");
		this.map = new Image("/map/surface/map.png");
		this.boundaries = new MapBoundaries("/map/surface/boundary_map.png");

		this.width = map.getWidth();
		this.height = map.getHeight();
		gameDriver.getRenderer().setMap(map);
		
		test(gameDriver);
		
		//gameDriver.getLevel().addGameObject(new Anvil(245f, 210f));
		gameDriver.getLevel().addGameObject(new Teepee(200f, 100f));
		gameDriver.getLevel().addGameObject(new Teepee(300f, 180f));
		gameDriver.getLevel().addGameObject(new Fence(250f, 50f));
		gameDriver.getLevel().addGameObject(new Fence(370f, 100f));
		gameDriver.getLevel().addGameObject(new Fence(120f, 40f));
		//Last
		for (AbstractGameObject object : levelObjects) {
			object.init(gameDriver);
		}
	}

	//TODO remove// testing
	private void test(GameDriver gameDriver) {
		
		
		
		ModCrown crown = new ModCrown(PlayerPrivileges.ADMIN);
		crown.init(gameDriver);
		crown.spawn(this, 220, 240);
		
		
		//Drop robe top on map
		RedRobeTop robeTop = new RedRobeTop();
		robeTop.init(gameDriver);
		robeTop.spawn(this, 325, 400);
		
		//Drop robe bottom on map
		RedRobeBottoms robeBottom = new RedRobeBottoms();
		robeBottom.init(gameDriver);
		robeBottom.spawn(this, 325, 450);
		
		
		
		//Drop robe top on map
		BlueRobeTop robeTop2 = new BlueRobeTop();
		robeTop2.init(gameDriver);
		robeTop2.spawn(this, 375, 400);
		
		//Drop robe bottom on map
		BlueRobeBottoms robeBottom2 = new BlueRobeBottoms();
		robeBottom2.init(gameDriver);
		robeBottom2.spawn(this, 375, 450);
		
		
		//Drop robe top on map
		YellowRobeTop robeTop4 = new YellowRobeTop();
		robeTop4.init(gameDriver);
		robeTop4.spawn(this, 425, 400);
		
		//Drop robe bottom on map
		YellowRobeBottoms robeBottom4 = new YellowRobeBottoms();
		robeBottom4.init(gameDriver);
		robeBottom4.spawn(this, 425, 450);
		
		//Drop robe top on map
		GreenRobeTop robeTop3 = new GreenRobeTop();
		robeTop3.init(gameDriver);
		robeTop3.spawn(this, 475, 400);
		
		//Drop robe bottom on map
		GreenRobeBottoms robeBottom3 = new GreenRobeBottoms();
		robeBottom3.init(gameDriver);
		robeBottom3.spawn(this, 475, 450);
		
	}
	/** Update the game level */
	
	/** Returns the spawn point X on this level
	 * 
	 * @return the spawn point X coordinate
	 */
	@Override
	public float getSpawnX() {
		return SPAWN_X;
	}

	/** Returns the spawn point Y on this level
	 * 
	 * @return the spawn point Y coordinate
	 */
	@Override
	public float getSpawnY() {
		return SPAWN_Y;
	}
	

}
