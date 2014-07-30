package cop4331.cloud9001.bentd;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
//import android.R;
//import android.view.View;

@SuppressLint({ "WrongCall", "ClickableViewAccessibility", "DrawAllocation" }) 
public class GameView extends SurfaceView {
    /*
     * Towers
     */
    private final Bitmap tower1 = BitmapFactory.decodeResource(getResources(),  R.drawable.tower_archer);//50g
	private final Bitmap tower2 = BitmapFactory.decodeResource(getResources(),  R.drawable.tower_ninja);//75g
	private final Bitmap tower3 = BitmapFactory.decodeResource(getResources(),  R.drawable.tower_shrine);//150g
	private final Bitmap tower4 = BitmapFactory.decodeResource(getResources(),  R.drawable.tower_ballista_idle);//200g
	/*
	 * Enemies
	 */
	private final Bitmap enemyOgre = BitmapFactory.decodeResource(getResources(),  R.drawable.enemy_oni);
	private final Bitmap enemyImp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_imp);
	private final Bitmap enemyKitsune = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_kitsune);
	private final Bitmap bossOroshi = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_oroshi);
	/*
	 * Bullets
	 */
	private final Bitmap arrow[] = {BitmapFactory.decodeResource(getResources(),  R.drawable.projectile_arrow_u),
			BitmapFactory.decodeResource(getResources(),  R.drawable.projectile_arrow_ru),
			BitmapFactory.decodeResource(getResources(),  R.drawable.projectile_arrow_r),
			BitmapFactory.decodeResource(getResources(),  R.drawable.projectile_arrow_rd),
			BitmapFactory.decodeResource(getResources(),  R.drawable.projectile_arrow_d),
			BitmapFactory.decodeResource(getResources(),  R.drawable.projectile_arrow_ld),
			BitmapFactory.decodeResource(getResources(),  R.drawable.projectile_arrow_l),
			BitmapFactory.decodeResource(getResources(),  R.drawable.projectile_arrow_lu)};
	private final Bitmap caltrops = BitmapFactory.decodeResource(getResources(),  R.drawable.projectile_caltrops);
	private final Bitmap fireBallA = BitmapFactory.decodeResource(getResources(),  R.drawable.projectile_fireball_a);
	private final Bitmap fireBallB = BitmapFactory.decodeResource(getResources(),  R.drawable.projectile_fireball_b);
	/*
	 * View Variables
	 */
    private SurfaceHolder holder;
    protected GameLoopThread gameLoopThread; //Periodic call for GameView.onDraw();
    protected static ArrayList<Enemy> Enemies; //Enemies on screen
    protected static ArrayList<Tower> Towers; //Towers on screen

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
    protected boolean spawnedWave = false;
    protected boolean spawnSubWave1 = false;
    protected boolean spawnSubWave2 = false;
    protected long startOfWaveInMiliseconds = 0;
	protected static boolean fast = false;
	protected static boolean slow = true;
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
    	if(timeRemaining > level.timePerWave){
    		spawnedWave = false;
    		spawnSubWave1 = false;
    		spawnSubWave2 = false;
    	}
    	if(!spawnedWave && currentWave == 0){
    		spawnedWave = true;
    		for(int i=0;i<level.EnemiesPerWave[currentWave];i++)
				Enemies.add(new Enemy(this,fieldOfBattle,1,enemyImp));
    		startOfWaveInMiliseconds = System.currentTimeMillis();
    		currentWave++;
    		if(fast){
    			for(Enemy e: Enemies)
    				e.speedUP();
    		}
    	}
    	else if(!spawnedWave&& currentWave == 1){
    		spawnedWave = true;
    		currentWave++;
    		//ENEMY SPAWN
    		for(int i=0;i<level.EnemiesPerWave[currentWave]-1;i++)
    			Enemies.add(new Enemy(this,fieldOfBattle,1,enemyImp));
    		Enemies.add(new Enemy(this,fieldOfBattle,3, enemyOgre));
    		startOfWaveInMiliseconds = System.currentTimeMillis();
    		//SPEED CORRECTION
    		if(fast){
    			for(Enemy e: Enemies)
    				e.speedUP();
    		}
    	}
    	else if(!spawnedWave && currentWave ==2){
    		spawnedWave = true;
    		currentWave++;
    		//ENEMY SPAWN
    		for(int i=0;i<level.EnemiesPerWave[currentWave]-3;i++)
    			Enemies.add(new Enemy(this,fieldOfBattle,1,enemyImp));
    		Enemies.add(new Enemy(this,fieldOfBattle,2, enemyKitsune));
    		Enemies.add(new Enemy(this,fieldOfBattle,3, enemyOgre));
    		Enemies.add(new Enemy(this,fieldOfBattle,3, enemyOgre));
    		startOfWaveInMiliseconds = System.currentTimeMillis();
    		//SPEED CORRECTION
    		if(fast){
    			for(Enemy e: Enemies)
    				e.speedUP();
    		}
    	}
    	else if(!spawnedWave && currentWave == (maxWaves -1)){
    		startOfWaveInMiliseconds = System.currentTimeMillis();
    		currentWave++;
    		spawnedWave = true;
    		
    		Enemies.add(new Enemy(this,fieldOfBattle,5,bossOroshi));
    		if(fast){
    			for(Enemy e: Enemies)
    				e.speedUP();
    		}
    	}
    	else if(!spawnedWave&& currentWave < (maxWaves-1)){
    		startOfWaveInMiliseconds = System.currentTimeMillis();
    		currentWave++;
    		spawnedWave = true;
    		
    		int spawnMax = level.EnemiesPerWave[currentWave]/2;
    		Random rnd = new Random();
    		int chance = 0;
    		level.EnemiesPerWave[currentWave]-= spawnMax;
    		for(int i = 0;i<spawnMax;i++){
    			chance =rnd.nextInt(100);
    			if(chance <= 10)
    				Enemies.add(new Enemy(this,fieldOfBattle,2, enemyKitsune));
    			else if(chance <= 30)
    				Enemies.add(new Enemy(this,fieldOfBattle,3, enemyOgre));
    			else
    				Enemies.add(new Enemy(this,fieldOfBattle,1,enemyImp));
    		}
    		if(fast){
    			for(Enemy e: Enemies)
    				e.speedUP();
    		}
    	}
    	else if(timeRemaining < ((2/3)*level.timePerWave) && currentWave > 3 && currentWave < maxWaves-1 && !spawnSubWave1){
    		//SUB WAVE 1
    		spawnSubWave1 = true;
    		
    		//ENEMY SPAWN
    		int spawnMax = level.EnemiesPerWave[currentWave]/2;
    		level.EnemiesPerWave[currentWave]-= spawnMax;
    		Random rnd = new Random();
    		int chance = 0;
    		level.EnemiesPerWave[currentWave]-= spawnMax;
    		for(int i = 0;i<spawnMax;i++){
    			chance =rnd.nextInt(100);
    			if(chance <= 10)
    				Enemies.add(new Enemy(this,fieldOfBattle,2, enemyKitsune));
    			else if(chance <= 30)
    				Enemies.add(new Enemy(this,fieldOfBattle,3, enemyOgre));
    			else
    				Enemies.add(new Enemy(this,fieldOfBattle,1,enemyImp));
    		}
    	}
    	else if(timeRemaining < (level.timePerWave/3) && currentWave > 2 && currentWave < (maxWaves-1) && !spawnSubWave2){
    		//SUB WAVE 2
    		spawnSubWave2 = true;
    		
    		//ENEMY SPAWN
    		int spawnMax = level.EnemiesPerWave[currentWave];
    		Random rnd = new Random();
    		int chance = 0;
    		level.EnemiesPerWave[currentWave]-= spawnMax;
    		for(int i = 0;i<spawnMax;i++){
    			chance =rnd.nextInt(100);
    			if(chance <= 10)
    				Enemies.add(new Enemy(this,fieldOfBattle,2, enemyKitsune));
    			else if(chance <= 30)
    				Enemies.add(new Enemy(this,fieldOfBattle,3, enemyOgre));
    			else
    				Enemies.add(new Enemy(this,fieldOfBattle,1,enemyImp));
    		}
    	}
    	
    	//If all enemies are killed and this is not the last wave
    	//change back to RUNNING mode and change button image to spawn_next icon
    	if(Enemies.size() == 0 && currentWave < maxWaves){
    		if(GameInstance.basic_map_view.getMode() == MapView.FAST_FORWARD){
    			Message msg = new Message();
    			GameInstance.ffPress.sendMessage(msg);
    		}
    	}
    	
    	//ENDING CONDITIONS
    	if(currentWave >= maxWaves && Enemies.size()==0 && health >0){
    		currentWave = maxWaves;
    		//SEND VICTORY MESSAGE TO GAME INSTANCE
    		Message msg = new Message();
    		String textToChange = "VICTORY";
    		msg.obj = textToChange;
    		GameInstance.endHandler.sendMessage(msg);
    		
    		//Stop thread
    		gameLoopThread.setRunning(false);
    		try {
				gameLoopThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	else if(health < 0){
    		//SEND DEFEAT MESSAGE TO GAME INSTANCE
    		Message msg = new Message();
    		String textToChange = "DEFEAT";
    		msg.obj = textToChange;
    		GameInstance.endHandler.sendMessage(msg);

    		//Stop thread
    		gameLoopThread.setRunning(false);
    		try {
				gameLoopThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	/*
    	 * ENTITY UPDATES
    	 */
		//TOWER TARGETING
		for(Tower t: Towers){
		   //Needs a target
		   if(t.target == null){
			   //Finds at the closest enemy
			   Enemy e = Enemy.nearestEnemy(Enemies,t.getx()+t.centerX,t.gety()+t.centerY,t.range);
			   if(e == null)
				   break;
			   else
				   //Fires at enemy
				   if(System.currentTimeMillis() - t.lastFired > t.fireSpeed ){
					   t.target = e;
					   if(t.towerID == 1)
						   t.fire(this, arrow);
				   }
		   }
		   //Has a target
		   else{
			   //Fires at enemy
			   if(System.currentTimeMillis() - t.lastFired > t.fireSpeed && t.Bullets.size() < t.MAX_BULLETS){
				   if(t.towerID == 1)
					   t.fire(this, arrow);
			   }
		   }
		}
		//TOWER AND BULLET UPDATES / DRAW
    	for(Tower t: Towers){
    		for(int i=0;i<t.Bullets.size();i++){
    			t.Bullets.get(i).update();
    			if(t.Bullets.get(i).getLifeSpane() > 5000){
    				t.target = null;
    				t.Bullets.remove(i--);
    			}
    		}
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
    		e.update();
    		if(wash(e.y,(GameInstance.game_view.getHeight()/MapView.Y_TILE_COUNT)) <=0 
    				&& wash(e.x,(GameInstance.game_view.getWidth()/MapView.X_TILE_COUNT))>=8){
    			health-=e.strength;
    			Enemies.remove(i--);
    		}		
    	}
    }
    static Handler ffHandler = new Handler(){
    	@Override
		public void handleMessage(Message msg){
			String text = (String)msg.obj;
			if(text.compareTo("fast")==0){
				if(Enemies.size() == 0){
					GameInstance.game_view.startOfWaveInMiliseconds = 0;
				}
				else if(!fast){
					//SPEED EVERYTHING UP IN towers and enemies
					for(Tower t: Towers)
						t.speedUP();
					for(Enemy e: Enemies)
						e.speedUP();
				}
				fast = true;
				slow = false;
			}
			else if(text.compareTo("slow")==0){
				if(!slow){
					//SLOW EVERYTHING DOWN IN tower and enemies
					for(Tower t: Towers)
						t.slowDown();
					for(Enemy e: Enemies)
						e.slowDown();
				}
				fast = false;
				slow = true;
			}
		}
    };
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
	protected void onDrawClear(Canvas canvas){
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
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
	            
		    LinearLayout tower1_layout = (LinearLayout) popupView.findViewById(R.id.tower1_layout);
		    LinearLayout tower2_layout = (LinearLayout) popupView.findViewById(R.id.tower2_layout);
		    LinearLayout tower3_layout = (LinearLayout) popupView.findViewById(R.id.tower3_layout);
		    LinearLayout tower4_layout = (LinearLayout) popupView.findViewById(R.id.tower4_layout);
		    
		    tower1_layout.setOnClickListener(new OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		if(money>=50){
	    			money-=50;
	    			Towers.add(new Tower(tower1,touch_x,touch_y,1, 100));
	    		}
	    		popup_window.dismiss();
	    		popup_active = false;
		    }
		    });
		    tower2_layout.setOnClickListener(new OnClickListener() {
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
		    tower3_layout.setOnClickListener(new OnClickListener() {
		    	@Override
		    	public void onClick(View v) {
		    		if(money>=150){
		    			money-=150;
		    			Towers.add(new Tower(tower3,touch_x,touch_y));
		    		}
		    		popup_window.dismiss();
		    		popup_active = false;
			    }
			});
		    tower4_layout.setOnClickListener(new OnClickListener() {
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
