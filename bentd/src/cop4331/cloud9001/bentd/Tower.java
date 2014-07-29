package cop4331.cloud9001.bentd;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;

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
		this.centerY = bmp.getHeight()/2;
		Bullets = new ArrayList<Bullet>();
		switch(i){
		case 1:
			strength = 5;
			this.range = (int) (range*2);
			fireSpeed = 500;
			bulletSpeed = 100;
			bulletID = 0;
			towerID = 1;
			break;
		case 2:
			
			break;
		case 4:
			fireSpeed = 2000;
			range = 200;
			bulletSpeed = 10;
			strength = 20;
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
		canvas.drawBitmap(bmp, x,y, null);
	}
	public void fire(GameView gameView2, Bitmap[] arrow) {
		lastFired = System.currentTimeMillis();
		Bullets.add(new Bullet(target,x,y,strength,bulletSpeed,arrow));
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
