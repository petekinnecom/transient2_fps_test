package org.petekinnecom.t2_game_model;

import java.util.ArrayList;

import android.graphics.Bitmap;


public class Ball
{
	public float x,y,r;
	public float lastX, lastY;
	public float vx, vy;
	public float ax, ay;
	
	public Bitmap png;
	
	public Ball(Point bs, float r, Bitmap ball_png)
	{
		this.x = bs.x;
		this.y = bs.y;
		this.r = r;
		vy = 0;
		vx = 0;
		ay = -Level.G_RATIO;
		ax = 0;
		lastX = x;
		lastY = y;
		this.png = ball_png;
	}

	public void moveBall(float deltaTick, ArrayList<Vektor> vektors)
	{
		lastX = x;
		lastY = y;
		x+=deltaTick * vx;
		y+=deltaTick * vy;

		int i = 0;
		while(collide(vektors) && i<6){i++;}
		vy += deltaTick *ay;
		vx += deltaTick *ax;
		ay = -Level.G_RATIO;
		ax = 0;
		
		x = Math.round(x);
		y = Math.round(y);
	}
	ArrayList<Vektor> hookVektors= new ArrayList<Vektor>();
	Vektor parallel;
	float magVel;
	float alpha;
	ArrayList<Vektor> correctionVks = new ArrayList<Vektor>();
	Vektor leftHook = new Vektor();
	Vektor rightHook = new Vektor();
	Vektor vel = new Vektor();
	Vektor leftSide = new Vektor();
	private boolean collide(ArrayList<Vektor> vektors)
	{
		boolean collided = false;
		//hookVektors.clear();
		correctionVks.clear();
		for(Vektor v: vektors)
		{
			float bpx, bpy;
			bpx =  (x + r*v.nx);
			bpy =  (y + r*v.ny); 
			//leftHook = new Vektor(bpx, bpy, v.px, v.py, false);
			//rightHook = new Vektor(bpx, bpy, v.cx, v.cy, false);
			leftHook.set(bpx, bpy, v.px, v.py);
			rightHook.set(bpx, bpy, v.cx, v.cy);
			/*
			 * If both are negative or positive, then
			 * we are out of range.  So we multiply
			 * them to find out whether they are
			 * opposites.
			 */
			
			if(leftHook.dot(v) * rightHook.dot(v)<=0)	
			{
				
				/*
				 * Determines whether correction
				 * vector is telling us we're below 
				 */
				
				if(leftHook.normalDot(v)<=0)
				{
					
					/*
					 * check to see how far under the ball is
					 */
					if(v.nx *leftHook.normalDot(v)*v.nx *leftHook.normalDot(v) + v.ny * leftHook.normalDot(v)*v.ny * leftHook.normalDot(v)<4*r*r)
					{
						/* fix location */
						x =  (x + v.nx * leftHook.normalDot(v));
						y =  (y + v.ny * leftHook.normalDot(v));
						


						//vel = new Vektor(0,0,vx,vy);
						vel.set(0,0,vx,vy);
						//vx = 1f * (vx - v.nx*vel.normalDot(v)*1.5f);
						//vy = 1f * ( vy - v.ny*vel.normalDot(v)*1.5f);
						
						correctionVks.add(new Vektor(0,0,-v.nx*vel.normalDot(v),-v.ny*vel.normalDot(v)));
						

						collided = true;
					}
				}
			}
		
		else 
			{

				/* the ball is out of range.  
				 * See if it's close to either corner.
				 */
				//leftSide = new Vektor(x, y, v.px, v.py, false);
				leftSide.set(x, y, v.px, v.py);
				if(leftSide.mag()<r)
				{
					/*
					 * It is, correct it by that distance in the 
					 */

					float offSet = r - leftSide.mag();
					leftSide.normalize();
					x =  (x - offSet*(leftSide.cx - leftSide.px));
					y =  (y - offSet*(leftSide.cy - leftSide.py));
					collided = true;

				}
			
			}
			//hookVektors.add(leftHook);
			//hookVektors.add(rightHook);
		}
		float dx,dy;
		dx = dy = 0f;
		for(Vektor v: correctionVks)
		{
			dx+= v.cx/(float) correctionVks.size();
			dy+= v.cy/(float) correctionVks.size();
		}

		vx = (vx + dx);
		vy = (vy + dy);
		
		return collided;

	}

	public void accelerate(float dx, float dy)
	{
		vx += dx;
		vy += dy;
		
		
		/*
		 * SPEED LIMITER
		 */
		if(Math.abs((double)vx)>Level.MAX_VX)
		{
			if(vx<0)
				vx = - Level.MAX_VX;
			else
				vx = Level.MAX_VX;
		}
		if(Math.abs((double) vy)>Level.MAX_VY)
		{
			if(vy<0)
				vy = - Level.MAX_VY;
			else
				vy = Level.MAX_VY;
		}
	}

	public Point getVelocity()
	{
		return new Point((int) vx, (int) vy);
	}
}
