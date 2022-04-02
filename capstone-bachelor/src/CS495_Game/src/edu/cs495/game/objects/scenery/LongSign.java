package edu.cs495.game.objects.scenery;

import edu.cs495.engine.GameDriver;
import edu.cs495.engine.gfx.Renderer;
import edu.cs495.engine.gfx.obj.Font;
import edu.cs495.engine.gfx.obj.Image;

	/** A short sign
	 * 
	 * @version February 2019
	 * @author Spencer Imbleau
	 */
	public class LongSign extends AbstractScenery{
		
	/** The file path for the scenery image */
	private static final String IMAGE_PATH = "/map/common/long_sign.png";

	/** Color of text on the sign */
	private static final int TEXT_COLOR = 0xff000000;
	
	/** Text on the sign */
	protected String text;
	
	/** Font on the sign */
	protected Font font;
	
	/** Image of the string stored in {@link #text} */
	protected Image textImage;
	
	/** Initialize the sign
	 *
	 * @param posX - X world coordinate
	 * @param posY - Y world coordinate
	 */
	public LongSign(String text, Font font, float posX, float posY) {
		super(IMAGE_PATH, posX, posY);
		this.text = text;
		this.font = font;
	}

	/** Set the sign's text and cache the textImage
	 * 
	 * @param text - the text to set
	 */
	public void setText(String text) {
		this.text = text;
		this.textImage = font.getStringImage(text, TEXT_COLOR);
	}
	
	/** Initialize the sign */
	@Override
	public void init(GameDriver gameDriver) {
		super.init(gameDriver);
		this.textImage = font.getStringImage(text, TEXT_COLOR);
	}

	/** Update does nothing, it's static scenery */
	@Override
	public void update(GameDriver gameDriver) {} //Do nothing


	/** Renders the sign */
	@Override
	public void render(GameDriver gameDriver, Renderer renderer) {
		renderer.draw(this.image, (int) offX, (int) offY);
		int renderOrder = (int) offY + this.image.getHeight();
		renderer.draw(textImage, renderOrder, (int)posX - textImage.getHalfWidth(), (int) offY + 10);
	}

}
