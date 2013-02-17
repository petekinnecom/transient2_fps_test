package org.petekinnecom.t2_game_controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import org.petekinnecom.t2_game_model.Level;
import org.petekinnecom.t2_game_model.Point;
import org.petekinnecom.t2_game_model.Tile;
import org.petekinnecom.t2_game_model.Vektor;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "levels.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TAG = "PK";

	private Context context;

	public DBHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		readDBFromAssets();
		SQLiteDatabase db = this.getReadableDatabase();
		if(C.DEBUG)
			Log.d(TAG, "Database location at: "+db.getPath());
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}

	public Level readLevel(int level_id) throws SQLException
	{

		if (C.DEBUG)
			Log.d(TAG, "Reading level from db.");

		SQLiteDatabase db = this.getReadableDatabase();
		if(C.DEBUG)
			Log.d(TAG, "Database location at: "+db.getPath());

		/*
		 * GENERIC INIT STUFF END
		 */
		Level level = new Level(level_id);
		if (C.DEBUG)
			Log.d(TAG, "SELECT * FROM level_data WHERE level_id='" + level_id
					+ ";'");
		Cursor c = db.rawQuery("SELECT * FROM level_data WHERE level_id=?",
				new String[] { "" + level_id });
		while (c.moveToNext())
		{
			int x = c.getInt(c.getColumnIndex("x"));
			int y = c.getInt(c.getColumnIndex("y"));
			int tile_id = c.getInt(c.getColumnIndex("tile_id"));
			int rotation = c.getInt(c.getColumnIndex("rotation"));
			int reflect = c.getInt(c.getColumnIndex("reflect"));

			level.addTile(new Tile(C.getTileById(tile_id), x, y, rotation,
					reflect));

		}
		if (C.DEBUG)
			Log.d(TAG, "Finished reading level layout.");
		if (C.DEBUG)
			Log.d(TAG, "Reading background/foreground data.");

		c.close();
		
		c = db.rawQuery("SELECT * FROM level_info WHERE level_id=?",
				new String[] { ""+level_id });
		c.moveToFirst();
		level.width = c.getInt(c.getColumnIndex("level_width"));
		level.height = c.getInt(c.getColumnIndex("level_height"));
		// level.backgroundPNG = getFile(rs.getString("backgroundPNG"));
		// level.foregroundPNG = getFile(rs.getString("foregroundPNG"));
		level.bgDepth = c.getInt(c.getColumnIndex("bgDepth"));
		level.fgDepth = c.getInt(c.getColumnIndex("fgDepth"));
		level.ballStart = new Point(c.getInt(c.getColumnIndex("ballStartX")),
				c.getInt(c.getColumnIndex("ballStartY")));

		db.close();

		if (C.DEBUG)
			Log.d(TAG, "Finished reading level");
		return level;

	}

	/*
	 * Used for loading the tile data into the static globals class.
	 */
	public void readTiles() throws SQLException
	{
		if (C.DEBUG)
			Log.d(TAG, "Reading tiles from db.");
		/*
		 * GENERIC INIT STUFF
		 */
		SQLiteDatabase db = this.getReadableDatabase();
		/*
		 * GENERIC INIT STUFF END
		 */

		ArrayList<Tile> tiles = new ArrayList<Tile>();
		String png_a, png_b;
		int id;
		Bitmap pngA, pngB;
		boolean solid, foreground;
		Cursor c = db.rawQuery("SELECT * FROM tiles;", null);
		if (C.DEBUG)
			Log.d(TAG, "entering loop");
		while (c.moveToNext())
		{
			if (C.DEBUG)
				Log.d(TAG,
						"Parsing tile record: "
								+ c.getInt(c.getColumnIndex("_id")));

			id = c.getInt(c.getColumnIndex("_id"));

			png_a = c.getString(c.getColumnIndex("png_a"));
			png_b = c.getString(c.getColumnIndex("png_b"));

			pngA = getFile(png_a);
			// pngB = getFile(png_b);
			// tiles.add(new Tile(id, pngA, pngB, x1, y1, x2, y2, solid,
			// foreground));
			if(pngA==null)
			{
				Log.d(TAG, "DBHelper.readTiles: null pngA");
				System.exit(1);
			}

			Tile tile = new Tile(id, 12, 12, pngA);

			tiles.add(tile);
		}

		C.tile_templates = tiles;

		Cursor vs = db.rawQuery("SELECT * FROM vektors ;", null);
		int x1, y1, x2, y2;
		Tile tile;

		int world;
		int dummy;
		int killType;
		float friction;

		while (vs.moveToNext())
		{
			tile = C.getTileById(vs.getInt(vs.getColumnIndex("tile_id")));
			x1 = vs.getInt(vs.getColumnIndex("x1"));
			y1 = vs.getInt(vs.getColumnIndex("y1"));
			x2 = vs.getInt(vs.getColumnIndex("x2"));
			y2 = vs.getInt(vs.getColumnIndex("y2"));
			world = vs.getInt(vs.getColumnIndex("world"));
			dummy = vs.getInt(vs.getColumnIndex("dummy"));
			killType = vs.getInt(vs.getColumnIndex("killType"));
			friction = vs.getFloat(vs.getColumnIndex("friction"));
			tile.addVektor(new Vektor(x1, y1, x2, y2, world, dummy, killType,
					friction));
		}
		vs.close();

		db.close();

		if (C.DEBUG)
			Log.d(TAG, "Finished reading tiles");

		return;
	}

	public void readDBFromAssets()
	{
		if (C.DEBUG)
			Log.d(TAG, "Begin readDBFromAssets.");

		String dbName = "levels.db";

		File outputFile = new File(
				"/data/data/org.petekinnecom.t2_fps_test/databases",
				"levels.db");
		try
		{
			// getResources().openRawResource(R.drawable.balloons);
			InputStream is = context.getAssets().open(dbName);
			OutputStream os = new FileOutputStream(outputFile);
			byte[] data = new byte[is.available()];
			is.read(data);
			os.write(data);
			is.close();
			os.close();
			if (C.DEBUG)
				Log.d(TAG, "Success it seems.");
		} catch (IOException e)
		{
			// Unable to create file, likely because external storage is
			// not currently mounted.
			if (C.DEBUG)
				Log.d("ExternalStorage", "Error writing " + outputFile, e);
		}

	}

	public Bitmap getFile(String s)
	{
		AssetManager assetManager = context.getAssets();
		InputStream inputStream;
		Bitmap png = null;
		s = "pete_textures/"+s;
		try
		{
			inputStream = assetManager.open(s);
			png = Bitmap.createBitmap(
					BitmapFactory.decodeStream(inputStream));


		} catch (IOException e)
		{
			Log.d(TAG, "DBHelper.getFile: error.");
			e.printStackTrace();
			System.exit(1);
		}
		if(png==null)
		{
			Log.d(TAG, "DBHelper.getFile: null png.");
			System.exit(1);
		}
		return png;
	}
}
