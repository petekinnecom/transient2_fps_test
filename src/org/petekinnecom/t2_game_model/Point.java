package org.petekinnecom.t2_game_model;

public class Point
{
	public float x, y;

	public Point()
	{
	}

	public Point(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}
	
	public void setLocation(float i, float j)
	{
		x = i;
		y = j;
	}
	
	public void setLocation(double i, double j)
	{
		x = (float) i;
		y = (float) j;
	}
	
	public String toString()
	{
		return "( "+x+" , "+y+" )";
	}
}
