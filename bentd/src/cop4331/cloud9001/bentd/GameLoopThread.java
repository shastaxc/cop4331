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
<<<<<<< HEAD
=======
       		   //Drawinng
    		   Canvas c = null;
>>>>>>> origin/Dakota-v1.25
    		   if(GameInstance.basic_map_view.getMode() == MapView.PAUSED || GameInstance.basic_map_view.getMode() == MapView.READY){
    			   //
    		   }
    		   else{
<<<<<<< HEAD
	       		   //Drawinng
	    		   Canvas c = null;
	    		   if(System.currentTimeMillis() - LastDraw > (60/TARGET_FPS) *1000){
	    			   Message msg = new Message();
	        		   //Currency
	           		   String textToChange = GameInstance.currencyToString(view.money)
	           				   +GameInstance.healthToString(view.health)+(1+view.currentWave)+view.maxWaves
	           				   +GameInstance.timeToString((long)(view.level.timePerWave - (System.currentTimeMillis() 
	           						   - view.startOfWaveInMiliseconds)));
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
=======
        		   if(System.currentTimeMillis() - LastDraw > (60/TARGET_FPS_INVERSE) *1000){
        			   Message msg = new Message();
            		   //Currency
        			   view.updateGame();
               		   String textToChange = GameInstance.currencyToString(view.money)
               				   +GameInstance.healthToString(view.health)+(1+view.currentWave)+view.maxWaves
               				   +GameInstance.timeToString((long)(view.level.timePerWave - (System.currentTimeMillis() 
               						   - view.startOfWaveInMiliseconds)));
               		   msg.obj = textToChange;
               		   GameInstance.mHandler.sendMessage(msg);
               		   //Health
               		   //textToChange = "2"+view.health;
               		   //msg.obj = textToChange;
               		   //GameInstance.mHandler.sendMessage(msg);
               		   /*//Wave
               		   textToChange = "3"+view.currentWave+view.maxWaves;
               		   msg.obj = textToChange;
               		   GameInstance.mHandler.sendMessage(msg);
               		   //Time
               		   textToChange = "4"+GameInstance.timeToString((long)(System.currentTimeMillis() - view.startOfWaveInMiliseconds));
               		   msg.obj = textToChange;
               		   GameInstance.mHandler.sendMessage(msg);*/
    		           try {
    		        	   c = view.getHolder().lockCanvas();
    		               synchronized (view.getHolder()) {
    		            	   view.onDraw(c);
    		               }
    		           }
    		           finally {
    		            	   if (c != null) {
    		            		   view.getHolder().unlockCanvasAndPost(c);
    		                   }
    		           }
        		   }
>>>>>>> origin/Dakota-v1.25
    		   }
    	   }
       }
}  

