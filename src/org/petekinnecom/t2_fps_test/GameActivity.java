package org.petekinnecom.t2_fps_test;


import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {
	GameController gameController;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        gameController = new GameController(this);
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	gameController.signalEnd();
    }
}