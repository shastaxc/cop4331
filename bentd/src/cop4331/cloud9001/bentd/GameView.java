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
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private ArrayList<Enemy> Enemies;
    private ArrayList<Tower> Towers;
    private ImageButton tower1_btn;
    private ImageButton tower2_btn;
    private ImageButton tower3_btn;
    private ImageButton tower4_btn;
    private MotionEvent touch_event;
    boolean popup_active = false;
    int touch_x;
    int touch_y;
    
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
		if(popup_active == false){
			touch_x = (int)event.getX();
			touch_y = (int)event.getY();
			touch_event = event;
			popup_active = true;
			final Bitmap bmp = BitmapFactory.decodeResource(getResources(),  R.drawable.ic_launcher);
			LayoutInflater layoutInflater = (LayoutInflater)GameInstance.app_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.tower_select_popup, (ViewGroup) findViewById(R.layout.activity_game_instance));
		    	final PopupWindow popupWindow = new PopupWindow(popupView, 
		    			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    popupWindow.setBackgroundDrawable(new BitmapDrawable(null,""));
			    popupWindow.setFocusable(true);
			    popupWindow.update();

	            popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, touch_x, touch_y);
	            
		    	tower1_btn = (ImageButton) popupView.findViewById(R.id.image_button_1);
		        tower2_btn = (ImageButton) popupView.findViewById(R.id.image_button_2);
		        tower3_btn = (ImageButton) popupView.findViewById(R.id.image_button_3);
		        tower4_btn = (ImageButton) popupView.findViewById(R.id.image_button_4);

		        tower1_btn.setOnClickListener(new OnClickListener() {
			        public void onClick(View v) {
						Towers.add(new Tower(bmp,touch_x,touch_y));
						popupWindow.dismiss();
						popup_active = false;
			        }
			    });
		        //tower2_btn.setOnClickListener(popup_click_listener);
		        //tower3_btn.setOnClickListener(popup_click_listener);
		        //tower4_btn.setOnClickListener(popup_click_listener);
		    	
		             
			/*if (System.currentTimeMillis() - lastClick > 500) {
				lastClick = System.currentTimeMillis();
				Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
				Towers.add(new Tower(bmp,(int)event.getX(),(int)event.getY()));
				
			}*/
				return true;
		}
		else{
			return false;
		}
	}
}
