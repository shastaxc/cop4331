package cop4331.cloud9001.bentd;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
//import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;

@SuppressLint("WrongCall") 
public class GameView extends SurfaceView {
    private Bitmap bmp;
	private final Bitmap tower1 = BitmapFactory.decodeResource(getResources(),  R.drawable.tower_archer);
	private final Bitmap tower2 = BitmapFactory.decodeResource(getResources(),  R.drawable.tower_ninja);
	private final Bitmap tower3 = BitmapFactory.decodeResource(getResources(),  R.drawable.tower_shrine);
	private final Bitmap tower4 = BitmapFactory.decodeResource(getResources(),  R.drawable.tower_ballista_idle);
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private ArrayList<Enemy> Enemies;
    private ArrayList<Tower> Towers;
    private ImageButton tower1_btn;
    private ImageButton tower2_btn;
    private ImageButton tower3_btn;
    private ImageButton tower4_btn;
    protected static PopupWindow popup_window;
    protected static boolean popup_active = false;
    private int touch_x;
    private int touch_y;
    
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
		if(popup_active == false || !popup_window.isShowing()){ //If popup window is not already showing, create & display it
			touch_x = (int)event.getX();
			touch_y = (int)event.getY();
			popup_active = true;	
			LayoutInflater layoutInflater = (LayoutInflater)GameInstance.app_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.tower_select_popup, (ViewGroup) findViewById(R.layout.activity_game_instance));
		    popup_window = new PopupWindow(popupView, 
		    			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    popup_window.setBackgroundDrawable(new BitmapDrawable(null,""));
		    popup_window.setFocusable(true);
		    popup_window.update();
		    popup_window.showAtLocation(this, Gravity.NO_GRAVITY, touch_x, touch_y);
		    
		    
		    popup_window.setTouchInterceptor(new OnTouchListener() { // or whatever you want
		        @Override
		        public boolean onTouch(View v, MotionEvent event)
		        {
		            if(event.getAction() == MotionEvent.ACTION_OUTSIDE)
		            {
		            	popup_window.dismiss();
		            	popup_active = false;
		            	return true;
		            }
		            return false;
		        }

		    });
	            
		    tower1_btn = (ImageButton) popupView.findViewById(R.id.image_button_1);
		    tower2_btn = (ImageButton) popupView.findViewById(R.id.image_button_2);
		    tower3_btn = (ImageButton) popupView.findViewById(R.id.image_button_3);
		    tower4_btn = (ImageButton) popupView.findViewById(R.id.image_button_4);
		    tower1_btn.setOnClickListener(new OnClickListener() {
		    	@Override
		    	public void onClick(View v) {
		    		Towers.add(new Tower(tower1,touch_x,touch_y));
		    		popup_window.dismiss();
		    		popup_active = false;
			    }
			});
		    tower2_btn.setOnClickListener(new OnClickListener() {
		    	@Override
		    	public void onClick(View v) {
		    		Towers.add(new Tower(tower2,touch_x,touch_y));
		    		popup_window.dismiss();
		    		popup_active = false;
			    }
			});
		    tower3_btn.setOnClickListener(new OnClickListener() {
		    	@Override
		    	public void onClick(View v) {
		    		Towers.add(new Tower(tower3,touch_x,touch_y));
		    		popup_window.dismiss();
		    		popup_active = false;
			    }
			});
		    tower4_btn.setOnClickListener(new OnClickListener() {
		    	@Override
		    	public void onClick(View v) {
		    		Towers.add(new Tower(tower4,touch_x,touch_y));
		    		popup_window.dismiss();
		    		popup_active = false;
			    }
			});
		    	
		             
			/*if (System.currentTimeMillis() - lastClick > 500) {
				lastClick = System.currentTimeMillis();
				Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
				Towers.add(new Tower(bmp,(int)event.getX(),(int)event.getY()));
				
			}*/
			return true;
		}
		else{ //If popup window already showing, do nothing
			return false;
		}
	}
}
