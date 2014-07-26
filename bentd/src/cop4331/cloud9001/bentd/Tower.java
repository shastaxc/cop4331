package cop4331.cloud9001.bentd;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Tower {
	private int x = 0;
	private int y = 0;
	@SuppressWarnings("unused")
	private GameView gameView;
    private Bitmap bmp;
    //private int height;
    //private int width;
    
    public Tower(Bitmap bmp, int x, int y) {
        this.bmp=bmp;
        this.x = x;
        this.y = y;
        //height = bmp.getHeight();
        //width = bmp.getWidth();
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
        //update();
        //int srcX = bmp.getWidth();//width;
        //int srcY = bmp.getHeight();//height;
        //Rect src = new Rect(srcX, srcY, srcX + width, srcY+height);
        //Rect dst = new Rect(x,y,x+width, y+height);
        canvas.drawBitmap(bmp, x,y, null);
  }
}
