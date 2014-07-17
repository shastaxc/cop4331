package cop4331.cloud9001.bentd;

import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Enemy {
	private int[] DIRECTION_TO_ANIMATION_MAP = {3,1,0,2};
	private static final int BMP_ROWS = 4;
	private static final int BMP_COLUMNS = 3;
	private int x = 0; 
	private int y = 0;
    private int xSpeed = 3;
    private int ySpeed = 3;
    @SuppressWarnings("unused")
	private GameView gameView;
    private Bitmap bmp;
    private Path path;
    
    private int currentFrame = 0;
    private int width;
    private int height;
   
    public Enemy(GameView gameView, Bitmap bmp) {
          this.gameView=gameView;
          this.bmp=bmp;
          width = bmp.getWidth() / BMP_COLUMNS;
          height = bmp.getHeight() / BMP_ROWS;
          path = new Path();
          Random rnd = new Random();
          xSpeed = rnd.nextInt(10);
          ySpeed = rnd.nextInt(10);
    }
    private void update() {
    	if(!path.done()){
    		if(x < path.nextX())
    			x+=xSpeed;
    		if(y < path.nextY())
    			y+=ySpeed;
    		else if(x >= path.nextX())
    			path.nextWaypoint();
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
          int srcY = getAnimationRow() * height;
          Rect src = new Rect(srcX, srcY, srcX + width, srcY+height);
          Rect dst = new Rect(x,y,x+width, y+height);
          canvas.drawBitmap(bmp, src, dst, null);
    }
}
