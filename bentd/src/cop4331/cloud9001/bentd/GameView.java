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
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
//import android.R;
//import android.view.View;

@SuppressLint({ "WrongCall", "ClickableViewAccessibility", "DrawAllocation" }) 
public class GameView extends SurfaceView {
    private Bitmap bmp;
    /*
     * Towers
     */
    private final Bitmap tower1 = BitmapFactory.decodeResource(getResources(),  R.drawable.tower_archer);//50g
	private final Bitmap tower2 = BitmapFactory.decodeResource(getResources(),  R.drawable.tower_ninja);//75g
	private final Bitmap tower3 = BitmapFactory.decodeResource(getResources(),  R.drawable.tower_shrine);//100g
	private final Bitmap tower4 = BitmapFactory.decodeResource(getResources(),  R.drawable.tower_ballista_idle);//200g
	/*
	 * Enemies
	 */
	private final Bitmap enemyOni = BitmapFactory.decodeResource(getResources(),  R.drawable.enemy_oni);
	private final Bitmap enemyImp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_imp);
	private final Bitmap enemyKitsune = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_kitsune);
	private final Bitmap bossOroshi = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_oroshi_a);
	/*
	 * View Variables
	 */
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread; //Periodic call for GameView.onDraw();
    protected ArrayList<Enemy> Enemies; //Enemies on screen
    protected ArrayList<Tower> Towers; //Towers on screen
    private ImageButton tower1_btn;
    private ImageButton tower2_btn;
    private ImageButton tower3_btn;
    private ImageButton tower4_btn;
    protected MapConfig level;
    protected static PopupWindow popup_window;
    protected static boolean popup_active = false;
    private int touch_x;
    private int touch_y;
    private Path p;
    private long lastClick = 0; //Time between tower clicks
    protected int[][] fieldOfBattle; //mapconfig file
    /*
     * Stats for stats bar
     */
    protected int money = 0;
    protected int score = 0;
    protected int health = 0;
    protected int currentWave = 0;
    protected int maxWaves = 0;
    /*
     * Waves
     */
    protected boolean spawnedFirstWave = false;
    protected boolean begin = true;
    protected long startOfWaveInMiliseconds = 0;
    /*
     * CONSTRUCTORS
     */
    public GameView(Context context) {
    	super(context);
        initializeGameView(context);
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeGameView(context);
    }
    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeGameView(context);
    }
    public void initializeGameView(Context context){
    	gameLoopThread = new GameLoopThread(this);
    	setZOrderOnTop(true);
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
        
        /****************************************
        text_layout = (LinearLayout) findViewById(R.id.text_layout);
		//text_layout.setOnClickListener(global_on_click_listener);
        stats_bar_layout = (RelativeLayout) findViewById(R.id.stats_bar_layout);
        //stats_bar_layout.setOnClickListener(GameInstance.global_on_click_listener);
        currency_textview = (TextView) findViewById(R.id.currency_textview);
        currency_textview.setText("9999");
        life_textview = (TextView) findViewById(R.id.life_textview);
        life_textview.setText("999");
        wave_textview = (TextView) findViewById(R.id.wave_textview);
        wave_textview.setText("9/9");
        time_remaining_textview = (TextView) findViewById(R.id.time_remaining_textview);
        time_remaining_textview.setText("99:99");
		pause_btn = (Button)findViewById(R.id.pause_btn);
		//pause_btn.setOnClickListener(global_on_click_listener);
		forward_btn = (Button)findViewById(R.id.fast_forward_btn);
		//forward_btn.setOnClickListener(global_on_click_listener);
        /*****************************************/
        initializeLevel(0);
    }
    public void initializeLevel(int levelSelected){
    	Towers = new ArrayList<Tower>();
        Enemies = new ArrayList<Enemy>();
        setMapGrid(0);
        level = new MapConfig(levelSelected);
        money = level.startingMoney;
        maxWaves = level.maxWaves;
        currentWave = 0;
        health = level.health;
        startOfWaveInMiliseconds = System.currentTimeMillis();
    }
	/*
	 * MAIN GAME CODE
	 * 
	 * The gameloop only acts as a timer for the ondraw method, to call it periodically.
	 * The onDraw performs these game tasks:
	 *     1) Tower Targeting
	 *     2) Tower Firing
	 *     3) Draw Towers and their Bullets
	 *     4) Delete Dead Enemies
	 *     5) Update Enemy positions
	 *     6) COMING SOON: Wave update and status bar timer.
	 */
    protected void updateGame(){
    	//Update Wave
    	long timeRemaining = System.currentTimeMillis() - startOfWaveInMiliseconds;
    	if(!spawnedFirstWave && begin){
    		spawnedFirstWave = true;
    		for(int i=0;i<level.EnemiesPerWave[currentWave];i++)
				Enemies.add(new Enemy(this,fieldOfBattle,0));
    		startOfWaveInMiliseconds = System.currentTimeMillis();
    	}
    	if(begin && (timeRemaining > level.timePerWave || Enemies.size() == 0)){//Wave is done, Spawn Next one
    		currentWave++;
    		if(currentWave < maxWaves-1)
    			for(int i=0;i<level.EnemiesPerWave[currentWave];i++)
    				Enemies.add(new Enemy(this,fieldOfBattle,0));
    		startOfWaveInMiliseconds = System.currentTimeMillis();
    	}
    	if(currentWave == maxWaves){
    		gameLoopThread.setRunning(false);
    	}
    	/*
    	 * ENTITY UPDATES
    	 */
		//TOWER TARGETING
		for(Tower t: Towers){
		   //Needs a target
		   if(t.target == null){
			   //Finds at the closest enemy
			   Enemy e = Enemy.nearestEnemy(Enemies,t.getx(),t.gety(),t.range);
			   if(e == null)
				   break;
			   else
				   //Fires at enemy
				   if(System.currentTimeMillis() - t.lastFired > t.fireSpeed ){
					   t.target = e;
					   t.fire(this);
				   }
		   }
		   //Has a target
		   else{
			   //Fires at enemy
			   if(System.currentTimeMillis() - t.lastFired > t.fireSpeed && t.Bullets.size() < t.MAX_BULLETS){
				   t.fire(this);
			   }
		   }
		}
		//TOWER AND BULLET UPDATES / DRAW
    	for(Tower t: Towers)
    		for(int i=0;i<t.Bullets.size();i++)
    			t.Bullets.get(i).update();
    	//ENEMY UPDATES
    	for(int i=0;i<Enemies.size();i++){
    		if(Enemies.get(i).health < 0){
    			for(Tower t: Towers){
    				if(t.target == Enemies.get(i)){
    					t.Bullets.clear();
    					t.target = null;
    				}
    			}
    			money+=Enemies.get(i).bounty;
    			score+=Enemies.get(i).bounty;
    			Enemies.remove(i--);	
    		}
    	}
    	//ENEMY DRAW
    	for(int i=0;i<Enemies.size();i++){
    		Enemy e = Enemies.get(i);
    		if(wash(e.y,(GameInstance.game_view.getHeight()/MapView.Y_TILE_COUNT)) <=0 
    				&& wash(e.x,(GameInstance.game_view.getWidth()/MapView.X_TILE_COUNT))>=8){
    			health-=e.strength;
    			Enemies.remove(i--);
    		}
    		else
    			e.update();
    		
    	}
    }
    @Override
    protected void onDraw(Canvas canvas) {
    	/*
    	 * DRAWING AND ENTITY UPDATES
    	 */
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		//TOWER AND BULLET DRAW
    	for(Tower t: Towers){
    		t.onDraw(canvas);
    		for(int i=0;i<t.Bullets.size();i++)
    			t.Bullets.get(i).onDraw(canvas);
    	}
    	//ENEMY DRAW
    	for(int i=0;i<Enemies.size();i++)
    			Enemies.get(i).onDraw(canvas);
    }
	
	@Override
	/***
	 * When the screen is tapped, the view checks if a tower can be placed.
	 */
	public boolean onTouchEvent(MotionEvent event){
		if(popup_active == false || !popup_window.isShowing()){ //If popup window is not already showing, create & display it
			touch_x = (int)event.getX();
			touch_y = (int)event.getY();
			touch_x = wash(touch_x,(GameInstance.game_view.getWidth()/MapView.X_TILE_COUNT));
			touch_y = wash(touch_y,(GameInstance.game_view.getHeight()/MapView.Y_TILE_COUNT));
			if(!validPos(touch_x,touch_y))
				return false;
			for(Tower t: Towers)
				if(t.getx() == touch_x && t.gety() == touch_y)
					return false;
			popup_active = true;	
			LayoutInflater layoutInflater = (LayoutInflater)GameInstance.app_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.tower_select_popup, (ViewGroup) findViewById(R.layout.activity_game_instance));
		    popup_window = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
		    		if(money>=50){
		    			money-=50;
		    			Towers.add(new Tower(tower1,touch_x,touch_y,1));
		    		}
		    		popup_window.dismiss();
		    		popup_active = false;
			    }
			});
		    tower2_btn.setOnClickListener(new OnClickListener() {
		    	@Override
		    	public void onClick(View v) {
		    		if(money>=75){
		    			money-=75;
		    			Towers.add(new Tower(tower2,touch_x,touch_y));
		    		}
		    		popup_window.dismiss();
		    		popup_active = false;
			    }
			});
		    tower3_btn.setOnClickListener(new OnClickListener() {
		    	@Override
		    	public void onClick(View v) {
		    		if(money>=100){
		    			money-=100;
		    			Towers.add(new Tower(tower3,touch_x,touch_y));
		    		}
		    		popup_window.dismiss();
		    		popup_active = false;
			    }
			});
		    tower4_btn.setOnClickListener(new OnClickListener() {
		    	@Override
		    	public void onClick(View v) {
		    		if(money>=200){
		    			money-=200;
		    			Towers.add(new Tower(tower4,touch_x,touch_y));
		    		}
		    		
		    		popup_window.dismiss();
		    		popup_active = false;
			    }
			});
			return true;
		}
		else{ //If popup window already showing, do nothing
			return false;
		}
	}
	/***
	 * Decides whether or not a tower can be placed down,
	 * Towers can only reside on 0 tiles that touch the path.
	 * @param x grid coordinate of selection
	 * @param y grid coordinate of selection
	 * @return 
	 */
	public boolean validPos(int x, int y){
		int viewHeight = this.getHeight();
		int viewWidth = this.getWidth();
		int gridWidth = viewWidth/fieldOfBattle[0].length;
		int gridHeight = viewHeight/fieldOfBattle.length;
		int gridX = (x/gridWidth);
		int gridY = (y/gridHeight);
		
		if(fieldOfBattle[gridY][gridX] != 14)
			return false;
		return true;
	}
	public int wash(int x,int max){
		return (x/max) * max;
	}
	public void setMapGrid(int map_number){
    	fieldOfBattle = new int[MapView.Y_TILE_COUNT][MapView.X_TILE_COUNT];
    	for (int y = 0; y < MapView.Y_TILE_COUNT; y++) {
            for (int x = 0; x < MapView.X_TILE_COUNT; x++) {
                fieldOfBattle[y][x] = MapConfig.getMap(0)[y][x];
            }
        }
    }
}
