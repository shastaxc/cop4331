package cop4331.cloud9001.bentd;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
//import android.view.View;

@SuppressLint("WrongCall") 
public class GameView extends SurfaceView {
    private Bitmap bmp;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private ArrayList<Enemy> Enemies;
    private ArrayList<Tower> Towers;
    
    private long lastClick;
    public GameView(Context context) {
    	super(context);
        initializeGameView(this);
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeGameView(this);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeGameView(this);
    }
    public void initializeGameView(GameView gv){
    	gameLoopThread = new GameLoopThread(this);
    	gv.setZOrderOnTop(true);
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSPARENT);
        holder.addCallback(new SurfaceHolder.Callback() {
        	@Override
        	public void surfaceDestroyed(SurfaceHolder holder) {
        		boolean retry = true;
        		gameLoopThread.setRunning(false);
        		while (retry) {
        			try {
        				gameLoopThread.join();
        				retry = false;
                		} catch (InterruptedException e) {}
                	}
        		}
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                	gameLoopThread.setRunning(true);
                    gameLoopThread.start();
                }
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format,
                             int width, int height) {
                }
        	});
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bad1);
        
        Towers = new ArrayList<Tower>();
        
        Enemies = new ArrayList<Enemy>();
        Enemies.add(new Enemy(this,bmp));
        Enemies.add(new Enemy(this,bmp));
        
    }
	@Override
    protected void onDraw(Canvas canvas) {
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
    	for(int i=0;i<Enemies.size();i++)
    		Enemies.get(i).onDraw(canvas);
    	for(int i=0;i<Towers.size();i++)
    		Towers.get(i).onDraw(canvas);
    }
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if (System.currentTimeMillis() - lastClick > 500) {
			lastClick = System.currentTimeMillis();
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
			Towers.add(new Tower(bmp,(int)event.getX(),(int)event.getY()));
			
		}
		return true;
	}
}
