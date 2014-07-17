package cop4331.cloud9001.bentd;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class MapView extends TileView{

	private int game_state = READY; //State of the game
    public static final int PAUSE = 0; //Index to reference game state
    public static final int READY = 1; //Index to reference game state
    public static final int RUNNING = 2; //Index to reference game state
    public static final int DEFEAT = 3; //Index to reference game state
    public static final int VICTORY = 4; //Index to reference game state
    
    private static TextView event_text; //Display various messages to user
    
    private static final int TOP_EDGE = 1; //Index to reference drawable
    private static final int BOTTOM_EDGE = 2; //Index to reference drawable
    private static final int LEFT_EDGE = 3; //Index to reference drawable
    private static final int RIGHT_EDGE = 4; //Index to reference drawable
    private static final int TOP_LEFT_CORNER = 5; //Index to reference drawable
    private static final int TOP_RIGHT_CORNER = 6; //Index to reference drawable
    private static final int BOTTOM_LEFT_CORNER = 7; //Index to reference drawable
    private static final int BOTTOM_RIGHT_CORNER = 8; //Index to reference drawable
    private static final int TOP_LEFT_INSIDE_CORNER = 9; //Index to reference drawable
    private static final int TOP_RIGHT_INSIDE_CORNER = 10; //Index to reference drawable
    private static final int BOTTOM_LEFT_INSIDE_CORNER = 11; //Index to reference drawable
    private static final int BOTTOM_RIGHT_INSIDE_CORNER = 12; //Index to reference drawable
    private static final int NO_EDGE = 13; //Index to reference drawable
	
	//Various constructors using standard View constructor syntax
    public MapView(Context context) {
        super(context);
        initializeMap();
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeMap();
    }

    public MapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeMap();
    }
	
	public void initializeMap(){
		setFocusable(true);
		Resources r = this.getContext().getResources();
		
		createImgHolder(14);
		//Index 0 = nothing
		loadTile(TOP_EDGE, r.getDrawable(cop4331.cloud9001.bentd.R.drawable.path_t_edge)); //Index 1
		loadTile(BOTTOM_EDGE, r.getDrawable(cop4331.cloud9001.bentd.R.drawable.path_b_edge)); //Index 2
		loadTile(LEFT_EDGE, r.getDrawable(cop4331.cloud9001.bentd.R.drawable.path_l_edge)); //Index 3
		loadTile(RIGHT_EDGE, r.getDrawable(cop4331.cloud9001.bentd.R.drawable.path_r_edge)); //Index 4
		loadTile(TOP_LEFT_CORNER, r.getDrawable(cop4331.cloud9001.bentd.R.drawable.path_tl_corner)); //Index 5
		loadTile(TOP_RIGHT_CORNER, r.getDrawable(cop4331.cloud9001.bentd.R.drawable.path_tr_corner)); //Index 6
		loadTile(BOTTOM_LEFT_CORNER, r.getDrawable(cop4331.cloud9001.bentd.R.drawable.path_bl_corner)); //Index 7
		loadTile(BOTTOM_RIGHT_CORNER, r.getDrawable(cop4331.cloud9001.bentd.R.drawable.path_br_corner)); //Index 8
		loadTile(TOP_LEFT_INSIDE_CORNER, r.getDrawable(cop4331.cloud9001.bentd.R.drawable.path_tl_inside_corner)); //Index 9
		loadTile(TOP_RIGHT_INSIDE_CORNER, r.getDrawable(cop4331.cloud9001.bentd.R.drawable.path_tr_inside_corner)); //Index 10
		loadTile(BOTTOM_LEFT_INSIDE_CORNER, r.getDrawable(cop4331.cloud9001.bentd.R.drawable.path_bl_inside_corner)); //Index 11
		loadTile(BOTTOM_RIGHT_INSIDE_CORNER, r.getDrawable(cop4331.cloud9001.bentd.R.drawable.path_br_inside_corner)); //Index 12
		loadTile(NO_EDGE, r.getDrawable(cop4331.cloud9001.bentd.R.drawable.path_no_edge)); //Index 13
	}
	
	
     //Save game state so that the user does not lose anything
     //if the game process is killed
    public Bundle saveState() {
        Bundle map = new Bundle();

        //map.putIntArray("mAppleList", coordArrayListToArray(mAppleList));
        //map.putInt("mDirection", Integer.valueOf(mDirection));
        //map.putInt("mNextDirection", Integer.valueOf(mNextDirection));
        //map.putLong("mMoveDelay", Long.valueOf(mMoveDelay));
        //map.putLong("mScore", Long.valueOf(mScore));
        //map.putIntArray("mSnakeTrail", coordArrayListToArray(mSnakeTrail));

        return map;
    }
    
    //Restores game state that was saved
    public void restoreState(Bundle capsule) {
        setMode(PAUSE);

        //mAppleList = coordArrayToArrayList(capsule.getIntArray("mAppleList"));
        //mDirection = capsule.getInt("mDirection");
        //mNextDirection = capsule.getInt("mNextDirection");
        //mMoveDelay = capsule.getLong("mMoveDelay");
        //mScore = capsule.getLong("mScore");
        //mSnakeTrail = coordArrayToArrayList(capsule.getIntArray("mSnakeTrail"));
    }
    
    public void setEventText(TextView newView) {
        event_text = newView;
    }
    
    public void setMode(int new_mode) {
        int previous_mode = game_state;
        game_state = new_mode;

        if (new_mode == RUNNING & previous_mode != RUNNING) {
            event_text.setVisibility(View.INVISIBLE);
            return;
        }

        Resources r = getContext().getResources();
        CharSequence str = "";
        if (new_mode == PAUSE) {
            str = r.getText(cop4331.cloud9001.bentd.R.string.mode_pause); //"Paused"
        }
        if (new_mode == READY) {
            str = r.getText(cop4331.cloud9001.bentd.R.string.mode_ready); //"Tap screen to begin"
        }
        if (new_mode == DEFEAT) {
            str = r.getString(cop4331.cloud9001.bentd.R.string.mode_defeat); //"Defeat"
        }
        if (new_mode == VICTORY) {
            str = r.getString(cop4331.cloud9001.bentd.R.string.mode_victory); //"Victory"
        }

        event_text.setText(str);
        event_text.setVisibility(View.VISIBLE);
    }
    
    public void setMapGrid(int map_number){
    	map_grid = new int[Y_TILE_COUNT][X_TILE_COUNT];
    	for (int y = 0; y < Y_TILE_COUNT; y++) {
            for (int x = 0; x < X_TILE_COUNT; x++) {
                map_grid[y][x] = MapConfig.getMap(0)[y][x];
            }
        }
        MapView.this.invalidate();
        setMode(RUNNING);
    }
}
