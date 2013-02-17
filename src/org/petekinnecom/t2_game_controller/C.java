package org.petekinnecom.t2_game_controller;

import java.util.ArrayList;

import org.petekinnecom.t2_game_model.Tile;


public class C
{
	
	
	public static int MOVE_SPEED = 16;
	
	//This is what it resets to
	public static final int GRID_DEFAULT = 8;

	public static final float TICK_MULTIPLIER = 4f;
	public static float MAX_DELTA_TICK = 1f;
	
	public static final int W_SOLID = 0;
	public static final int W_A = 1;
	public static final int W_B = 2;
	public static final int W_LIMBO = 3;
	
	public static final int KT_NONE = 0;
	public static final int KT_EXPLODE = 1;

	public static final boolean DEBUG = true;
	
	public static ArrayList<Tile> tile_templates;
	public static Tile nullTile;
	public static Tile emptyTile;
	
	
	public static void loadTiles()
	{
//		try
//		{
//			DBHelper.readTiles();
//		} catch (SQLException e)
//		{
//			
//			e.printStackTrace();
//		}
	}
	
	
	public static Tile getTileById(int tile_id)
	{
		for(Tile t : tile_templates)
		{
			if(t.id == tile_id)
				return t;
		}
		return nullTile;
	}
}
