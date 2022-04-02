package edu.cs495.game.objects.player.chatbox;

public enum ChatColor {
		/** The border color of the chat box */		
		BORDER(0x000000),
		
		/** The color of overhead dialogue */
		DIALOGUE(0xffffff00),
		
		/** The fill color of the chat box */
		BACKGROUND(0xff888888),
		
		/** The color of the username */
		USERNAME(0xffffffff);
	
	
		/** The value of the color */
		public final int color;

		/**
		 * Initialize the enumerated value with a color
		 * 
		 * @param color - the color value of the enumeration
		 */
		ChatColor(int color) {
			this.color = color;
		}
}
