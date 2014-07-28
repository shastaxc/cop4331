package cop4331.cloud9001.bentd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
//import android.os.AsyncTask;

public class GameInstance extends Activity {

	protected static Context app_context;
	protected static MapView basic_map_view;
	protected static GameView game_view;
	protected static String CAPSULE_KEY = "map-view"; //Used to restore saved game
	protected static Button pause_btn;
	protected static Button forward_btn;
	protected static TextView currency_textview;
	protected static TextView life_textview;
	protected static TextView wave_textview;
	protected static TextView time_remaining_textview;
	protected static LinearLayout text_layout;
	protected static RelativeLayout stats_bar_layout;
	protected static AlertDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_instance);
		app_context = getApplicationContext();
		
		//Load map
		basic_map_view = (MapView) findViewById(cop4331.cloud9001.bentd.R.id.map);
        basic_map_view.setEventText((TextView) findViewById(cop4331.cloud9001.bentd.R.id.event_textview), (LinearLayout) findViewById(R.id.text_layout));
        basic_map_view.setMapGrid(0);
        basic_map_view.setMode(MapView.READY);
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
        text_layout = (LinearLayout) findViewById(R.id.text_layout);
		text_layout.setOnClickListener(global_on_click_listener);
        stats_bar_layout = (RelativeLayout) findViewById(R.id.stats_bar_layout);
        stats_bar_layout.setOnClickListener(global_on_click_listener);
        currency_textview = (TextView) findViewById(R.id.currency_textview);
        currency_textview.setText("9999");
        life_textview = (TextView) findViewById(R.id.life_textview);
        life_textview.setText("999");
        wave_textview = (TextView) findViewById(R.id.wave_textview);
        wave_textview.setText("9/9");
        time_remaining_textview = (TextView) findViewById(R.id.time_remaining_textview);
        time_remaining_textview.setText("99:99");
		pause_btn = (Button)findViewById(R.id.pause_btn);
		pause_btn.setOnClickListener(global_on_click_listener);
		forward_btn = (Button)findViewById(R.id.fast_forward_btn);
		forward_btn.setOnClickListener(global_on_click_listener);
		
		
		game_view = (GameView) findViewById(cop4331.cloud9001.bentd.R.id.game);
	}
	static Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			String text = (String)msg.obj;
			currency_textview.setText(text.substring(0,4));
			life_textview.setText(text.substring(4,7));
			wave_textview.setText(""+text.substring(7,8)+"/"+text.substring(8,9));
			time_remaining_textview.setText(text.substring(9,text.length()));
		}
	};
	protected static String healthToString(int n){
		if(n>=100)
			return ""+n;
		else if(n>10)
			return "0"+n;
		else
			return "00"+n;
	}
	protected static String currencyToString(int n){
		String str = Integer.toString(n);
		switch(str.length()){
		case 0:
			str = "0000";
			break;
		case 1:
			str = "000"+str;
			break;
		case 2:
			str = "00"+str;
			break;
		case 3:
			str = "0"+str;
			break;
		default:
			break;
		}
			
		return str;
	}
	static String timeToString(long timeInMili){
		long minutes = timeInMili/60000;
		long seconds = (timeInMili%60000)/1000;
		if(minutes>10){
			if(seconds>=10)
				return ""+minutes+":"+seconds;
			else
				return ""+minutes+":"+"0"+seconds;
		}
		else{
			if(seconds>=10)
				return "0"+minutes+":"+seconds;
			else
				return "0"+minutes+":"+"0"+seconds;
		}
	}
	protected static void modifyStatBar(int currency){
        currency_textview.setText(currencyToString(currency));
        currency_textview.postInvalidate();
        //life_textview.setText(Integer.toString(lifeRemaining));
        //wave_textview.setText(""+currWave+"/"+maxWaves+"");
        //time_remaining_textview.setText(timeToString(timeRemaining));
	}
	//Global on click listener
    final OnClickListener global_on_click_listener = new OnClickListener() {
        public void onClick(final View v) {
    		switch(v.getId()){
    			case R.id.pause_btn:
    				pauseBtnClick();
    				break;
    			case R.id.fast_forward_btn:
    				forwardBtnClick();
    				break;
    			case R.id.text_layout:
    				if(basic_map_view.getMode() == MapView.READY){
    					basic_map_view.setMode(MapView.RUNNING);
    				}
    				break;
    			case R.id.stats_bar_layout:
    				if(basic_map_view.getMode() == MapView.READY){
    					basic_map_view.setMode(MapView.RUNNING);
    				}
    				break;
    		}
        }
    };
    
    private void pauseBtnClick(){
    	if(basic_map_view.getMode() == MapView.PAUSE){
    		basic_map_view.setMode(MapView.RUNNING);
        	pause_btn.setBackgroundResource(R.drawable.pause_icon);
    	}
    	else if(basic_map_view.getMode() == MapView.READY){
    		// Button will not function in this mode
    	}
    	else if(basic_map_view.getMode() == MapView.RUNNING){
        	basic_map_view.setMode(MapView.PAUSE);
        	pause_btn.setBackgroundResource(R.drawable.play_icon);
        	createPauseMenu();
    	}
    	else if(basic_map_view.getMode() == MapView.FAST_FORWARD){
    		basic_map_view.setMode(MapView.PAUSE);
        	pause_btn.setBackgroundResource(R.drawable.play_icon);
    		forward_btn.setBackgroundResource(R.drawable.fast_forward_icon);
        	createPauseMenu();
    		
    	}
    	else if(basic_map_view.getMode() == MapView.DEFEAT){
    		// Button will not function in this mode
    	}
    	else if(basic_map_view.getMode() == MapView.VICTORY){
    		// Button will not function in this mode
    	}
    }
    
    private void forwardBtnClick(){
    	if(basic_map_view.getMode() == MapView.PAUSE){
    		// Button will not function in this mode
    	}
    	else if(basic_map_view.getMode() == MapView.READY){
    		basic_map_view.setMode(MapView.FAST_FORWARD);
    		forward_btn.setBackgroundResource(R.drawable.play_icon);
    	}
    	else if(basic_map_view.getMode() == MapView.RUNNING){
    		basic_map_view.setMode(MapView.FAST_FORWARD);
    		forward_btn.setBackgroundResource(R.drawable.play_icon);
    	}
    	else if(basic_map_view.getMode() == MapView.FAST_FORWARD){
    		basic_map_view.setMode(MapView.RUNNING);
    		forward_btn.setBackgroundResource(R.drawable.fast_forward_icon);
    	}
    	else if(basic_map_view.getMode() == MapView.DEFEAT){
    		// Button will not function in this mode
    	}
    	else if(basic_map_view.getMode() == MapView.VICTORY){
    		// Button will not function in this mode
    	}
    }
    private void createPauseMenu(){

    	DialogFragment pause_frag = PauseDialogFragment.newInstance(R.string.mode_pause);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack("pause-menu");
    	pause_frag.show(getFragmentManager(), "dialog");
        
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Store the game state
        outState.putBundle(CAPSULE_KEY, basic_map_view.saveState());
    }

	@Override
	public void onBackPressed(){
		/*if(getFragmentManager().findFragmentByTag("in-game-scoreboard").isVisible()){
			System.out.println("works");
		}*/
		//Also do a pauseBtnClick() call if pausemenu is active
		if(GameView.popup_active){
			GameView.popup_window.dismiss();
    		GameView.popup_active = false;
		}
		else{
			super.onBackPressed();
		}
	}
}
