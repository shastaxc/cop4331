package cop4331.cloud9001.bentd;
//import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class GameLoopThread extends Thread {
       private GameView view;
       
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
    		   Canvas c = null;
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

