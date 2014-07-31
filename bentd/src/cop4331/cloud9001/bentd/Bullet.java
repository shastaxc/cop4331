package cop4331.cloud9001.bentd;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bullet {
	protected Enemy target;
	protected int posX;
	protected int posY;
	protected int radius = 10;
	protected int stoppingPower;
	protected int velocity;
	protected boolean destroyed = false;
	protected Paint paint;
	private Bitmap bmp[];
	private Bitmap bmp2;
	private int currentFrame = 0;
	private long birth = System.currentTimeMillis();
	private int bulletID;
	private ArrayList<Enemy> targets;
	
	public Bullet(Enemy e, int x, int y, int strength, int bulletSpeed, Bitmap[] bmpArr, int id) {
		bmp = bmpArr;
		target = e;
		posX = x;
		posY = y;
		stoppingPower = strength;
		velocity = bulletSpeed;
		bulletID = id;
	}
	public Bullet(int i, int x, int xmod, int y, int ymod, int direction,
			Bitmap bmp) {
		bmp2 = bmp;
		bulletID = 1;
		//012
		//7X3
		//654
		radius = bmp.getWidth();
		if(direction == 1){
			posX = x;
			posY = y-ymod;
		}
		else if(direction == 0){
			if(i==1){
				posX = x-xmod;
				posY = y;
			}
			else if(i==2){
				posX = x;
				posY = y-ymod;
			}
			else{
				posX = x-xmod;
				posY = y-ymod;
			}
		}
		else if(direction == 2){
			if(i==1){
				posX = x;
				posY = y-ymod;
			}
			else if(i==2){
				posX = x+xmod;
				posY = y;
			}
			else{
				posX = x+xmod;
				posY = y-ymod;
			}
		}
		else if(direction == 3){
			posX = x+xmod;
			posY = y;
		}
		else if(direction == 4){
			if(i==1){
				posX = x+xmod;
				posY = y;
			}
			else if(i==2){
				posX = x+xmod;
				posY = y+ymod;
			}
			else{
				posX = x;
				posY = y+ymod;
			}
		}
		else if(direction == 5){
			posX = x;
			posY = y+ymod;
		}
		else if(direction == 6){
			if(i==1){
				posX = x-xmod;
				posY = y;
			}
			else if(i==2){
				posX = x-xmod;
				posY = y+ymod;
			}
			else{
				posX = x;
				posY = y+ymod;
			}
		}
		else if(direction == 7){
			posX = x-xmod;
			posY = y;
		}
	}
	public Bullet(Enemy target2, int x, int y, int strength, int bulletSpeed,
			Bitmap fireBall, int id) {
		target = target2;
		posX = x;
		posY = y;
		this.stoppingPower = strength;
		velocity = bulletSpeed;
		bmp2 = fireBall;
		this.bulletID = id;
	}
	public Bullet(Enemy target2, ArrayList<Enemy> enemies, int x, int y,
			int strength, int bulletSpeed, Bitmap fireBall, int i) {
		this.targets = enemies;
		this.target = target2;
		posX = x;
		posY = y;
		this.radius = 100;
		this.velocity = bulletSpeed;
		bmp2 = fireBall;
		this.stoppingPower = strength;
		this.bulletID = i;
	}
	public int distance(int x1, int x2, int y1, int y2){
		return (int)Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
	}
	public void update(){
		int speed = (int)Math.sqrt(velocity);
		int targetX = target.x+target.hitCenterX;
		int targetY = target.y+target.hitCenterY;
		float diffX = targetX - posX;
		float diffY = targetY - posY;

		if(!destroyed && hitDetection(targetX-target.radius,targetX+target.radius,targetY-target.radius,targetY+target.radius,
				posX,posX,posY,posY)){
			if(bulletID == 0){
				target.health -= stoppingPower;
				destroyed = true;
			}
			else if(bulletID == 2){
				if(System.currentTimeMillis() - target.lastSlowed > 3000){
					target.lastSlowed = System.currentTimeMillis();
					target.xSpeed /= 2;
					target.ySpeed /= 2;
					target.isSlowed = true;
				}
			}
			else{
				target.health -= stoppingPower;
				for(int i=0;i<targets.size();i++)
					if(distance(targets.get(i),target) < this.radius){
						targets.get(i).health -= stoppingPower;
					}
				destroyed = true;
			}
		}
		else{
			if(diffX < 0){
				if(Math.abs(diffX) < speed)
					posX -= Math.abs(diffX);
				else
					posX -= speed;
			}
			else if(diffX >0){
				if(diffX < speed)
					posX += diffX;
				else
					posX += speed;
			}
			if(diffY < 0){
				if(Math.abs(diffY) < speed)
					posY -= Math.abs(diffY);
				else
					posY-=speed;
			}
			else if(diffY >0){
				if(diffY < speed)
					posY += diffY;
				else
					posY += speed;
			}
			
			if(hitDetection(targetX-target.radius,targetX+target.radius,targetY-target.radius,targetY+target.radius,
					posX-(int)(.1*posX),posX+(int)(.1*posX),posY-(int)(.1*posY),posY+(int)(.1*posY)) && !destroyed){
				target.health -= stoppingPower;
				destroyed = true;
			}
		}
		//ROTATION
		if(diffX == 0){
			if(diffY > 0)
				currentFrame = 0;
			else
				currentFrame = 4;
		}
		else if(diffY == 0){
			if(diffX > 0)
				currentFrame = 2;
			else
				currentFrame = 6;
		}
		else{
			if(diffX < 0 && diffY < 0)
				currentFrame = 1;
			else if(diffX > 0 && diffY >0)
				currentFrame = 3;
			else if(diffX>0 && diffY <0)
				currentFrame = 7;
			else
				currentFrame = 5;
		}
	}
	private int distance(Enemy e1, Enemy e2) {
		return (int) Math.sqrt((e2.x-e1.x)*(e2.x-e1.x)+(e2.y-e1.y)*(e2.y-e1.y));
	}
	public void onDraw(Canvas canvas){
		//update();
		//canvas.drawCircle(posX,posY,10, paint);
		if(bulletID == 0)
			canvas.drawBitmap(bmp[currentFrame], posX,posY, null);
		else if(bulletID == 1 || bulletID ==2)
			canvas.drawBitmap(bmp2,posX,posY,null);
		}
	public long getLifeSpane(){
		return (long) (System.currentTimeMillis() - birth);
	}
	static boolean hitDetection(int minEnemyX, int maxEnemyX, int minEnemyY, int maxEnemyY,
			int minBulletX, int maxBulletX, int minBulletY, int maxBulletY){
		return ((minBulletX <= maxEnemyX && maxEnemyX <= maxBulletX)|| (minEnemyX <= maxBulletX && maxBulletX <= maxEnemyX))
				&&((minBulletY <= maxEnemyY && maxEnemyY <= maxBulletY)|| (minEnemyY <= maxBulletY && maxBulletY <= maxEnemyY));
	}
	public void speedUP() {
		velocity *= 2;
	}
	public void slowDown() {
		velocity /= 2;
	}
}
