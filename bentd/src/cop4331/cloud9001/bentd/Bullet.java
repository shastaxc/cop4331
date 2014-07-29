package cop4331.cloud9001.bentd;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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
	private Bitmap bmp;
	private Bitmap rotatedBmp;
	private float rotation = 180;
	private long birth = System.currentTimeMillis();
	
	public Bullet(Enemy e, int x, int y, int strength, int bulletSpeed, Bitmap arrow) {
		bmp = arrow;
		rotatedBmp = RotateBitmap(bmp,rotation);
		target = e;
		posX = x;
		posY = y;
		radius = 0;//arrow.getWidth();
		stoppingPower = strength;
		velocity = bulletSpeed;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(5);
		paint.setStyle(Paint.Style.STROKE);
	}
	public int distance(int x1, int x2, int y1, int y2){
		return (int)Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-1));
	}
	private static Bitmap RotateBitmap(Bitmap source, float angle){
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(),source.getHeight(),matrix,true);
	}
	public void update(){
		int speed = (int)Math.sqrt(velocity);
		int targetX = target.x+target.hitCenterX;
		int targetY = target.y+target.hitCenterY;
		float diffX = targetX - posX;
		float diffY = targetY - posY;
		if(rotation ==0){
			rotation = (float)(Math.acos(diffY/diffX)*(180/Math.PI));
			rotation = rotation/90 *90;
			rotatedBmp = RotateBitmap(bmp,rotation);
		}
		if(distance(posX,targetX,posY+(bmp.getHeight()/2),targetY) < target.radius){
			target.health -= stoppingPower;
			destroyed = true;
		}
		else{
			if(diffX < 0){
				if(diffX < speed)
					posX -= diffX;
				else
					posX-=speed;
			}
			else if(diffX >0){
				if(diffX < speed)
					posX += diffX;
				else
					posX += speed;
			}
			if(diffY < 0){
				if(diffY < speed)
					posY -= diffY;
				else
					posY-=speed;
			}
			else if(diffY >0){
				if(diffY < speed)
					posY += diffY;
				else
					posY += speed;
			}
			if(distance(posX,targetX,posY,targetY) < target.radius){
				target.health -= stoppingPower;
				destroyed = true;
			}
		}
	}
	public void onDraw(Canvas canvas){
		//update();
		//canvas.drawCircle(posX,posY,10, paint);
		canvas.drawBitmap(rotatedBmp, posX,posY, null);
	}
	public long getLifeSpane(){
		return (long) (System.currentTimeMillis() - birth);
	}
}
