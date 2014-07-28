package cop4331.cloud9001.bentd;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Tower {
	private int x;
	private int y;
	protected int targetingRadius;
	@SuppressWarnings("unused")
	private GameView gameView;
    private Bitmap bmp;
    protected int MAX_BULLETS = 5;
    
    private int strength = 5;
    protected int range = 50;
    protected long fireSpeed = 1000;//Time in ms between each shot
    protected int bulletSpeed = 20;
    
    protected long lastFired = 0;
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
	
	public Tower(Bitmap tower1, int touch_x, int touch_y, int i) {
		this.bmp = tower1;
		this.x = touch_x;
		this.y = touch_y;
		Bullets = new ArrayList<Bullet>();
		switch(i){
		case 1:
			strength = 3;
			range = 100;
			fireSpeed = 1000;
			bulletSpeed = 50;
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
	public void fire(GameView gv) {
		lastFired = System.currentTimeMillis();
		Bullets.add(new Bullet(target,x,y,strength,bulletSpeed));
	}
}
