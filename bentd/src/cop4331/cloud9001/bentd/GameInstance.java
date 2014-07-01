package cop4331.cloud9001.bentd;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GameInstance extends Activity {

	private MapView basic_map_view;
	//Used to restore saved game
    private static String CAPSULE_KEY = "map-view";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_instance);
		
		//Load map
		basic_map_view = (MapView) findViewById(cop4331.cloud9001.bentd.R.id.map);
        basic_map_view.setEventText((TextView) findViewById(cop4331.cloud9001.bentd.R.id.event_textview));
        basic_map_view.setMapGrid(0);
        
        /*
        if (savedInstanceState == null) {
        	MapConfig.createMapGrid(0);
            // No save state, set up new game
        	basic_map_view.initializeMap();
        	basic_map_view.setMode(MapView.READY);
        }
        else {
            //Save state detected, loading previous data
            Bundle map = savedInstanceState.getBundle(CAPSULE_KEY);
            if (map != null) {
            	basic_map_view.restoreState(map);
            } else {
            	basic_map_view.setMode(MapView.PAUSE);
            }
        }
		*/
		//Load Stats UI
        TextView currency_textview = (TextView) findViewById(R.id.currency_textview);
        currency_textview.setText("0000");
        TextView life_textview = (TextView) findViewById(R.id.life_textview);
        life_textview.setText("100");
        TextView wave_textview = (TextView) findViewById(R.id.wave_textview);
        wave_textview.setText("0/8");
        TextView time_remaining_textview = (TextView) findViewById(R.id.time_remaining_textview);
        time_remaining_textview.setText("02:00");
		Button pause_btn = (Button)findViewById(R.id.pause_btn);
		pause_btn.setOnClickListener(global_on_click_listener);
		Button settings_btn = (Button)findViewById(R.id.settings_btn);
		settings_btn.setOnClickListener(global_on_click_listener);
		
	}
	
	//Global on click listener
    final OnClickListener global_on_click_listener = new OnClickListener() {
        public void onClick(final View v) {
    		switch(v.getId()){
    			case R.id.resume_btn:
    				pauseBtnClick();
    				break;
    			case R.id.new_game_btn:
    				settingsBtnClick();
    				break;
    		}
        }
    };
    
    private static void pauseBtnClick(){
    	//game_state = basic_map_view.setMode(MapView.PAUSE);
    }
    
    private static void settingsBtnClick(){
    	
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // Pause the game along with the activity
        basic_map_view.setMode(MapView.PAUSE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Store the game state
        outState.putBundle(CAPSULE_KEY, basic_map_view.saveState());
    }
}
