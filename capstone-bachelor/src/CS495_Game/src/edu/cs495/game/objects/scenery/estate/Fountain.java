package edu.cs495.game.objects.scenery.estate;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.game.AbstractGameObject;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.SpriteSheet;

public class Fountain extends AbstractGameObject {
	
	/** The file path of the fountain animation sprite sheet */
	private static final String SHEET_PATH = "/map/common/fountain.png";
	
	/** The width of a fountain animation tile */
	private static final int TILE_WIDTH = 133;
	
	/** The height of a fountain animation tile */
	private static final int TILE_HEIGHT = 114;
	
	/** The FPS of the fountain */
	private static final int FPS = 8;
	
	/** The sprite sheet of the object*/
	private SpriteSheet spriteSheet;
	
	/** The amount of frames in the animation */
	private int totalFrames;
	
	/** The frame of the animation */
	private int frame;
	
	/** Initialize the object
	 *
	 * @param posX - X coordinate
	 * @param posY - Y coordinate
	 */
	public Fountain(float posX, float posY) {
		super("(Fountain)", posX, posY, TILE_WIDTH, TILE_HEIGHT);
		this.frame = 0;
		this.totalFrames = 0;
	}
	
	
	/** Initialize the object */
	@Override
	public void init(GameDriver gameDriver) {
		this.spriteSheet = new SpriteSheet(SHEET_PATH, TILE_WIDTH, TILE_HEIGHT);
		
		this.frame = 0;
		this.totalFrames = spriteSheet.getSheetWidth() / spriteSheet.getTileWidth();
		
	}

	/** Update the frame of the fountain */
	@Override
	public void update(GameDriver gameDriver) {
		this.frame = (int) (
				(FPS * gameDriver.getGameTime() / GameDriver.NANO_SECOND)) % totalFrames;
	}


	/** Renders the object */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		renderer.draw(spriteSheet, frame, 0,  (int) posY - 15, (int) offX, (int) offY);
	}

}
