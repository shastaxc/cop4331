package cop4331.cloud9001.bentd;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bullet {
	protected Enemy target;
	protected int posX;
	protected int posY;
	protected int radius;
	protected int stoppingPower;
	protected int velocity;
	protected boolean destroyed = false;
	protected Paint paint;
	private Bitmap bmp[];
	private int currentFrame = 0;
	private long birth = System.currentTimeMillis();
	
	public Bullet(Enemy e, int x, int y, int strength, int bulletSpeed, Bitmap[] bmpArr) {
		bmp = bmpArr;
		target = e;
		posX = x;
		posY = y;
		stoppingPower = strength;
		velocity = bulletSpeed;
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
			target.health -= stoppingPower;
			destroyed = true;
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
					posX,posX,posY,posY) && !destroyed){
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
	public void onDraw(Canvas canvas){
		//update();
		//canvas.drawCircle(posX,posY,10, paint);
		canvas.drawBitmap(bmp[currentFrame], posX,posY, null);
	}
	public long getLifeSpane(){
		return (long) (System.currentTimeMillis() - birth);
	}
	private boolean hitDetection(int minEnemyX, int maxEnemyX, int minEnemyY, int maxEnemyY,
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
