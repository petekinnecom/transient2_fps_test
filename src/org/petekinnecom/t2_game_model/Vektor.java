package org.petekinnecom.t2_game_model;

import org.petekinnecom.t2_game_controller.C;


public class Vektor
{
	
	public float px, py, cx, cy;
	public float nx, ny;
	public int world;
	public boolean dummy;
	public int killType;
	public float friction;
		
	double mag;
	
	public Vektor()
	{
		
	}
	
	public Vektor(float px, float py, float cx, float cy)
	{
		this.px = px;
		this.py = py;
		this.cx = cx;
		this.cy = cy;
		
		float dx = cx - px;
		float dy = cy - py;
		
		mag = Math.sqrt(dx*dx + dy*dy);
		
		nx = (float) (dy / mag); 
		ny = (float) (-dx / mag);
		
		
		
		/* defaults */
		this.world = C.W_SOLID;
		this.dummy = false;
		this.killType = C.KT_NONE;
		this.friction = 1f;
	}
	public Vektor(float px, float py, float cx, float cy, int world, int dummy, int killType, float friction)
	{
		this.px = px;
		this.py = py;
		this.cx = cx;
		this.cy = cy;
		this.world = world;
		if(dummy == 0)
			this.dummy = false;
		else
			this.dummy = true;
		this.killType = killType;
		this.friction = friction;
		
		
		float dx = cx - px;
		float dy = cy - py;
		
		double mag = Math.sqrt(dx*dx + dy*dy);
		
		nx = (float) (dy / mag); 
		ny = (float) (-dx / mag);

	}
	public Vektor(float px, float py, float cx, float cy, boolean b)
	{
		this.px = px;
		this.py = py;
		this.cx = cx;
		this.cy = cy;
		this.nx = 0;
		this.ny = 0;
	}
	
	public float dot(Vektor b)
	{
		return (cx - px)*(b.cx - b.px) + (cy - py)*(b.cy - b.py); 
	}
	public float normalDot(Vektor b)
	{
		return (cx - px)*(b.nx) + (cy - py)*(b.ny); 
	}
	public float mag()
	{
		return (float) Math.sqrt((cx - px)*(cx - px) + (cy-py)*(cy-py));
	}
	public void normalize()
	{
		/*
		 * can't make function call while scaling,
		 * because scaling x will affect the scaling of
		 * the y.  So, save the magnitude.
		 */
		float mag = mag();
		cx = (cx - px)/mag + px;
		cy = (cy - py)/mag + py;
	}
	public String toString()
	{
		return "("+px+" , "+py+") to ("+cx+" , "+cy+")  length: "+mag();
	}

	public void set(float px, float py, float cx, float cy)
	{
		this.px = px;
		this.py = py;
		this.cx = cx;
		this.cy = cy;
		
		float dx = cx - px;
		float dy = cy - py;
		
		mag = Math.sqrt(dx*dx + dy*dy);
		
		nx = (float) (dy / mag); 
		ny = (float) (-dx / mag);
		
		
	}
}
