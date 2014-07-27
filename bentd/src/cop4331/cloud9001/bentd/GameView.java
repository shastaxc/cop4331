package cop4331.cloud9001.bentd;

import java.util.ArrayList;
import java.util.Iterator;

//import android.R;
import cop4331.cloud9001.bentd.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
//import android.view.View;
import android.widget.TextView;

@SuppressLint({ "WrongCall", "ClickableViewAccessibility", "DrawAllocation" }) 
public class GameView extends SurfaceView {
    private Bitmap bmp;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread; //Periodic call for GameView.onDraw();
    protected ArrayList<Enemy> Enemies; //Enemies on screen
    protected ArrayList<Tower> Towers; //Towers on screen
    protected MapConfig level;
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
    protected long startOfWaveInMiliseconds = 0;
    
    /*
     * STATS BAR
     *
    protected static Button pause_btn;
	protected static Button forward_btn;
	protected static TextView currency_textview;
	protected static TextView life_textview;
	protected static TextView wave_textview;
	protected static TextView time_remaining_textview;
	protected static LinearLayout text_layout;
	protected static RelativeLayout stats_bar_layout;*/
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
    protected void onDraw2(Canvas canvas){
    	money++;
    }
    @Override
    protected void onDraw(Canvas canvas) {
    	//Update Wave
    	//GameInstance.currency_textview.setText("1234");
    	long timeRemaining = System.currentTimeMillis() - startOfWaveInMiliseconds;
    	if(!spawnedFirstWave){
    		spawnedFirstWave = true;
    		for(int i=0;i<level.EnemiesPerWave[currentWave];i++)
				Enemies.add(new Enemy(this,fieldOfBattle,0));
    		startOfWaveInMiliseconds = System.currentTimeMillis();
    	}
    	if(timeRemaining < level.timePerWave){
    		//UPDATE STATUS BAR
    		//GameInstance.modifyStatBar(money);
    	}
    	else{//Wave is done, Spawn Next one
    		currentWave++;
    		if(currentWave < maxWaves)
    			for(int i=0;i<level.EnemiesPerWave[currentWave];i++)
    				Enemies.add(new Enemy(this,fieldOfBattle,0));
    		startOfWaveInMiliseconds = System.currentTimeMillis();
    	}
    	/*
    	 * DRAWING AND ENTITY UPDATES
    	 */
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		//TOWER TARGETING AND FIRING
		for(Tower t: Towers){
		   //Needs a target
		   if(t.target == null){
			   //Finds the closest enemy for attacking
			   Enemy e = Enemy.nearestEnemy(Enemies,t.getx(),t.gety());
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
    	for(Tower t: Towers){
    		t.onDraw(canvas);
    		for(Bullet b: t.Bullets)
    			b.onDraw(canvas);
    	}
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
    		e.onDraw(canvas);
    	}
    }
	
	@Override
	/***
	 * When the screen is tapped, the view checks if a tower can be placed.
	 */
	public boolean onTouchEvent(MotionEvent event){
		if (System.currentTimeMillis() - lastClick > 500) {
			lastClick = System.currentTimeMillis();
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.towerdefault);
			int x = wash((int)event.getX(),bmp.getWidth());
			int y = wash((int)event.getY(),bmp.getHeight());
			boolean empty = true, valid = false;
			if(validPos(x,y)){
				valid = true;
				for(Tower t: Towers){
					if(t.getx()==x && t.gety()==y)
						empty = false;	
				}
			}
			if(empty && valid)
				Towers.add(new Tower(bmp,x,y));
			//Enemies.add(new Enemy(this, fieldOfBattle,0));
		}
		return true;
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
		
		if(fieldOfBattle[gridY][gridX] != 0)
			return false;
		//Check UP
		if(gridY > 0 && fieldOfBattle[gridY-1][gridX] >0)
			return true;
		//Check LEFT
		if(gridX > 0 && fieldOfBattle[gridY][gridX-1] >0)
			return true;
		//Check RIGHT
		if(gridX < (fieldOfBattle[0].length-1) && fieldOfBattle[gridY][gridX+1] >0)
			return true;
		//Check DOWN
		if(gridY < (fieldOfBattle.length-1) && fieldOfBattle[gridY+1][gridX] >0)
			return true;
		return false;
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
