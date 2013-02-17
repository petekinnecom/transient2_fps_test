package org.petekinnecom.t2_game_model;

import java.util.ArrayList;

import org.petekinnecom.t2_game_controller.Box;
import org.petekinnecom.t2_game_controller.Texture;

import android.graphics.Bitmap;
import android.util.Log;

public class GameModel
{
	private static final String TAG = "PK";
	private Level level;
	private Ball ball;
	
	public GameModel(Level level, Bitmap ball_png)
	{
		this.level = level;
		ball = new Ball(level.ballStart, 32, ball_png);
	}
	ArrayList<Vektor> vektors = new ArrayList<Vektor>();
	int getTileS, getVekS, i , j;
	ArrayList<Tile> tiles, veks;
	public ArrayList<Vektor> getVektors(Box zoomBox)
	{
		vektors.clear();
		for(Tile t: level.getTiles(zoomBox))
		{
			for(Vektor v : t.getVektors())
			{
				vektors.add(v);
			}
			
		}

		return vektors;
	}
	
	public Ball getBall()
	{
		return ball;
	}

	
	public void accelBall(float dx, float dy)
	{
		ball.accelerate(dx, dy);
	}
	
	public void tick(float deltaTick, Box zoomBox)
	{
		ball.moveBall(deltaTick, getVektors(zoomBox));

	}
	
	public Point getBallVelocity()
	{
		return ball.getVelocity();
	}

	
	ArrayList<Texture> texs;
	public ArrayList<Texture> getTextures(Box zoomBox)
	{
		texs =  level.getTextures(zoomBox);
		texs.add(new Texture((int) (ball.x-ball.r), (int) (ball.y-ball.r), ball.png));
		return texs;
	}
	float x1, y1;
	Box temp;
	public Box getZoomBox(float width, float height)
	{
		x1 = (int) ball.x - (int) (width/3f);
		y1 = (int) ball.y - (int) (height/3f);
		

		temp = new Box((int)x1, (int) y1, (int)(x1 + width), (int) (y1 + height));
		//C.out("input size: "+width+", "+height);
		//C.out("output size: "+ temp.getWidth()+", "+temp.getHeight());
		
		return temp;
	}
}
