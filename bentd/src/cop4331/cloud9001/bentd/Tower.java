package cop4331.cloud9001.bentd;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tower {
	private int x;
	private int y;
	protected int centerX;
	protected int centerY;
	protected int targetingRadius;
	@SuppressWarnings("unused")
	private GameView gameView;
    private Bitmap bmp;
    protected int MAX_BULLETS = 5;
    
    private int strength;
    protected int range;
    protected long fireSpeed;//Time in ms between each shot
    protected int bulletSpeed;
    protected int bulletID; //Normal bullet = 0, AOE = 1, AOE/DEBUFF = 2
    protected int towerID;
    protected long lastFired;
    protected ArrayList<Bullet> Bullets;
    protected Enemy target = null;
	private Bitmap bmp2;
	private int direction;
	private boolean directionals[] = {false,false,false};
	protected boolean idle = true;
    /******
     * Constructor
     * @param bmp
     * @param x
     * @param y
     */
    public Tower(Bitmap bmp, int x, int y) {
        this.bmp=bmp;
        this.x = x;
        this.y = y;
        Bullets = new ArrayList<Bullet>();
	}
	public Tower(GameView gameView, Bitmap bmp, int x, int y) {
        this.gameView=gameView;
        this.bmp=bmp;
        this.x = x;
        this.y = y;
        Bullets = new ArrayList<Bullet>();
	}
	
	public Tower(Bitmap tower1, int touch_x, int touch_y, int i, int range) {
		this.bmp = tower1;
		this.x = touch_x;
		this.y = touch_y;
		this.centerX = bmp.getWidth()/2;
		this.centerY = bmp.getHeight();
		Bullets = new ArrayList<Bullet>();
		switch(i){
		case 1://Arrow
			strength = 5;
			this.range = (int) (range*2);
			fireSpeed = 1000;
			bulletSpeed = 150;
			bulletID = 0;
			towerID = 1;
			break;
		case 2:
			
			break;
		default:
			break;
		}
	}
	public Tower(Bitmap tower4, int touch_x, int touch_y,
			int i, int range, int direction) {
		this.bmp = tower4;
		this.direction = direction;
		this.x = touch_x;
		this.y = touch_y;
		this.centerX = bmp.getWidth()/2;
		this.centerY = bmp.getHeight();
		Bullets = new ArrayList<Bullet>();
		switch(i){
		case 2://NINJA
			fireSpeed = 3000;
			this.range = (int)(range*2);
			//bulletSpeed = 150;
			//strength = 40;
			bulletID = 1;
			towerID = 2;
			break;
		case 4://BALLISTA
			fireSpeed = 2000;
			this.range = (int)(range*2);
			bulletSpeed = 150;
			strength = 40;
			bulletID = 0;
			towerID = 4;
			break;
		default:
			break;
		}
	}
	public int getx() {
		return x;
	}
	public void setx(int x) {
		this.x = x;
	}
	public int gety() {
		return y;
	}
	public void sety(int y) {
		this.y = y;
	}
	@SuppressLint("DrawAllocation") 
	public void onDraw(Canvas canvas) {
		if(towerID==4){
			if(idle){
				int height = bmp.getHeight()/2;
				int width = bmp.getWidth()/8;
				Rect src = new Rect(direction*width,0, direction*width+width, 0+height);
				Rect dst = new Rect(x,y,x+width, y+height);
				canvas.drawBitmap(bmp, src, dst, null);
			}
			else{
				int height = bmp.getHeight()/2;
				int width = bmp.getWidth()/8;
				Rect src = new Rect(direction*width,height, direction*width+width, height+height);
				Rect dst = new Rect(x,y,x+width, y+height);
				canvas.drawBitmap(bmp, src, dst, null);
			}
			//canvas.drawBitmap(bmp, x,y, null);
		}
		else
			canvas.drawBitmap(bmp, x,y, null);
		
	}
	public void fire(GameView gameView2, Bitmap[] arrow) {
		lastFired = System.currentTimeMillis();
		if(towerID == 1 || towerID == 4)
			Bullets.add(new Bullet(target,x,y,strength,bulletSpeed,arrow,0));
		idle = true;
	}
	public void fire(GameView gameView, Bitmap bmp){
		if(towerID == 2 && Bullets.size() < MAX_BULLETS){//Ninja
			Random rnd = new Random();
			Bullets.add(new Bullet(rnd.nextInt(3)+1,x,(gameView.getWidth()/MapView.X_TILE_COUNT),y,(gameView.getHeight()/MapView.Y_TILE_COUNT),direction,bmp));
		}
	}
	public void speedUP() {
		fireSpeed /= 2;
	    bulletSpeed *=2;
	    for(Bullet b: Bullets)
	    	b.speedUP();
	}
	public void slowDown(){
		fireSpeed *=2;
		bulletSpeed /=2;
		for(Bullet b: Bullets)
	    	b.slowDown();
	}
}
