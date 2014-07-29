package cop4331.cloud9001.bentd;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class Enemy {
	private int[] DIRECTION_TO_ANIMATION_MAP = {3,1,0,2};
	private int BMP_ROWS = 4;
	private int BMP_COLUMNS = 3;
	protected int x = 0; 
	protected int y = 0;
	private int direction = 0;//0 up,1 right, 2 down, 3 left
    private int xSpeed;
    private int ySpeed;
    protected int strength;
    protected int health;
    protected int radius;
    protected int hitCenterX = 0;
    protected int hitCenterY = 0;
    protected int bounty;
    private Bitmap bmp;
    private Path path;
    
    private int currentFrame = 0;
    private int width;
    private int height;
   
    /*public Enemy(GameView gameView, Bitmap bmp) {
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
	}*/
    public Enemy(GameView gameView, int[][] fieldOfBattle,int id){
    	//this.bmp=bmp;
    	this.bmp = getEnemyBmp(gameView,id);
    	//radius = bmp.getWidth()/4;
    	
		Random rnd = new Random();
    	switch(id){
    	case 0:
    		BMP_COLUMNS = 3;
    		BMP_ROWS = 4;
    		xSpeed = rnd.nextInt(4)+1;
    		ySpeed = rnd.nextInt(4)+1;
    		break;
    	case 1:
    		BMP_COLUMNS = 3;
    		BMP_ROWS = 3;
    		xSpeed = (int) Math.sqrt(rnd.nextInt(11)+5);
    		ySpeed = xSpeed;
    		bounty = 5;
    		strength = 3;
    		health = 15;
    		break;
    	case 2:
    		BMP_COLUMNS = 3;
    		BMP_ROWS = 3;
    		xSpeed = (int) Math.sqrt(rnd.nextInt(20)+8);
    		ySpeed = xSpeed;
    		bounty = 15;
    		strength = 1;
    		health = 12;
    		break;
    	default:
    		break;
    	}
    	
    	hitCenterX = (int)((bmp.getWidth())/BMP_COLUMNS)/2;
		hitCenterY = (int)((bmp.getHeight())/BMP_ROWS)/2;
        width = bmp.getWidth() / BMP_COLUMNS;
        height = bmp.getHeight() / BMP_ROWS;
        radius = width/2;
        path = new Path(fieldOfBattle,gameView);
        x = path.nextX();
        y = path.nextY();
        y+= rnd.nextInt(gameView.getHeight()/fieldOfBattle.length);
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
	public void update() {
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
    	}
    	currentFrame = ++currentFrame % BMP_ROWS;
    }
    public void onDraw(Canvas canvas) {
          //update();
    	  int fixedDirection = 0;
    	  switch(direction){
    	  case 0:
    		  fixedDirection = 1;
    		  break;
    	  case 1:
    		  fixedDirection = 2;
    		  break;
    	  case 2:
    		  fixedDirection = 0;
    	  }
          int srcX = fixedDirection * width;
          int srcY = currentFrame * height;
          Rect src = new Rect(srcX, srcY, srcX + width, srcY+height);
          Rect dst = new Rect(x,y,x+width, y+height);
          canvas.drawBitmap(bmp, src, dst, null);
    }
    private static int distance(Enemy e, int x, int y){
    	return (int)(Math.sqrt((e.x-x)*(e.x-x)+(e.y-y)*(e.y-y)));
    }
	public static Enemy nearestEnemy(ArrayList<Enemy> Enemies, int X, int Y, int range) {
		if(Enemies.size() <= 0)
			return null;
		Enemy victim = Enemies.get(0);
		for(Enemy e:Enemies){
			if(distance(e,X,Y) < distance(victim,X,Y))
				victim = e;
		}
		if(distance(victim,X,Y) > range)
			return null;
		return victim;
	}
}
