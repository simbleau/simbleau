package edu.cs495.game.levels;


import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.game.AbstractLevel;
import edu.cs495.engine.game.MapBoundaries;
import edu.cs495.engine.gfx.obj.Font;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.input.Input;
import edu.cs495.game.Game;
import edu.cs495.game.objects.equipment.armor.chests.BlueRobeTop;
import edu.cs495.game.objects.equipment.armor.chests.GreenRobeTop;
import edu.cs495.game.objects.equipment.armor.chests.RedRobeTop;
import edu.cs495.game.objects.equipment.armor.chests.YellowRobeTop;
import edu.cs495.game.objects.equipment.armor.helmets.BluePointyHat;
import edu.cs495.game.objects.equipment.armor.helmets.GreenPointyHat;
import edu.cs495.game.objects.equipment.armor.helmets.ModCrown;
import edu.cs495.game.objects.equipment.armor.helmets.PointyHat;
import edu.cs495.game.objects.equipment.armor.helmets.RedPointyHat;
import edu.cs495.game.objects.equipment.armor.helmets.SpecialCrown;
import edu.cs495.game.objects.equipment.armor.helmets.YellowPointyHat;
import edu.cs495.game.objects.equipment.armor.legs.BlueRobeBottoms;
import edu.cs495.game.objects.equipment.armor.legs.GreenRobeBottoms;
import edu.cs495.game.objects.equipment.armor.legs.RedRobeBottoms;
import edu.cs495.game.objects.equipment.armor.legs.YellowRobeBottoms;
import edu.cs495.game.objects.equipment.gloves.PrimaryLavaGlove;
import edu.cs495.game.objects.equipment.gloves.PrimaryMagicGlove;
import edu.cs495.game.objects.equipment.gloves.SecondaryFireGlove;
import edu.cs495.game.objects.equipment.gloves.SecondaryIceGlove;
import edu.cs495.game.objects.player.PlayerPrivileges;
import edu.cs495.game.objects.scenery.Anvil;
import edu.cs495.game.objects.scenery.LongSign;
import edu.cs495.game.objects.scenery.ShortSign;
import edu.cs495.game.objects.scenery.TargetDummy;
import edu.cs495.game.objects.scenery.estate.BottomWall;
import edu.cs495.game.objects.scenery.estate.Entrance;
import edu.cs495.game.objects.scenery.estate.Fountain;
import edu.cs495.game.objects.scenery.estate.InnerSideWall;
import edu.cs495.game.objects.scenery.estate.OuterSideWall;
import edu.cs495.game.objects.scenery.estate.TopLeftWall;
import edu.cs495.game.objects.scenery.estate.TopRightWall;
import edu.cs495.game.objects.scenery.estate.WindowType1;
import edu.cs495.game.objects.scenery.estate.WindowType2;

public class Estate extends AbstractLevel {

	/** The spawn point X */
	private static final float SPAWN_X = 1022f;

	/** The spawn point Y */
	private static final float SPAWN_Y = 600f;

	/**
	 * Runs one-off operations before the game loop continues
	 * 
	 * @param gameDriver a reference to the Game Driver
	 */
	@Override
	public void init(GameDriver gameDriver) {
		gameDriver.getWindow().setJFrameTitle("Current Level: Estate");
		this.map = new Image("/map/estate/map.png");
		this.boundaries = new MapBoundaries("/map/estate/boundary_map.png");

		this.width = map.getWidth();
		this.height = map.getHeight();
		gameDriver.getRenderer().setMap(map);

		// Add Fountain
		gameDriver.getLevel().addGameObject(new Fountain(1028f, 423f));

		// Add Anvil
		Input localInput = ((Game) gameDriver.getGame()).getPlayer().getInput();
		gameDriver.getLevel().addGameObject(new Anvil(localInput, 700f, 610f));

		// Add Heal Station
		// TODO TeePee?

		// Add DPS dummy
		gameDriver.getLevel().addGameObject(new TargetDummy(1100f, 550f)); // Same pos as Player

		// Add Walls
		gameDriver.getLevel().addGameObject(new BottomWall(1024f, 724f));
		gameDriver.getLevel().addGameObject(new OuterSideWall(630f, 724f));
		gameDriver.getLevel().addGameObject(new OuterSideWall(1413f, 724f));
		gameDriver.getLevel().addGameObject(new InnerSideWall(816f, 547f));
		gameDriver.getLevel().addGameObject(new InnerSideWall(1230f, 547f));
		gameDriver.getLevel().addGameObject(new TopLeftWall(725f, 410f));
		gameDriver.getLevel().addGameObject(new TopRightWall(1323f, 410f));
		gameDriver.getLevel().addGameObject(new Entrance(1023f, 548f));

		// Add Windows
		// Bottom Wall Windows Left -> Right
		gameDriver.getLevel().addGameObject(new WindowType1(727f, 705f));
		gameDriver.getLevel().addGameObject(new WindowType2(859f, 705f));
		gameDriver.getLevel().addGameObject(new WindowType1(1029f, 705f));
		gameDriver.getLevel().addGameObject(new WindowType2(1182f, 705f));
		gameDriver.getLevel().addGameObject(new WindowType1(1323f, 705f));
		// Top Wall Windows
		gameDriver.getLevel().addGameObject(new WindowType1(724f, 391f));
		gameDriver.getLevel().addGameObject(new WindowType1(1323f, 391f));
		// Entrance Windows
		gameDriver.getLevel().addGameObject(new WindowType2(858f, 528f));
		gameDriver.getLevel().addGameObject(new WindowType2(1181f, 528f));

		gameDriver.getLevel().addGameObject(new LongSign("Long sign", Font.SMALL, 900f, 350f));
		gameDriver.getLevel().addGameObject(new ShortSign("short", Font.SMALL, 1150f, 330f));

		test(gameDriver);
		// Last
		for (AbstractGameObject object : levelObjects) {
			object.init(gameDriver);
		}
	}

	// TODO remove// testing
	private void test(GameDriver gameDriver) {
		ModCrown crown = new ModCrown(PlayerPrivileges.ADMIN);
		crown.init(gameDriver);
		crown.spawn(this, 220, 240);

		ModCrown crown2 = new ModCrown(PlayerPrivileges.MODERATOR);
		crown2.init(gameDriver);
		crown2.spawn(this, 250, 240);

		SpecialCrown crown3 = new SpecialCrown();
		crown3.init(gameDriver);
		crown3.spawn(this, 900, 640);

		// Drop robe top on map
		RedRobeTop robeTop = new RedRobeTop();
		robeTop.init(gameDriver);
		robeTop.spawn(this, 325, 400);

		// Drop robe bottom on map
		RedRobeBottoms robeBottom = new RedRobeBottoms();
		robeBottom.init(gameDriver);
		robeBottom.spawn(this, 325, 450);

		// Drop robe top on map
		BlueRobeTop robeTop2 = new BlueRobeTop();
		robeTop2.init(gameDriver);
		robeTop2.spawn(this, 375, 400);

		// Drop robe bottom on map
		BlueRobeBottoms robeBottom2 = new BlueRobeBottoms();
		robeBottom2.init(gameDriver);
		robeBottom2.spawn(this, 375, 450);

		// Drop robe top on map
		YellowRobeTop robeTop4 = new YellowRobeTop();
		robeTop4.init(gameDriver);
		robeTop4.spawn(this, 425, 400);

		// Drop robe bottom on map
		YellowRobeBottoms robeBottom4 = new YellowRobeBottoms();
		robeBottom4.init(gameDriver);
		robeBottom4.spawn(this, 425, 450);

		// Drop robe top on map
		GreenRobeTop robeTop3 = new GreenRobeTop();
		robeTop3.init(gameDriver);
		robeTop3.spawn(this, 475, 400);

		// Drop robe bottom on map
		GreenRobeBottoms robeBottom3 = new GreenRobeBottoms();
		robeBottom3.init(gameDriver);
		robeBottom3.spawn(this, 475, 450);

		PrimaryLavaGlove primaryFireGlove = new PrimaryLavaGlove();
		primaryFireGlove.init(gameDriver);
		primaryFireGlove.spawn(this, 1200, 600);

		SecondaryIceGlove secondaryFrostGlove = new SecondaryIceGlove();
		secondaryFrostGlove.init(gameDriver);
		secondaryFrostGlove.spawn(this, 1200, 650);

		PrimaryMagicGlove primaryMagicGlove = new PrimaryMagicGlove();
		primaryMagicGlove.init(gameDriver);
		primaryMagicGlove.spawn(this, 1250, 625);

		SecondaryFireGlove secondaryFireGlove = new SecondaryFireGlove();
		secondaryFireGlove.init(gameDriver);
		secondaryFireGlove.spawn(this, 1200, 625);

		PointyHat yellowHat = new YellowPointyHat();
		yellowHat.init(gameDriver);
		yellowHat.spawn(this, 1225, 600);

		PointyHat redHat = new RedPointyHat();
		redHat.init(gameDriver);
		redHat.spawn(this, 1250, 600);

		PointyHat greenHat = new GreenPointyHat();
		greenHat.init(gameDriver);
		greenHat.spawn(this, 1275, 600);

		PointyHat blueHat = new BluePointyHat();
		blueHat.init(gameDriver);
		blueHat.spawn(this, 1300, 600);

	}

	/**
	 * Returns the spawn point X on this level
	 * 
	 * @return the spawn point X coordinate
	 */
	@Override
	public float getSpawnX() {
		return SPAWN_X;
	}

	/**
	 * Returns the spawn point Y on this level
	 * 
	 * @return the spawn point Y coordinate
	 */
	@Override
	public float getSpawnY() {
		return SPAWN_Y;
	}

}
