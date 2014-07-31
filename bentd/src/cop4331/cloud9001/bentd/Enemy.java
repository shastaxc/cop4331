package cop4331.cloud9001.bentd;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


@SuppressLint("DrawAllocation")
public class Enemy {
	private int BMP_ROWS = 4;
	private int BMP_COLUMNS = 3;
	protected int x = 0; 
	protected int y = 0;
	private int direction = 0;//0 up,1 right, 2 down, 3 left
    protected int xSpeed;
    protected int ySpeed;
    protected int strength;
    protected int health;
    protected int radius;
    protected int hitCenterX = 0;
    protected int hitCenterY = 0;
    protected int bounty;
    protected boolean isSlowed = false;
    protected long lastSlowed = 0;
    private Bitmap bmp;
    private Path path;
    
    private int id;
    private int currentFrame = 0;
    private int width;
    private int height;

    public Enemy(GameView gameView, int[][] fieldOfBattle,int id, Bitmap bmp){
    	this.bmp=bmp;
    	this.id = id;
		Random rnd = new Random();
    	switch(id){
    	case 0:
    		BMP_COLUMNS = 3;
    		BMP_ROWS = 4;
    		xSpeed = rnd.nextInt(4)+1;
    		ySpeed = rnd.nextInt(4)+1;
    		break;
    	case 1://IMPS
    		BMP_COLUMNS = 3;
    		BMP_ROWS = 3;
    		xSpeed = (int) Math.sqrt(rnd.nextInt(10)+10);
    		ySpeed = xSpeed;
    		bounty = 10;
    		strength = 3;
    		health = 20;
    		break;
    	case 2://FOX
    		BMP_COLUMNS = 3;
    		BMP_ROWS = 3;
    		xSpeed = (int) Math.sqrt(rnd.nextInt(10)+15);
    		ySpeed = xSpeed;
    		bounty = 20;
    		strength = 1;
    		health = 20;
    		break;
    	case 3://OGRE
    		BMP_COLUMNS = 3;
    		BMP_ROWS = 3;
    		xSpeed = (int) Math.sqrt(rnd.nextInt(10)+5);
    		ySpeed = xSpeed;
    		bounty = 30;
    		strength = 10;
    		health = 30;
    		break;
    	case 5:
    		BMP_COLUMNS = 8;
    		BMP_ROWS = 2;
    		xSpeed = (int) Math.sqrt(rnd.nextInt(10)+3);
    		ySpeed = xSpeed;
    		bounty = 1000;
    		strength = 90001;//IT"S HUUUUUUUUGEEEEEEEEEE!!!!!!!!
    		health = 400;
    		break;
    	default:
    		break;
    	}
    	hitCenterX = (int)((bmp.getWidth())/BMP_COLUMNS)/2;
		hitCenterY = (int)((bmp.getHeight())/BMP_ROWS)/2;
        width = bmp.getWidth() / BMP_COLUMNS;
        height = bmp.getHeight() / BMP_ROWS;
        radius = width/4;
        path = new Path(fieldOfBattle,gameView);
        x = path.nextX();
        y = path.nextY();
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
    		updateY();
    		updateX();
    		
    		
    	}
    	currentFrame = ++currentFrame % BMP_ROWS;
    }
	private void updateX(){
		if(direction == 1){
			x+=xSpeed;
			if(x >= path.nextX())
				path.nextWaypoint();
		}
		else if(direction == 3){
			x-=xSpeed;
			if(x <= path.nextX())
				path.nextWaypoint();
		}
		else{
			if(x < path.nextX())
				x += xSpeed;
			else if(x > path.nextX())
				x -= xSpeed;
		}
	}
	private void updateY(){
		if(direction == 0){
			y-=ySpeed;
			if(y <= path.nextY())
				path.nextWaypoint();
			
		}
		else if(direction == 2){
			y+=ySpeed;
			if(y >= path.nextY())
				path.nextWaypoint();
		}
		else{
			if(y < path.nextY())
				y += ySpeed;
			else if(y > path.nextY())
				y -= ySpeed;
		}
	}
    public void onDraw(Canvas canvas) {
          //update();
    	int srcX = 0, srcY = 0;
        if(id == 5){
        	if(health >= 360)
        		srcX = 0;
        	else
        		srcX = (8-((((health)/50)+8)%8))*width;
        	srcY = currentFrame * height;
        }
        else{
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
        	srcX = fixedDirection * width;
        	srcY = currentFrame * height;
        }
        Rect src = new Rect(srcX, srcY, srcX + width, srcY+height);
    	Rect dst = new Rect(x,y,x+width, y+height);
    	canvas.drawBitmap(bmp, src, dst, null);
    }
    public static int distance(Enemy e, int x, int y){
    	return (int)(Math.sqrt((e.x+e.hitCenterX-x)*(e.x+e.hitCenterX-x)+(e.y+e.hitCenterY-y)*(e.y+e.hitCenterY-y)));
    }
	public static Enemy nearestEnemy(ArrayList<Enemy> Enemies, int X, int Y, int range) {
		if(Enemies.size() <= 0)
			return null;
		Enemy victim = Enemies.get(0);
		for(Enemy e:Enemies){
			if(distance(e,X,Y) < distance(victim,X,Y))
				victim = e;
		}
		if(distance(victim,X,Y) > range-victim.radius)
			return null;
		return victim;
	}
	public void speedUP() {
		xSpeed *= 2;
	    ySpeed *= 2;
	}
	public void slowDown() {
		xSpeed /= 2;
	    ySpeed /= 2;
	}
}
