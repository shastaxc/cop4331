package cop4331.cloud9001.bentd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class TileView extends View{
	/**
     * Parameters controlling the size of the tiles and their range within view.
     * Width/Height are in pixels, and Drawables will be scaled to fit to these
     * dimensions. X/Y Tile Counts are the number of tiles that will be drawn.
     */

	protected static DisplayMetrics display_metrics;
	protected static final int Y_TILE_COUNT = 7; //Assuming horizontal orientation
	protected static final int X_TILE_COUNT = 10; //Assuming horizontal orientation
    protected int tile_width;
    protected int tile_height;

    protected static int x_offset;
    protected static int y_offset;

    final Context context;

    /**
     * A hash that maps integer handles specified by the subclasser to the
     * drawable that will be used for that reference
     */
    private Bitmap[] tile_imgs; 

    /**
     * A two-dimensional array of integers in which the number represents the
     * index of the tile that should be drawn at that locations
     */
    public static int[][] map_grid = new int[Y_TILE_COUNT][X_TILE_COUNT];

    private final Paint paintbrush = new Paint();

    //Various constructors using standard View constructor syntax
    public TileView(Context context) {
        super(context);
        this.context = context;
		//Initialize dimension variables
        display_metrics = context.getResources().getDisplayMetrics();
        this.tile_height = (int) Math.floor(display_metrics.heightPixels) / Y_TILE_COUNT;
        this.tile_width = (int) Math.floor(display_metrics.widthPixels) / X_TILE_COUNT;
    }

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
		//Initialize dimension variables
		display_metrics = context.getResources().getDisplayMetrics();
		this.tile_height = (int) Math.floor(display_metrics.heightPixels) / Y_TILE_COUNT;
		this.tile_width = (int) Math.floor(display_metrics.widthPixels) / X_TILE_COUNT;
    }

    public TileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
		//Initialize dimension variables
		display_metrics = context.getResources().getDisplayMetrics();
		this.tile_height = (int) Math.floor(display_metrics.heightPixels) / Y_TILE_COUNT;
		this.tile_width = (int) Math.floor(display_metrics.widthPixels) / X_TILE_COUNT;
    }
    
    /**
     * Rests the internal array of Bitmaps used for drawing tiles, and
     * sets the maximum index of tiles to be inserted
     */
    public void createImgHolder(int tilecount) {
    	tile_imgs = new Bitmap[tilecount];
    }

    /**
     * Function to set the specified Drawable as the tile for a particular
     * integer key.
     */
    public void loadTile(int key, Drawable tile) {
        Bitmap bitmap = Bitmap.createBitmap(tile_width, tile_height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, tile_width, tile_height);
        tile.draw(canvas);
        
        tile_imgs[key] = bitmap;
    }

    //Resets all tiles to 0 (empty)
    public void clearTiles() {
        for (int y = 0; y < Y_TILE_COUNT; y++) {
            for (int x = 0; x < X_TILE_COUNT; x++) {
                map_grid[y][x] = 0;
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int y = 0; y < Y_TILE_COUNT; y++) {
            for (int x = 0; x < X_TILE_COUNT; x++) {
                if (map_grid[y][x] >= 0) {
                    canvas.drawBitmap(tile_imgs[map_grid[y][x]], 
                    		x_offset + x * tile_width,
                    		y_offset + y * tile_height,
                    		paintbrush);
                }
            }
        }

    }

}