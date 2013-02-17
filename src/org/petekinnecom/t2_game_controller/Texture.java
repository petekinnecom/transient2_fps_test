package org.petekinnecom.t2_game_controller;


import android.graphics.Bitmap;

public class Texture
{
	
	/*
	 * 
	 * This class serves as a general purpose
	 * holder for all things that will be passed to 
	 * the renderer.  
	 * 
	 * It contains the minimum information necessary
	 * to draw to screen.  Any other information needs
	 * to be contained in the subclass.
	 */
	
	public Bitmap png;
	public int x, y;
	
	//-1 for horizontal reflection
	public int reflect = 1;
	
	//increments of 90
	public int rotation = 0;
	
	public Texture(Bitmap b, int x, int y, int reflect, int rotation)
	{
		this.png = b;
		this.x = x;
		this.y = y;
		this.reflect = reflect;
		this.rotation = rotation;
	}
	
	public Texture(int x, int y, Bitmap b){
		this.x = x;
		this.y = y;
		this.png = b;
	}
	public Texture(){}
}
