package org.petekinnecom.t2_fps_test;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.petekinnecom.t2_game_controller.*;
import org.petekinnecom.t2_game_model.Vektor;

public class GameRender extends SurfaceView implements SurfaceHolder.Callback
{

	private static final String TAG = "PK";

	class RenderThread extends Thread
	{
		private volatile Thread signal;

		RenderThread()
		{
			signal = this;
		}

		public void signalEnd()
		{
			signal = null;
			Log.d(TAG, "GameRender.RenderThread.signalEnd");
		}

		@Override
		public void run()
		{
			Log.d(TAG, "GameRender.RenderThread: thread started.");
			Thread thisThread = Thread.currentThread();
			while (thisThread == signal)
			{
				update();
//				 try
//				 {
//				 Thread.sleep(16);
//				 } catch (InterruptedException e)
//				 {
//				 e.printStackTrace();
//				 }
			}
		}
	}

	RenderThread thread;
	Bitmap buffer, toDraw;
	Canvas canvas;
	private SurfaceHolder surfaceHolder;
	private ArrayList<Texture> textures;
	private Box zoomBox;

	public void start()
	{
		thread.start();
	}

	public void signalEnd()
	{
		thread.signalEnd();
	}

	ArrayList<Vektor> veks;

	public synchronized void setTextures(ArrayList<Texture> items, Box zoomBox,
			ArrayList<Vektor> vektors)
	{
		this.textures = items;
		this.zoomBox = zoomBox;
		this.veks = vektors;
	}

	public void update()
	{
		buffer();
		redraw();
	}

	protected synchronized void redraw()
	{
		if (toDraw != null)
		{
			canvas = surfaceHolder.lockCanvas(null);
			// if(canvas!=null)
			canvas.drawBitmap(toDraw, 0, 0, paint);
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	Canvas bCanvas, tdCanvas;
	Paint paint = new Paint();
	Matrix transform = new Matrix();
	long lastTime = 0l;
	long thisTime;
	float deltaTime;
	int frames = 0;
	int x1, y1, x2, y2;
	private void buffer() 
	{
		/*
		 * this is where we build what is to be drawn.
		 */
		// buffer = Bitmap.createBitmap(this.getWidth(), this.getHeight(),
		// Config.ARGB_8888);
		// bCanvas = new Canvas(buffer);
		paint.setColor(Color.WHITE);
		bCanvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
		for (Texture t : textures)
		{
			transform.reset();
			transform.postRotate(t.rotation, t.png.getWidth() / 2,
					t.png.getHeight() / 2);
			transform.postScale(t.reflect, 1);
			if (t.reflect == -1)
				transform.postTranslate(t.png.getWidth(), 0);
			transform.postTranslate(t.x - zoomBox.x1,
					this.getHeight() - t.png.getHeight() - t.y + zoomBox.y1);

			// if(t.png!=null)
			// bCanvas.drawBitmap(t.png, t.x-zoomBox.x1,
			// this.getHeight()-t.png.getHeight() - t.y+zoomBox.y1, paint);
			bCanvas.drawBitmap(t.png, transform, paint);
		}
		paint.setColor(Color.RED);
		for (Vektor v : veks)
		{
			x1 = (int) v.px - zoomBox.x1;
			y1 = (int) v.py - zoomBox.y1;
			x2 = (int) v.cx - zoomBox.x1;
			y2 = (int) v.cy - zoomBox.y1;
			// bCanvas.drawLine(x1, y1, x2, y2, paint);
		}
		// bCanvas.scale(1,-1, 0, this.getHeight()/2);
		// transform.reset();
		// transform.setScale(1,-1, 0, this.getHeight()/2);
		// toDraw = buffer.copy(Config.ARGB_8888, false);
		toDraw = buffer;
		// toDraw = Bitmap.createBitmap(buffer, 0, 0, buffer.getWidth(),
		// buffer.getHeight(), transform, true);
		frames = (frames + 1) % 100;
		if (frames == 0)
		{
			thisTime = System.currentTimeMillis();
			deltaTime = (thisTime - lastTime) / 1000f;
			if (C.DEBUG)
				Log.d(TAG, "fps: " + 100f / deltaTime);
			lastTime = thisTime;
		}
	}

	/*
	 * Because the thread needs to access the z zoomBox, we may as well hold a
	 * reference to it.
	 */
	public GameRender(Context context, Box zoomBox)
	{
		super(context);
		this.zoomBox = zoomBox;
		thread = new RenderThread();
		Log.d(TAG, "new render");
		toDraw = buffer = null;
		paint = new Paint();

		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);


	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		surfaceHolder = holder;
		surfaceHolder.addCallback(this);

		// thread.start();
		buffer = Bitmap.createBitmap(this.getWidth(), this.getHeight(),
				Config.ARGB_8888);

		bCanvas = new Canvas(buffer);
		toDraw = Bitmap.createBitmap(this.getWidth(), this.getHeight(),
				Config.ARGB_8888);
		tdCanvas = new Canvas(toDraw);
		// bCanvas = new Canvas(buffer);
		thread.start();

		Log.d(TAG, "render.surfaceCreated");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{

	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return super.onTouchEvent(event);
	}
}
