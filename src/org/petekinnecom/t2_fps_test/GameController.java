package org.petekinnecom.t2_fps_test;

import java.sql.SQLException;

import org.petekinnecom.t2_game_controller.AffineTransform;
import org.petekinnecom.t2_game_controller.Box;
import org.petekinnecom.t2_game_controller.C;
import org.petekinnecom.t2_game_controller.DBHelper;
import org.petekinnecom.t2_game_model.GameModel;
import org.petekinnecom.t2_game_model.Level;
import org.petekinnecom.t2_game_model.Point;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameController implements OnTouchListener
{
	private static final String TAG = "PK";

	class ControlThread extends Thread
	{
		private volatile Thread signal;

		ControlThread()
		{
			signal = this;
		}

		public void signalEnd()
		{
			signal = null;
			Log.d(TAG, "GameController.ControlThread.signalEnd");
		}

		@Override
		public void run()
		{
			Log.d(TAG, "GameController.ControlThread started");
			lastTime = System.currentTimeMillis();
			Thread thisThread = Thread.currentThread();
			while (thisThread == signal)
			{
				update();
				 try
				 {
				 Thread.sleep(30);
				 } catch (InterruptedException e)
				 {
				 e.printStackTrace();
				 }
			}
		}
	}

	GameRender gameRender;
	ControlThread controlThread;
	GameModel gameModel;
	Box zoomBox;
	Level level;
	int count = 0;
	private static final float SPEED_MULTIPLIER = 4;
	long lastTime, thisTime;
	float deltaTime;

	private void update()
	{
		zoomBox = gameModel.getZoomBox(gameRender.getWidth(),
				gameRender.getHeight());

		gameRender.setTextures(gameModel.getTextures(zoomBox), zoomBox,
				gameModel.getVektors(zoomBox));

		thisTime = System.currentTimeMillis();
		deltaTime = Math.min(C.MAX_DELTA_TICK, (thisTime - lastTime) / 1000f);
		gameModel.tick(deltaTime*C.TICK_MULTIPLIER, zoomBox);
		lastTime = thisTime;
		// gameModel.accelBall(10f, 0);
	}

	GameController(Activity gameActivity)
	{

		zoomBox = new Box(0, 0, 800, 480);
		gameRender = new GameRender((Context) gameActivity, zoomBox);
		gameRender.setOnTouchListener(this);
		gameActivity.setContentView(gameRender);
		// render.start();
		controlThread = new ControlThread();
		// controlThread.start();

		// render.stop();
		// controlThread.signalEnd();
		DBHelper dbHelper = new DBHelper((Context) gameActivity);
		try
		{
			dbHelper.readTiles();
			level = dbHelper.readLevel(0);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		Log.d(TAG, "gameController end init.");
		if (level == null)
			System.exit(1);

		/*
		 * TODO: fix ugly hack to get the ball texture working.
		 */
		Bitmap ball_png = dbHelper.getFile("ball.png");

		level.fixTiles();
		gameModel = new GameModel(level, ball_png);

		// gameRender.start();
		controlThread.start();
	}

	public void signalEnd()
	{
		controlThread.signalEnd();
		gameRender.signalEnd();
	}

	public boolean onTouchEvent(MotionEvent event)
	{
		return true;
	}

	/*
	 * These events come from a different thread than the game update thread. To
	 * correctly handle these events, we should set a flag that is processed
	 * during the gameModel.tick() process.
	 */
	@Override
	public boolean onTouch(View v, MotionEvent e)
	{
		if (e.getX() > v.getWidth() / 2 && e.getY() > v.getHeight() / 2)
		{
			gameModel.accelBall(40f, 0);
		} else if (e.getX() < v.getWidth() / 2 && e.getY() > v.getHeight() / 2)
		{
			gameModel.accelBall(-40f, 0);
		} else
		{
			Log.d(TAG, "jumped");
			gameModel.accelBall(0, 200f);
		}

		return false;
	}
}
