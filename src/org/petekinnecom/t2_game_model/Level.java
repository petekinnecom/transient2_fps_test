package org.petekinnecom.t2_game_model;

import java.util.ArrayList;

import org.petekinnecom.t2_game_controller.Box;
import org.petekinnecom.t2_game_controller.Texture;


public class Level
{
	public static final float G_RATIO = 128f;
	public static final float MAX_VX = 400f;
	public static final float MAX_VY = 400f;
	
	
	public int id;
	protected ArrayList<Tile> tiles;
	public Point ballStart = new Point(32, 32);
	
	
	public int bgDepth, fgDepth;
	public int width, height;
	
	/*
	 * we need a deep copy, so that
	 * original vektors stay intact.
	 */
	public Level(int level_id)
	{
		this.id = level_id;
		tiles = new ArrayList<Tile>();
		
	}
	
	public void addTile(Tile t)
	{
		tiles.add(t);
	}

	public void fixTiles()
	{
		for(Tile t : tiles)
		{
			
			t.fixVektors();
			t.fixBox();
		}
		
	}
	
	ArrayList<Texture> temp;
	public ArrayList<Texture> getTextures(Box zoomBox)
	{
		temp = new ArrayList<Texture>();
		
		for(Tile t: tiles)
		{
			if(t.inBox(zoomBox))
			{
				temp.add((Texture) t);
			}
		}
		
		return temp;
	}
	
	/*
	 * This should be merged with getTextures
	 * It is used to grab the vektors from 
	 * the required tiles.  Perhaps set a vektors
	 * arraylist when we get the textures and simply
	 * return this on getVektors(). ?
	 */
	ArrayList<Tile> tmp;
	public ArrayList<Tile> getTiles(Box zoomBox)
	{
		tmp = new ArrayList<Tile>();
		
		for(Tile t: tiles)
		{
			if(t.inBox(zoomBox))
			{
				tmp.add(t);
			}
		}
		
		return tmp;
	}
}
