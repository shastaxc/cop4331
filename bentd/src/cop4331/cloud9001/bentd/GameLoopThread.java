package cop4331.cloud9001.bentd;
//import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Message;

public class GameLoopThread extends Thread {
       private GameView view;
       private long LastDraw = 0;
       private int TARGET_FPS= 12;// With respect to 60, so if 12 is our target, 12*5=60, 5 is our fps inverse
       private boolean running = false;
       
       public GameLoopThread(GameView view) {
             this.view = view;
       }
       public void setRunning(boolean run) {
             running = run;
       }
       @SuppressLint("WrongCall") 
       @Override
       public void run() {
    	   while (running) {
    		   if(!running){
    			   //
    		   }
    		   else if(GameInstance.basic_map_view.getMode() == MapView.PAUSED || GameInstance.basic_map_view.getMode() == MapView.READY 
    				   || GameInstance.basic_map_view.getMode() == MapView.DEFEAT || GameInstance.basic_map_view.getMode() == MapView.VICTORY){
    			   Canvas c = null;
    			   try {
		        	   c = view.getHolder().lockCanvas();
		               synchronized (view.getHolder()) {
		            	   view.onDrawClear(c);
		               }
		           }
    			   finally {
		           	   if (c != null) {
		           		   view.getHolder().unlockCanvasAndPost(c);
		               }
		           }
    		   }
    		   else{
	       		   //Drawing
	    		   Canvas c = null;
	    		   if(System.currentTimeMillis() - LastDraw > (60/TARGET_FPS) *1000){
	    			   Message msg = new Message();
	        		   //Currency
	    			   String textToChange="";
	    			   if(GameInstance.endless){
		           		   textToChange = GameInstance.currencyToString(view.money)
		           				   +GameInstance.healthToString(view.health)+GameInstance.currentWaveToString(view.currentWave);
	    			   }
	    			   else{
		           		   textToChange = GameInstance.currencyToString(view.money)
		           				   +GameInstance.healthToString(view.health)+GameInstance.currentWaveToString(view.currentWave)+view.maxWaves
		           				   +GameInstance.timeToString((long)(view.level.timePerWave - (System.currentTimeMillis() 
		           						   - view.startOfWaveInMiliseconds)));
	    			   }
	           		   msg.obj = textToChange;
	           		   GameInstance.mHandler.sendMessage(msg);
			           view.updateGame();
			           try {
			        	   c = view.getHolder().lockCanvas();
			               synchronized (view.getHolder()) {
			            	   view.onDraw(c);
			               }
			               }finally {
			            	   if (c != null) {
			            		   view.getHolder().unlockCanvasAndPost(c);
			                   }
			               }
	    		   }
    		   }
    	   }
       }
}  

