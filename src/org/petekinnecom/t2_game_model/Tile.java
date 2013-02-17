package org.petekinnecom.t2_game_model;

import java.util.ArrayList;

import org.petekinnecom.t2_game_controller.C;
import org.petekinnecom.t2_game_controller.Texture;
import org.petekinnecom.t2_game_controller.AffineTransform;
import org.petekinnecom.t2_game_controller.Box;

import android.graphics.Bitmap;
import android.util.Log;


public class Tile extends Texture
{
	private static final String TAG = "PK";
	public int id;
	protected ArrayList<Vektor> vektors = new ArrayList<Vektor>();
	

	protected Box box;	
	
	/*
	 * INITIATOR FUNCTIONS
	 */
	
	/*
	 * This is by DBHelper to initiate a template 
	 * tile.  The vektors are added later.
	 */
	public Tile(int id, int x, int y, Bitmap pngA)
	{
		super(x, y, pngA);
		this.id = id;
		this.box = new Box(x, y, x+png.getWidth(), y+png.getHeight());
	}
	
	/*
	 * This is used by DBHelper to populate our level with
	 * actual tiles. 
	 */
	public Tile(Tile template, int x, int y, int rotation, int reflect)
	{
		super();
		this.id = template.id;
		this.png = template.png;
		this.x = x;
		this.y = y;
		this.box = new Box(x, y, x+png.getWidth(), y+png.getHeight());
		this.rotation = rotation;
		this.reflect = reflect;
		this.vektors = template.copyVektors();
	}
	
	/* 
	 * Deep copy 
	 * Currently unused.
	 */
//	public Tile(Tile t)
//	{
//		this.id = t.id;
//		this.x = t.x;
//		this.y = t.y;
//		this.png = t.png;
//		this.rotation = t.rotation;
//		this.reflect = t.reflect;
//		this.vektors = t.copyVektors();
//	}

	/*
	 * GETTER FUNCTIONS
	 */
	public Bitmap getPNG()
	{
		return png;
	}
	public ArrayList<Vektor> getVektors()
	{
		return vektors;
	}
	public String toString()
	{
		return "Tile id: "+id;
	}
	
	
	public boolean inBox(Box zoomBox)
	{
		if(box.inBox(zoomBox))
			return true;
		return false;
	}

	public void fixVektors()
	{
		ArrayList<Vektor> fixed = new ArrayList<Vektor>();
		for(Vektor v: vektors)
		{
			AffineTransform trans = new AffineTransform();
			trans.setToIdentity();
			trans.translate(x, y);
			trans.translate(png.getWidth() / 2, png.getHeight() / 2);
			trans.scale(reflect, 1.0);
			trans.rotate(Math.toRadians(-rotation));
			trans.translate(-png.getWidth() / 2, -png.getHeight() / 2);
			
			Point start = new Point((int) v.px, (int) v.py);
			
			Point left = new Point();
			trans.transform(start, left);
			start.x = (int) v.cx;
			start.y = (int) v.cy;
			Point right = new Point();
			trans.transform(start, right);
			if(reflect == 1)
				fixed.add(new Vektor(left.x, left.y, right.x, right.y));
			else
				fixed.add(new Vektor(right.x, right.y, left.x, left.y));
		}
		this.vektors = fixed;
		if(C.DEBUG)
			Log.d(TAG, "Tile.fixVektors fixed");
	}
	public void fixBox()
	{
		
		float minx, miny, maxx, maxy;
		minx = x;
		miny = y;
		maxx = png.getWidth() + x;
		maxy = png.getHeight() + y;
		for(Vektor v: vektors)
		{
			if(v.px > maxx)
				maxx = v.px;
			if(v.px<minx)
				minx = v.px;
			if(v.cx>maxx)
				maxx = v.cx;
			if(v.cx<minx)
				minx = v.cx;
			if(v.py>maxy)
				maxy = v.py;
			if(v.py<miny)
				miny = v.py;
			if(v.cy>maxy)
				maxy = v.cy;
			if(v.cy<miny)
				miny = v.cy;
		}
		this.box = new Box((int) minx, (int) miny, (int) maxx, (int) maxy);
	}
	/*
	 * EDITOR FUNCTIONS
	 */
	public void reflect()
	{
		reflect = - reflect;
	}
	public void rotate(int i)
	{
		rotation =(rotation + i*45)%360;
	}
	public void addVektor(Vektor v)
	{
		vektors.add(v);
	}
	public void setVektors(ArrayList<Vektor> v)
	{
		vektors = v;
	}
	public ArrayList<Vektor> copyVektors()
	{
		ArrayList<Vektor> copy = new ArrayList<Vektor>();
		for (Vektor v : vektors)
		{
			copy.add(new Vektor(v.px, v.py, v.cx, v.cy));
		}
		return copy;
	}
}
