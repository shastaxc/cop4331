package cop4331.cloud9001.bentd;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Enemy {
	private int[] DIRECTION_TO_ANIMATION_MAP = {3,1,0,2};
	private static final int BMP_ROWS = 4;
	private static final int BMP_COLUMNS = 3;
	protected int x = 0; 
	protected int y = 0;
	private int direction = 0;//0 up,1 right, 2 down, 3 left
    private int xSpeed = 3;
    private int ySpeed = 3;
    protected int health = 10;
    protected int radius = 5;
    protected int hitCenterX = 0;
    protected int hitCenterY = 0;
    protected int bounty = 10;
    private Bitmap bmp;
    private Path path;
    
    private int currentFrame = 0;
    private int width;
    private int height;
   
    public Enemy(GameView gameView, Bitmap bmp) {
          //this.gameView=gameView;
          this.bmp=bmp;
          width = bmp.getWidth() / BMP_COLUMNS;
          height = bmp.getHeight() / BMP_ROWS;
          path = new Path();
          Random rnd = new Random();
          xSpeed = rnd.nextInt(10);
          ySpeed = rnd.nextInt(10);
    }
    public Enemy(GameView gameView, int[][] fieldOfBattle,Bitmap bmp) {
    	//this.gameView=gameView;
        this.bmp=bmp;
        width = bmp.getWidth() / BMP_COLUMNS;
        height = bmp.getHeight() / BMP_ROWS;
        path = new Path(fieldOfBattle,gameView);
        x = path.nextX();
        y = path.nextY();
	}
    public Enemy(GameView gameView, int[][] fieldOfBattle,int id){
    	//this.bmp=bmp;
    	this.bmp = getEnemyBmp(gameView,id);
    	switch(id){
    	case 0:
    		hitCenterX = (int)((this.bmp.getWidth())/6);
    		hitCenterY = (int)((this.bmp.getHeight())/8);
    		break;
    		default:
    			Random rnd = new Random();
    			xSpeed += rnd.nextInt(3);
    			ySpeed += rnd.nextInt(3);
    			break;
    	}
    	
        width = bmp.getWidth() / BMP_COLUMNS;
        height = bmp.getHeight() / BMP_ROWS;
        path = new Path(fieldOfBattle,gameView);
        //Log.i("Enemy_56", path.toString());
        x = path.nextX();
        y = path.nextY();
    }
    private Bitmap getEnemyBmp(GameView gv,int id) {
		if(id==0)
			return BitmapFactory.decodeResource(gv.getResources(), R.drawable.bad1);
		else if(id==1)
			return BitmapFactory.decodeResource(gv.getResources(), R.drawable.enemy_imp);
		else if(id==2)
			return BitmapFactory.decodeResource(gv.getResources(), R.drawable.enemy_kitsune);
		else if(id==3)
			return BitmapFactory.decodeResource(gv.getResources(), R.drawable.enemy_oni);
		return null;
	}
	private void updateDirection(Path p){
    	if(p.currentWaypoint() == 0){
    		direction = 1;
    		return;
    	}
    	int oldX = p.oldX();
    	int oldY = p.oldY();
    	int newX = p.nextX();
    	int newY = p.nextY();
    	
    	if(oldX == newX){
    		if(oldY < newY)
    			direction = 2;
    		else
    			direction = 0;
    	}
    	else{
    		if(oldX < newX)
    			direction = 1;
    		else
    			direction = 3;
    	}
    	
    }
	private void update() {
    	if(!path.done()){
    		updateDirection(path);
    		if(direction == 0){
    			y-=ySpeed;
    			if(y <= path.nextY())
    				path.nextWaypoint();
    		}
    		else if(direction == 1){
    			x+=xSpeed;
    			if(x >= path.nextX())
    				path.nextWaypoint();
    		}
    		else if(direction == 2){
    			y+=ySpeed;
    			if(y >= path.nextY())
    				path.nextWaypoint();
    		}
    		else if(direction == 3){
    			x-=xSpeed;
    			if(x <= path.nextX())
    				path.nextWaypoint();
    		}
    		/*if(x < path.nextX())
    			x+=xSpeed;
    		if(y < path.nextY())
    			y+=ySpeed;
    		
    		if(x >= path.nextX())
    			path.nextWaypoint();
    		if( y >= path.nextY())
    			path.nextWaypoint();
    		*/
    		/*
    		if(!path.done()){
        		if(x < path.nextX())
        			x+=xSpeed;
        		if(y < path.nextY())
        			y+=ySpeed;
        		else if(x >= path.nextX())
        			path.nextWaypoint();
        	}
        	*/
    	}
    	currentFrame = ++currentFrame % BMP_COLUMNS;
    }
    private int getAnimationRow(){
    	double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI/2)+2);
    	int direction =(int) Math.round(dirDouble) % BMP_ROWS;
    	return DIRECTION_TO_ANIMATION_MAP[direction];
    }
    @SuppressLint("DrawAllocation") 
    public void onDraw(Canvas canvas) {
          update();
          int srcX = currentFrame * width;
          int srcY = 1 * height;
          Rect src = new Rect(srcX, srcY, srcX + width, srcY+height);
          Rect dst = new Rect(x,y,x+width, y+height);
          canvas.drawBitmap(bmp, src, dst, null);
    }
    private static int distance(Enemy e, int x, int y){
    	return (int)(Math.sqrt((e.x-x)*(e.x-x)+(e.y-y)*(e.y-y)));
    }
	public static Enemy nearestEnemy(ArrayList<Enemy> Enemies, int X, int Y) {
		if(Enemies.size() <= 0)
			return null;
		Enemy victim = Enemies.get(0);
		for(Enemy e:Enemies){
			if(distance(e,X,Y) < distance(victim,X,Y))
				victim = e;
		}
		return victim;
	}
}
