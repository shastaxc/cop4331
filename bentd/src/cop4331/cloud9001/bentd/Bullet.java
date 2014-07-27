package cop4331.cloud9001.bentd;

import android.graphics.Canvas;
import android.graphics.Color;
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
	public Bullet(Enemy e, int x, int y, int strength, int bulletSpeed) {
		target = e;
		posX = x;
		posY = y;
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
	public void update(){
		int speed = (int)Math.sqrt(velocity);
		int targetX = target.x+target.hitCenterX;
		int targetY = target.y+target.hitCenterY;
		
		if(distance(posX,targetX,posY,targetY)+target.radius+radius < velocity){
			target.health -= stoppingPower;
			destroyed = true;
		}
		else{
			float diffX = targetX - posX;
			float diffY = targetY - posY;
			
			if(diffX < 0)
				posX-=velocity;
			else if(diffX >0)
				posX+=velocity;
			if(diffY <0)
				posY-=velocity;
			else if(diffY >0)
				posY+=velocity;
			
			if(distance(posX,targetX,posY,targetY)+target.radius+radius < velocity){
				target.health -= stoppingPower;
				destroyed = true;
			}
		}
			
		/*
		else{
			int A = posY - target.y;
			int B = posX - target.x;
			int C = distance(posX,target.x,posY,target.y);
			
			posX += B*velocity/C;
			posY += A*velocity/C;
		}*/
	}
	public void onDraw(Canvas canvas){
		update();
		canvas.drawCircle(posX,posY,10, paint);
	}
}
