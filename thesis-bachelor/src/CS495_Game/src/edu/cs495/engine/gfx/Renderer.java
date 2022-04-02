/**
 * 
 */
package edu.cs495.engine.gfx;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import edu.cs495.engine.GameDriver;
import edu.cs495.engine.developer.DeveloperLog;
import edu.cs495.engine.game.AbstractCamera;
import edu.cs495.engine.gfx.obj.Box;
import edu.cs495.engine.gfx.obj.Ellipse;
import edu.cs495.engine.gfx.obj.Font;
import edu.cs495.engine.gfx.obj.Image;
import edu.cs495.engine.gfx.obj.Light;
import edu.cs495.engine.gfx.obj.Line;
import edu.cs495.engine.gfx.obj.SpriteSheet;
import edu.cs495.engine.gfx.requests.AbstractRequest;
import edu.cs495.engine.gfx.requests.BoxOverlayRequest;
import edu.cs495.engine.gfx.requests.BoxRequest;
import edu.cs495.engine.gfx.requests.EllipseRequest;
import edu.cs495.engine.gfx.requests.ImageRequest;
import edu.cs495.engine.gfx.requests.LightRequest;
import edu.cs495.engine.gfx.requests.LineOverlayRequest;
import edu.cs495.engine.gfx.requests.LineRequest;
import edu.cs495.engine.gfx.requests.ImageOverlayRequest;

/** The final boss of rendering. This class handles all drawing requests for
 * each type of {@link AbstractRequest} and acts executive of render order.
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class Renderer {
	/** The frame buffer pixel map (pixels are stored in ARGB) */
	private int[] pixels;
	/** The width of the pixel data map */
	private int pixelWidth;
	/** The height of the pixel data map */
	private int pixelHeight;
	
	/** Light Map */
	private int[] lightMap;
	/** Light Blockage Map */
	private float[] lightBlock;
	/** Ambient Color, only used in levels which enable lighting. */
	private int ambientRGB = 0x8ba4a9;
	
	/** The background image */
	private Image background;
	/** Render requests are automatically ordered */
	private ArrayList<AbstractRequest> renderRequests = new ArrayList<>();
	/** UI elements (rendered last & ordering is handled by the user!) */
	private ArrayList<AbstractRequest> overlayRequests = new ArrayList<>(); 
	
	/** Reference to the game driver */
	private GameDriver gameDriver;
	
	
	/** Initializes the renderer 
	 * 
	 * @param gameDriver : the game driver for this renderer */
	public Renderer(GameDriver gameDriver) {
		this.pixelWidth = gameDriver.getGameWidth();
		this.pixelHeight = gameDriver.getGameHeight();
		
		// * The frame buffer
		this.pixels = ((DataBufferInt) gameDriver.getWindow()
				.getImage().getRaster().getDataBuffer()).getData();
		
		this.lightMap = new int[pixels.length];
		this.lightBlock = new float[pixels.length];
		this.gameDriver = gameDriver;
	}	

	/** Clear the pixel data  */
	public void clear() {
		this.pixelWidth = gameDriver.getGameWidth();
		this.pixelHeight = gameDriver.getGameHeight();
		this.pixels = ((DataBufferInt) gameDriver.getWindow()
				.getImage().getRaster().getDataBuffer()).getData();
		if (lightMap.length != pixels.length ||
				lightBlock.length != pixels.length) {
			this.lightMap = new int[pixels.length];
			this.lightBlock = new float[pixels.length];
		}
		
		//Clear pixel map
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0; 
		}
		
		//With lighting, set the ambient
		if (gameDriver.getLevel().hasLighting()) {
			for (int i = 0; i < pixels.length; i++) {
				lightMap[i] = ambientRGB;
				lightBlock[i] = 0;
			}
		}
	}

	/** Processes and draw the game. 
	 * 
	 * Order
	 * > Draw Background
	 * > Draw Non-Overlay Requests
	 * > Draw Overlay Requests
	 * 
	 * @param 
	 */
	public void process(AbstractCamera camera) {
		//Draw background
		if (background != null) {
			drawImage(background, 0, 0); 
		}
		
		//Debug Text
		if (DeveloperLog.isDebugging()) {
			Image fpsString = Font.SMALL.getStringImage("FPS: " + gameDriver.getFPS(), 0xffff0000);
			Image requestString = Font.SMALL.getStringImage("Requests: " + renderRequests.size(), 0xffff0000);
			Image overlaysString = Font.SMALL.getStringImage("Overlays: " + overlayRequests.size(), 0xffff0000);
			Image cameraString = Font.SMALL.getStringImage("Camera: " 
			+ ((int)camera.getCamX()) + ", " + ((int)camera.getCamY()), 0xffff0000);
			drawOverlay(fpsString, Integer.MAX_VALUE, gameDriver.getGameWidth() - fpsString.getWidth(), 1);
			drawOverlay(requestString, Integer.MAX_VALUE, gameDriver.getGameWidth() - requestString.getWidth(), Font.SMALL.SIZE * 1);
			drawOverlay(overlaysString, Integer.MAX_VALUE, gameDriver.getGameWidth() - overlaysString.getWidth(), 1 + Font.SMALL.SIZE * 2);
			drawOverlay(cameraString, Integer.MAX_VALUE, gameDriver.getGameWidth() - cameraString.getWidth(), 1 + Font.SMALL.SIZE * 3);
		} 
		
		//Order requests from bottom to top. (*Desired rendering order*)
		Collections.sort(renderRequests);
		
		//Render the (now) ordered requests
		for (int i = 0; i < renderRequests.size(); i++) {
			AbstractRequest request = renderRequests.get(i);
			renderRequest(request);
		}
		
		//Apply lighting if our level supports it
		if (gameDriver.getLevel().hasLighting()) {
			renderLighting();
		}
		
		//Order overlay requests
		Collections.sort(overlayRequests);
		//Render the overlays (screen elements)
		for (int i = 0; i < overlayRequests.size(); i++) {
			AbstractRequest request = overlayRequests.get(i);
			renderOverlayRequest(request);
		}
		
		//Clear it for the next loop
		renderRequests.clear();
		overlayRequests.clear();
	}
	
	/** Helper method which routes and handles the request to render a request 
	 * 
	 * @param request - An {@link AbstractRequest} to draw
	 */
	private void renderRequest(AbstractRequest request) {
		if (request instanceof ImageRequest) {
			//IMAGE REQUESTS
			ImageRequest imageRequest = (ImageRequest) request;
			if (DeveloperLog.isDebugging()) {
				drawBox(imageRequest.offX, 
						imageRequest.offY, 
						imageRequest.image.getWidth(), 
						imageRequest.image.getHeight(), 
						0x4fff0000, false);
			}
			//Draw image
			drawImage(imageRequest.image, 
					imageRequest.offX, 
					imageRequest.offY);
			
		} else if (request instanceof LightRequest) {
			if (gameDriver.getLevel().hasLighting()) {
				//LIGHT REQUESTS
				LightRequest lightRequest = (LightRequest) request;
				Light light = lightRequest.light;
				for (int j = 0; j <= light.diameter; j++) {
					drawLightLine(
							light, 
							light.radius, light.radius, 
							j, 0, 
							lightRequest.offX, lightRequest.offY);
					drawLightLine(
							light, 
							light.radius, light.radius, 
							j, light.diameter, 
							lightRequest.offX, lightRequest.offY);
					drawLightLine(
							light, 
							light.radius, light.radius, 
							0, j, 
							lightRequest.offX, lightRequest.offY);
					drawLightLine(
							light, 
							light.radius, light.radius, 
							light.diameter, j, 
							lightRequest.offX, lightRequest.offY);
				}
			}
			
		} else if (request instanceof BoxRequest) {
			//BOX REQUESTS
			BoxRequest boxRequest = (BoxRequest) request;
			Box box = boxRequest.box;
			drawBox(boxRequest.offX, boxRequest.offY, 
					box.getWidth(), box.getHeight(), 
					box.getArgb(), box.isFull());
		} else if (request instanceof EllipseRequest) {
			//ELLIPSE REQUESTS
			EllipseRequest ellipseRequest = (EllipseRequest) request;
			Ellipse ellipse = ellipseRequest.ellipse;
			drawEllipse(ellipseRequest.offX, ellipseRequest.offY,
					ellipse.getWidth(), ellipse.getHeight(),
					ellipse.getArgb(), ellipse.isFull());
		} else if (request instanceof LineRequest) {
			//Line Requests
			LineRequest lineRequest = (LineRequest) request;
			drawLine(lineRequest);
		}
	}
	
	/** Helper method which routes and handles the request to render a request 
	 * 
	 * @param request - An {@link AbstractRequest} to draw
	 */
	private void renderOverlayRequest(AbstractRequest request) {
		if (request instanceof ImageOverlayRequest) {
			//IMAGE OVERLAY REQUESTS
			ImageOverlayRequest imageRequest = (ImageOverlayRequest) request;
			drawImageOverlay(imageRequest.IMAGE, 
					imageRequest.X, 
					imageRequest.Y);
			if (DeveloperLog.isDebugging()) {
				drawBoxOverlay( 
						imageRequest.X, 
						imageRequest.Y,
						imageRequest.IMAGE.getWidth(),
						imageRequest.IMAGE.getHeight(),
						0x4f0000f,
						false);
			}
		} else if (request instanceof BoxOverlayRequest) {
			//Box overlay requests
			BoxOverlayRequest boxRequest = (BoxOverlayRequest) request;
			drawBoxOverlay( 
					boxRequest.X, 
					boxRequest.Y, 
					boxRequest.BOX.getWidth(),
					boxRequest.BOX.getHeight(),
					boxRequest.BOX.getArgb(),
					boxRequest.BOX.isFull());
		} else if (request instanceof LineOverlayRequest) {
			//Line overlay requests
			LineOverlayRequest lineRequest = (LineOverlayRequest) request;
			drawLineOverlay(lineRequest);
		} else {
			DeveloperLog.errLog("Request: " + request.getClass().getSimpleName()
					+ " is not an overlay request and was skipped");
		}
	}
	
	/** Helper method which applies light blending
	 * 
	 * @param request - An {@link AbstractRequest} to draw
	 */
	private void renderLighting() {
		//Render Light requests
		for (int i = 0; i < pixels.length; i++) {
			//Get the color of the pixel
			int r = (pixels[i] >> 16 ) & 0xff;
			int g = (pixels[i] >> 8 ) & 0xff;
			int b = pixels[i] & 0xff;
			
			//Get light power from {@link #lightMap} that corresponds to the 
			//same pixel
			float rPower = ((lightMap[i] >> 16) & 0xff) / 255f;
			float gPower = ((lightMap[i] >> 8) & 0xff) / 255f;
			float bPower = (lightMap[i] & 0xff) / 255f;
			
			//Blend the two
			pixels[i]= ((int)(r * rPower) << 16) | 
					((int)(g * gPower) << 8) | 
					(int)(b * bPower); 
		}
	}
	
	/** Sets the pixel at a point with an ARGB value
	 * 
	 * @param x - The x coordinate
	 * @param y - The y coordinate
	 * @param argb - An ARGB color value (i.e. 0xff00ff00 = Green) 
	 */
	private void setPixel(int x, int y, int argb) {
		//If it's out of bounds, we can end our quest
		if ((x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight)) {
			return;
		}
		
		//Get our alpha value
		int alpha = ((argb >> 24) & 0xff);
		
		if (alpha == 0x00) { 
			//0x00 (0) is fully transparent
			return;
		} else if (alpha == 0xff) { 
			// 0xff (255) is fully opaque
			pixels[x + y  * pixelWidth] = argb;	
		} else { 
			//Frame blending
			
			//Get pixel color
			int index = x + y * pixelWidth;
			int pixelColor = pixels[index];
			  
			//Find new red
			int redOld = ((pixelColor >> 16) & 0xff);
			int redVal = ((argb >> 16) & 0xff);
			int redNew = redOld - (int)((redOld - redVal) * (alpha / 255f));
			
			//Find new green
			int greenOld = ((pixelColor >> 8) & 0xff);
			int greenVal = ((argb >> 8) & 0xff);
			int greenNew = greenOld - (int)((greenOld - greenVal) * (alpha / 255f));
			
			//Find new blue
			int blueOld = (pixelColor & 0xff);
			int blueVal = (argb & 0xff);
			int blueNew = blueOld - (int)((blueOld - blueVal) * (alpha / 255f));
			
			//Format the color to be ARGB
			int newColor = (0xff000000 | redNew << 16 | greenNew << 8 | blueNew);
		
			//Load it
			pixels[x + y  * pixelWidth] = newColor;	
		}
		
	}
	
	/** Sets the background image
	 * 
	 * @param image - The background image 
	 */
	public void setMap(Image image) {
		this.background = image;
	}
	
	/** Sets a value of the light map
	 * 
	 * @param x - the x position in the light map
	 * @param y - the y position in the light map
	 * @param value - the new value to insert
	 */
	private void setLightMap(int x, int y, int value) {
		//Don't render off-screen lights
		if (x < 0) return;
		if (x >= pixelWidth) return;
		if (y < 0) return;
		if (y >= pixelHeight) return;
	
		int colorOld = lightMap[x + y * pixelWidth]; 
		
		int redNew = Math.max((colorOld >> 16) & 0xff, (value >> 16) & 0xff);
		int greenNew = Math.max((colorOld >> 8) & 0xff, (value >> 8) & 0xff);
		int blueNew = Math.max(colorOld & 0xff, value & 0xff);
		
		int colorNew = (redNew << 16 | greenNew << 8 | blueNew);
		
		lightMap[x + y * pixelWidth] = colorNew;
	}
	
	/** Sets a value of the light block
	 * 
	 * @param x - the x position in the light block map
	 * @param y - the y position in the light block map
	 * @param value - the new value to insert
	 */
	private void setLightBlock(int x, int y, float value) {
		//Don't render off-screen lights
		if (x < 0) return;
		if (x >= pixelWidth) return;
		if (y < 0) return;
		if (y >= pixelHeight) return;
	
		lightBlock[x + y * pixelWidth] = value;
	}
	
	
	//===============
	// DRAWS BELOW
	//===============
	
	/** (Draw is an overloaded method and has many implementations)
	 * This draw method will create an ImageRequest and add it to 
	 * the list of render requests. Render requests are actually drawn
	 * to the canvas during processing.
	 * 
	 * @see process()
	 * @param image - The image to draw
	 * @param offX - The x coordinate to draw image at (from top left)
	 * @param offY - The y coordinate to draw image at (from top left)
	 */
	public void draw(Image image, int offX, int offY) {
		AbstractRequest request = new ImageRequest(image, offX, offY);
		renderRequests.add(request);
	}
	
	/** (Draw is an overloaded method and has many implementations)
	 * This draw method will create an ImageRequest with a specific render order
	 * and add it to the list of render requests. 
	 * Render requests are actually drawn to the canvas during processing.
	 * 
	 * @see process()
	 * @param image - The image to draw
 	 * @param renderOrder - override the default render order
	 * @param offX - The x coordinate to draw image at (from top left)
	 * @param offY - The y coordinate to draw image at (from top left)
	 */
	public void draw(Image image, int renderOrder, int offX, int offY) {
		AbstractRequest request = new ImageRequest(image, renderOrder, offX, offY);
		renderRequests.add(request);
	}
	
	
	/** (Draw is an overloaded method and has many implementations)
	 * This draw method will create an ImageRequest from a sprite and add it to 
	 * the list of render requests. Render requests are actually drawn
	 * to the canvas during processing.
	 * 
	 * @see process()
	 * @param spriteSheet - The sprite sheet being used 
	 * @param tileX - the X coordinate tile
	 * @param tileY - the y coordinate tile
	 * @param offX - the x coordinate to render at
	 * @param offY - the y coordinate to render at
	 */
	public void draw(SpriteSheet spriteSheet, 
			int tileX, int tileY, int offX, int offY) {
		Image sprite = new Image(
				spriteSheet.getTileWidth(), 
				spriteSheet.getTileHeight(), 
				spriteSheet.get(tileX, tileY));
		AbstractRequest request = new ImageRequest
				(sprite, offX, offY);
		renderRequests.add(request);
	}
	
	
	/** (Draw is an overloaded method and has many implementations)
	 * This draw method will create an ImageRequest from a sprite with a
	 * specific render order and add it to the list of render requests. 
	 * Render requests are actually drawn to the canvas during processing.
	 * 
	 * @see process()
	 * @param spriteSheet - The sprite sheet being used 
	 * @param tileX - the X coordinate tile
	 * @param tileY - the y coordinate tile
	 * @param renderOrder - override the default render order
	 * @param offX - the x coordinate to render at
	 * @param offY - the y coordinate to render at
	 */
	public void draw(SpriteSheet spriteSheet, 
			int tileX, int tileY, int renderOrder, int offX, int offY) {
		Image sprite = new Image(
				spriteSheet.getTileWidth(), 
				spriteSheet.getTileHeight(), 
				spriteSheet.get(tileX, tileY));
		AbstractRequest request = new ImageRequest
				(sprite, renderOrder, offX, offY);
		renderRequests.add(request);
	}
	
	/** (Draw is an overloaded method and has many implementations)
	 * This draw method will create an ImageRequest from a string and font 
	 * with a specific render order and add it to the list of render requests. 
	 * Render requests are actually drawn to the canvas during processing.
	 * 
	 * NOTE: This is kinda horribly inefficient remaking the font image every
	 * time, so only use this for debugging
	 * 
	 * @see process()
	 * @param text - text to render 
	 * @param font - the font to render the text in
	 * @param argb - the ARGB formatted color of the string when rendered
	 * @param renderOrder - override the default render order
	 * @param offX - the x coordinate to render at
	 * @param offY - the y coordinate to render at
	 */
	public void draw(String text, Font font, int argb, int renderOrder, 
			int offX, int offY) {
		Image fontImage = font.getStringImage(text, argb);
		
		AbstractRequest request = new ImageRequest
				(fontImage, renderOrder, offX, offY);
		renderRequests.add(request);
	}
	
	/** (Draw is an overloaded method and has many implementations)
	 * This draw method will create an BoxRequest from a box and
	 * add it to the list of render requests. Render requests are actually 
	 * drawn to the canvas during processing.
	 * 
	 * @see process()
	 * @param box - the box to render 
	 * @param offX - the x coordinate to render at
	 * @param offY - the y coordinate to render at
	 */
	public void draw(Box box, int offX, int offY) {
		AbstractRequest request = new BoxRequest(box, offX, offY);
		renderRequests.add(request);
	}
	
	/** (Draw is an overloaded method and has many implementations)
	 * This draw method will create an BoxRequest from a box with a specific
	 * render ordering and add it to the list of render requests. 
	 * Render requests are actually drawn to the canvas during processing.
	 * 
	 * @see process()
	 * @param box - the box to render 
	 * @param renderOrder - override the default render order
	 * @param offX - the x coordinate to render at
	 * @param offY - the y coordinate to render at
	 */
	public void draw(Box box, int renderOrder, int offX, int offY) {
		AbstractRequest request = new BoxRequest(box, renderOrder, offX, offY);
		renderRequests.add(request);
	}
	
	/** (Draw is an overloaded method and has many implementations)
	 * This draw method will create an LineRequest from a line and add it to the list of render requests. 
	 * Render requests are actually drawn to the canvas during processing.
	 * 
	 * @see process()
	 * @param line - the line to render 
	 * @param offX - the x coordinate to render at
	 * @param offY - the y coordinate to render at
	 */
	public void draw(Line line, int offX, int offY) {
		AbstractRequest request = new LineRequest(line, offX, offY);
		renderRequests.add(request);
	}
	
	/** (Draw is an overloaded method and has many implementations)
	 * This draw method will create an LineRequest from a line with a specific
	 * render ordering and add it to the list of render requests. 
	 * Render requests are actually drawn to the canvas during processing.
	 * 
	 * @see process()
	 * @param line - the line to render 
	 * @param renderOrder - override the default render order
	 * @param offX - the x coordinate to render at
	 * @param offY - the y coordinate to render at
	 */
	public void draw(Line line, int renderOrder, int offX, int offY) {
		AbstractRequest request = new LineRequest(line, renderOrder, offX, offY);
		renderRequests.add(request);
	}
	
	
	
	/** (Draw is an overloaded method and has many implementations)
	 * This draw method will create an EllipseRequest from an ellipse and
	 * add it to the list of render requests. Render requests are actually 
	 * drawn to the canvas during processing.
	 * 
	 * @see process()
	 * @param ellipse - the ellipse to render 
	 * @param centerX - the center x coordinate
	 * @param centerY - the center y coordinate
	 */
	public void draw(Ellipse ellipse, int offX, int offY) {
		AbstractRequest request = new EllipseRequest(ellipse, offX, offY);
		renderRequests.add(request);
	}
	
	/** (Draw is an overloaded method and has many implementations)
	 * This draw method will create an EllipseRequest from an ellipse with a 
	 * specific render ordering and add it to the list of render requests. 
	 * Render requests are actually drawn to the canvas during processing.
	 * @see process()
	 * 
	 * @param ellipse - the ellipse to render 
	 * @param renderOrder - override the default render order
	 * @param centerX - the center x coordinate
	 * @param centerY - the center y coordinate
	 */
	public void draw(Ellipse ellipse, int renderOrder, int offX, int offY) {
		AbstractRequest request = 
				new EllipseRequest(ellipse, renderOrder, offX, offY);
		renderRequests.add(request);
	}
	
	/** (Draw is an overloaded method and has many implementations)
	 * This draw method will create a LightRequest from a light and add it to 
	 * the list of render requests. Render requests are actually drawn to the 
	 * canvas during processing.
	 * 
	 * @see process()
	 * @param light - the light to render 
	 * @param offX - the x coordinate to render at
	 * @param offY - the y coordinate to render at
	 */
	public void draw(Light light, int offX, int offY) {
		AbstractRequest request = new LightRequest(light,  offX,  offY);
		renderRequests.add(request);
	}
	
	/** (Draw is an overloaded method and has many implementations)
	 * This draw method will create a LightRequest from a light  with a specific
	 * render order and add it to the list of render requests. Render requests 
	 * are actually drawn to the canvas during processing.
	 * 
	 * @see process()
	 * @param light - the light to render 
	 * @param renderOrder - override the default render order
	 * @param offX - the x coordinate to render at
	 * @param offY - the y coordinate to render at
	 */
	public void draw(Light light, int renderOrder, int offX, int offY) {
		AbstractRequest request = new LightRequest(light, renderOrder, offX, offY);
		renderRequests.add(request);
	}
	
	//===============
	// OVERLAYS BELOW
	//===============
	
	/** (DrawOverlay is an overloaded method and has many implementations)
	 * This draw method will create an OverlayRequest from an Image and add it
	 * to the list of OverlayRequests. Overlays are drawn during processing. 
	 * are actually drawn to the canvas during processing.
	 * 
	 * @param image - the image to draw 
	 * @param renderOrder - the render order of the image
	 * @param x - x coordinate offset from the left side of the screen
	 * @param y - y coordinate offset from the top side of the screen
	 */
	public void drawOverlay(Image image, int renderOrder, int x, int y) {
		ImageOverlayRequest request = new ImageOverlayRequest(image, renderOrder, x, y);
		overlayRequests.add(request);
	}
	
	/** (DrawOverlay is an overloaded method and has many implementations)
	 * This draw method will create an OverlayRequest from a sprite and add it
	 * to the list of OverlayRequests. Overlays are drawn during processing. 
	 * are actually drawn to the canvas during processing.
	 * 
	 * @param spriteSheet - the sprite sheet the sprite comes from
	 * @param tileX - the x coordinate tile in the sprite sheet
	 * @param tileY - the y coordinate tile in the sprite sheet 
	 * @param renderOrder - the render order of the sprite
	 * @param x - x coordinate offset from the left side of the screen
	 * @param y - y coordinate offset from the top side of the screen
	 */
	public void drawOverlay(SpriteSheet spriteSheet, int tileX, int tileY,
			int renderOrder, int x, int y) {
		Image sprite = new Image(
				spriteSheet.getTileWidth(),
				spriteSheet.getTileHeight(),
				spriteSheet.get(tileX, tileY));
		ImageOverlayRequest request = new ImageOverlayRequest(sprite, renderOrder, x, y);
		overlayRequests.add(request);
	}
	
	/** (DrawOverlay is an overloaded method and has many implementations)
	 * This draw method will create an OverlayRequest from a box and add it
	 * to the list of OverlayRequests. Overlays are drawn during processing. 
	 * are actually drawn to the canvas during processing.
	 * 
	 * @param box - the box to draw 
	 * @param renderOrder - the render order of the box
	 * @param x - x coordinate offset from the left side of the screen
	 * @param y - y coordinate offset from the top side of the screen
	 */
	public void drawOverlay(Box box, int renderOrder, int x, int y) {
		BoxOverlayRequest request = new BoxOverlayRequest(box, renderOrder, x, y);
		overlayRequests.add(request);
	}

	
	/** (DrawOverlay is an overloaded method and has many implementations)
	 * This draw method will create an OverlayRequest from a String and add it
	 * to the list of OverlayRequests. Overlays are drawn during processing. 
	 * are actually drawn to the canvas during processing.
	 * 
	 * @param text - the text to render
	 * @param font - the font to render the text in
	 * @param argb - the color of the font
	 * @param renderOrder - the render order of the text
	 * @param x - x coordinate offset from the left side of the screen
	 * @param y - y coordinate offset from the top side of the screen
	 */
	public void drawOverlay(String text, Font font, int argb, int renderOrder,
			int x, int y) {
		Image fontImage = font.getStringImage(text, argb);
		ImageOverlayRequest request = new ImageOverlayRequest(fontImage, renderOrder, x, y);
		overlayRequests.add(request);
	}
	
	/** (DrawOverlay is an overloaded method and has many implementations)
	 * This draw method will create an LineOverlayRequest from a Line and add it
	 * to the list of OverlayRequests. Overlays are drawn during processing. 
	 * are actually drawn to the canvas during processing.
	 * 
	 * @param image - the image to draw 
	 * @param renderOrder - the render order of the image
	 * @param x - x coordinate offset from the left side of the screen
	 * @param y - y coordinate offset from the top side of the screen
	 */
	public void drawOverlay(Line line, int renderOrder, int x, int y) {
		LineOverlayRequest request = new LineOverlayRequest(line, renderOrder, x, y);
		overlayRequests.add(request);
	}
	
	
	//========================================
	//Private helper methods below for drawing
	//========================================
	
	/** (Helper Method) Draws an image onto the game canvas
	 * 
	 * @param image - The image to draw
	 * @param offX - The x coordinate to draw image at (from top left)
	 * @param offY - The y coordinate to draw image at (from top left)
	 */
	private void drawImage(Image image, int offX, int offY) {
		//Offset cameras
		offX -= ((int) gameDriver.getGame().getCamera().getCamX());
		offY -= ((int) gameDriver.getGame().getCamera().getCamY());
		
		//Don't render off-screen images
		if (offX < -image.getWidth()) return;
		if (offX > pixelWidth) return;
		if (offY < -image.getHeight()) return;
		if (offY > pixelHeight) return;
		
		//In the next steps we specify the bounds which are visible on screen
		int xStart = 0;
		int yStart = 0;
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();
		
		//Transform those bounds
		if (offX < 0) { xStart -= offX; } //-X drawing efficiency
		if (offY < 0) {	yStart -= offY; } //-Y drawing efficiency
		if (imgWidth + offX > pixelWidth) { 
			imgWidth -= imgWidth + offX - pixelWidth; //+X drawing efficiency
		} 
		if (imgHeight + offY > pixelHeight) {
			imgHeight -= imgHeight + offY - pixelHeight; //+Y drawing efficiency
		} 
		
		//Draw pixels
		int yWidth = 0;
		for (int y = yStart; y < imgHeight; y++) {
			yWidth = y * image.getWidth();
			for (int x = xStart; x < imgWidth; x++) {
				setPixel(x + offX, y + offY, image.getPixels()[x + yWidth]);
			}
		}
	}
	
	/** (Helper Method) Draws an image onto the game canvas as an overlay
	 * 
	 * @param image - The image to draw
	 * @param offX - The x coordinate to draw image at (from top left)
	 * @param offY - The y coordinate to draw image at (from top left)
	 */
	private void drawImageOverlay(Image image, int x, int y) {
		//Don't render off-screen images
		if (x < -image.getWidth()) return;
		if (x > pixelWidth) return;
		if (y < -image.getHeight()) return;
		if (y > pixelHeight) return;
		
		//In the next steps we specify the bounds which are visible on screen
		int xStart = 0;
		int yStart = 0;
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();
		
		//Transform those bounds
		if (x < 0) { xStart -= x; } //-X drawing efficiency
		if (y < 0) {	yStart -= y; } //-Y drawing efficiency
		if (imgWidth + x > pixelWidth) { 
			imgWidth -= imgWidth + x - pixelWidth; //+X drawing efficiency
		} 
		if (imgHeight + y > pixelHeight) {
			imgHeight -= imgHeight + y - pixelHeight; //+Y drawing efficiency
		} 
		
		//Draw pixels
		int yWidth = 0;
		for (int screenY = yStart; screenY < imgHeight; screenY++) {
			yWidth = screenY * image.getWidth();
			for (int screenX = xStart; screenX < imgWidth; screenX++) {
				setPixel(screenX + x, screenY + y, image.getPixels()[screenX + yWidth]);
			}
		}
	}
	
	/** (Helper Method) Draws a box onto the game canvas
	 * 
	 * @param offX - The x coordinate to draw at (from top left)
	 * @param offY - The y coordinate to draw at (from top left)
	 * @param width - the width of the box
	 * @param height - the height of the box
	 * @param argb - the color in ARGB format of the box
	 * @param full - true if full, false if hollow
	 */
	private void drawBox(int offX, int offY, 
			int width, int height, 
			int argb, boolean full) {
		offX -= ((int) gameDriver.getGame().getCamera().getCamX());
		offY -= ((int) gameDriver.getGame().getCamera().getCamY());

		// Don't render off-screen rectangles
		if (offX < -width) return;
		if (offX > pixelWidth) return;
		if (offY < -height)	return;
		if (offY > pixelHeight)	return;

		// In the next steps we specify the bounds which are visible on screen
		int xStart = 0;
		int yStart = 0;
		// Specify those bounds
		if (offX < 0) xStart -= offX; // -X drawing efficiency
		if (offY < 0) yStart -= offY; // -Y drawing efficiency
		if (width + offX > pixelWidth) width -= width + offX - pixelWidth; // +X drawing efficiency
		if (height + offY > pixelHeight) height -= height + offY - pixelHeight; // +Y drawing efficiency

		if (full) {
			//Full rectangles
			for (int y = yStart; y < height; y++) {
				for (int x = xStart; x < width; x++) {
					setPixel(x + offX, y + offY, argb);
				}
			}
		} else {
			//Hollow rectangles
			for (int y = yStart; y < height; y++) {
				setPixel(offX, y + offY, argb);
				setPixel(offX + (width - 1), y + offY, argb);
			}
			for (int x = xStart; x < width; x++) {
				setPixel(x + offX, offY, argb);
				setPixel(x + offX, offY + (height - 1), argb);
			}
		}
	}
	
	
	/** (Helper Method) Draws a box onto the game canvas as an overlay
	 * 
	 * @param offX - The x coordinate to draw at (from top left)
	 * @param offY - The y coordinate to draw at (from top left)
	 * @param width - the width of the box
	 * @param height - the height of the box
	 * @param argb - the color in ARGB format of the box
	 * @param full - true if full, false if hollow
	 */
	private void drawBoxOverlay(int offX, int offY, 
			int width, int height, 
			int argb, boolean full) {

		// Don't render off-screen rectangles
		if (offX < -width) return;
		if (offX > pixelWidth) return;
		if (offY < -height)	return;
		if (offY > pixelHeight)	return;

		// In the next steps we specify the bounds which are visible on screen
		int xStart = 0;
		int yStart = 0;
		// Specify those bounds
		if (offX < 0) xStart -= offX; // -X drawing efficiency
		if (offY < 0) yStart -= offY; // -Y drawing efficiency
		if (width + offX > pixelWidth) width -= width + offX - pixelWidth; // +X drawing efficiency
		if (height + offY > pixelHeight) height -= height + offY - pixelHeight; // +Y drawing efficiency

		if (full) {
			//Full rectangles
			for (int y = yStart; y < height; y++) {
				for (int x = xStart; x < width; x++) {
					setPixel(x + offX, y + offY, argb);
				}
			}
		} else {
			//Hollow rectangles
			for (int y = yStart; y < height; y++) {
				setPixel(offX, y + offY, argb);
				setPixel(offX + (width - 1), y + offY, argb);
			}
			for (int x = xStart; x < width; x++) {
				setPixel(x + offX, offY, argb);
				setPixel(x + offX, offY + (height - 1), argb);
			}
		}
	}
	
	
	/** (Helper Method) Draws a light ray onto the game canvas
	 * 
	 * @param light - the light from which this ray is from
	 * @param x0 - Light Line From X Position
	 * @param y0 - Light Line From Y Position 
	 * @param x1 - Light Line To X Position
	 * @param y1 - Light Line To Y Position
	 * @param offX - Offset x render position
	 * @param offY - Offset y render position 
	 */
	public void drawLightLine(Light light, int x0, int y0, int x1, int y1, 
			int offX, int offY) {
		int deltaX = Math.abs(x1 - x0);
		int deltay = Math.abs(y1 - y0);
		
		int xDirection = x0 < x1 ? 1 : -1;
		int yDirection = y0 < y1 ? 1 : -1;
		
		int err = deltaX - deltay;
		int err2;
		
		while (true) {
			//Get next pixel position
			int xPos = x0 - light.radius + offX;
			int yPos = y0 - light.radius + offY;
			
			xPos -= ((int)gameDriver.getGame().getCamera().getCamX());
			yPos -= ((int)gameDriver.getGame().getCamera().getCamY());
			
			
			//Are we out of bounds? 
			if (xPos < 0 || xPos >= pixelWidth || 
					yPos < 0 || yPos >= pixelHeight) {
				return;
			}
			
			//Check if we're light-blocked
			if (lightBlock[xPos + yPos * pixelWidth] == 1) {
				return;
			}
			
			//Get the light color at the value of (x,y) in our Light's LightMap
			int lightColor = light.getLightValue(x0,  y0);
			if (lightColor == 0) { 
				return;
			}
			
			//Set light map value to it's corresponding lightmap pixel
			setLightMap(xPos,  yPos, light.lightMap[x0 + y0 * light.diameter]);
			
			//If we reach the end of the line, return
			if (x0 == x1 && y0 == y1)
				break; //Successful line
			
			
			//Move towards the end of our line 
			//(Uses int operations which are cheaper computationally)
			err2 = err * 2;
			
			if (err2 > -1 * deltay) {
				err -= deltay;
				x0 += xDirection;
			}
			
			if (err2 < deltaX) {
				err += deltaX;
				y0 += yDirection;
			}
		
		}
	}
	
	/** (Helper Method) Draws a line onto the game canvas
	 * 
	 * @param lineRequest - a request for a line
	 */
	private void drawLine(LineRequest lineRequest) {
		Line line = lineRequest.line;
		
		int xDirection = line.getDx() > 0 ? 1 : -1;
		int yDirection = line.getDy() > 0 ? 1 : -1;
		
		int err = Math.abs(line.getDx()) - Math.abs(line.getDy());
		int err2;
		

		//Get position
		int x = lineRequest.offX;
		int y = lineRequest.offY;
		x -= ((int)gameDriver.getGame().getCamera().getCamX());
		y -= ((int)gameDriver.getGame().getCamera().getCamY());
		
		
		int dx = 0;
		int dy = 0; 
		
		while (Math.abs(dx) < Math.max(1, Math.abs(line.getDx()))
				&& Math.abs(dy) < Math.max(1, Math.abs(line.getDy()))) {
			int nextX = x + dx;
			int nextY = y + dy;
			//Are we out of bounds? 
			if (nextX < 0 || nextX >= pixelWidth || 
					nextY < 0 || nextY >= pixelHeight) {
				return;
			}
			
			//Set light map value to it's corresponding lightmap pixel
			setPixel(nextX,  nextY, lineRequest.line.getArgb());
			
			//Move towards the end of our line 
			//(Uses int operations which are cheaper computationally)
			err2 = err * 2;
			
			if (err2 > -1 * Math.abs(line.getDy())) {
				err -= Math.abs(line.getDy());
				dx += xDirection;
			}
			
			if (err2 < Math.abs(line.getDx())) {
				err += Math.abs(line.getDx());
				dy += yDirection;
			}
		}
	}
	
	/** (Helper Method) Draws a line overlay onto the game canvas
	 * 
	 * @param lineRequest - a request for a line overlay
	 */
	private void drawLineOverlay(LineOverlayRequest lineRequest) {
		Line line = lineRequest.line;
		
		int xDirection = line.getDx() > 0 ? 1 : -1;
		int yDirection = line.getDy() > 0 ? 1 : -1;
		
		int err = Math.abs(line.getDx()) - Math.abs(line.getDy());
		int err2;
		

		//Get position
		int x = lineRequest.x;
		int y = lineRequest.y;
		
		
		int dx = 0;
		int dy = 0; 
		
		while (Math.abs(dx) <= Math.abs(line.getDx())
				&& Math.abs(dy) <= Math.abs(line.getDy())) {
			int nextX = x + dx;
			int nextY = y + dy;
			//Are we out of bounds? 
			if (nextX < 0 || nextX >= pixelWidth || 
					nextY < 0 || nextY >= pixelHeight) {
				return;
			}
			
			//Set light map value to it's corresponding lightmap pixel
			setPixel(nextX,  nextY, lineRequest.line.getArgb());
			
			//Move towards the end of our line 
			//(Uses int operations which are cheaper computationally)
			err2 = err * 2;
			
			if (err2 > -1 * Math.abs(line.getDy())) {
				err -= Math.abs(line.getDy());
				dx += xDirection;
			}
			
			if (err2 < Math.abs(line.getDx())) {
				err += Math.abs(line.getDx());
				dy += yDirection;
			}
		
		}
	}
	
	
	/** Horrible method, ignore this. It will be replaced eventually.
	 *
	 *Documentation below for future purpose...
	 * (Helper Method) Draws an ellipse onto the game canvas (efficiently)
	 * 
	 * @param centerX - The center X coordinate
	 * @param centerY - The center Y coordinate
	 * @param width - the width of the ellipse
	 * @param height - the height of the ellipse
	 * @param argb - the color in ARGB format of the ellipse
	 * @param full - true if full, false if hollow
	 */
	private void drawEllipse(int offX, int offY, int width,int height, int argb, boolean full){
		offX -= ((int) gameDriver.getGame().getCamera().getCamX());
		offY -= ((int) gameDriver.getGame().getCamera().getCamY() + height / 2);

		// Don't render off-screen rectangles
		if (offX < -width) return;
		if (offX > pixelWidth) return;
		if (offY < -height)	return;
		if (offY > pixelHeight)	return;
		
        int w2 = width*width;
        int h2 = height*height;
        int two_w2 = 2 * w2;
        int two_h2 = 2 * h2;
       
        int x = 0;
        int y = height;
        
        int p;
        int px = 0;
        int py = two_w2 * y;
        
        int centerX = offX + width / 2;
        int centerY = offY + height / 2;
        
        EllipsePlotpoint(centerX, centerY,x,y, argb, full);   
       
        // Region 1
        // Finds the pixels in region 1, where tan < 1
        p = (int) (h2 - (w2 * height) + (0.25 + w2));
        while(px < py){
            x++;
            px += two_h2;
            if(p < 0){
                p += h2 + px;
            }
            else{
                y--;
                py -= two_w2;
                p += h2 + px - py;
            }
            EllipsePlotpoint(centerX, centerY,x,y, argb, full);   
        }
       
        /* Region2 */
        p = (int) (h2 * (x + 0.5) * (x + 0.5) + 
        		w2 * (y - 1) * (y - 1) - w2 * h2);
       while(y > 0){
            y--;
            py -= two_w2;
            if(p > 0){
                p += w2 - py;
            }
            else{
                x++;
                px += two_h2;
                p += w2 + px - py;
            }
            EllipsePlotpoint(centerX,centerY,x,y, argb, full);   
     }
             
    }
   
	/** Horrible method, ignore this. It will be replaced eventually. */
    public void EllipsePlotpoint(int centerX, int centerY, int x, int y, int argb, boolean full){ 	 
         setPixel(centerX + x, centerY + y, argb);
         setPixel(centerX + x, centerY - y, argb);
         setPixel(centerX - x, centerY + y, argb);
         setPixel(centerX - x, centerY - y, argb);
         if (full) {
             for (int i = 0; i < y; i++) {
                 setPixel(centerX + x, centerY + i, argb);
                 setPixel(centerX + x, centerY - i, argb);
                 setPixel(centerX - x, centerY + i, argb);
                 setPixel(centerX - x, centerY - i, argb);
             }
         }
    	 
      
    }
		
	
	
}
