package cop4331.cloud9001.bentd;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
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
	protected static FragmentManager fragman;
	protected static Context app_context;
	protected static MapView basic_map_view;
	protected static GameView game_view;
	protected static Button pause_btn;
	protected static Button forward_btn;
	protected static TextView currency_textview;
	protected static TextView life_textview;
	protected static TextView wave_textview;
	protected static TextView time_remaining_textview;
	protected static LinearLayout text_layout;
	protected static RelativeLayout stats_bar_layout;
	protected static AlertDialog dialog;
	protected static String SAVE_FILE = "save_data.txt";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_instance);
		app_context = getApplicationContext();
		fragman = getFragmentManager();
		System.out.println("onCreate");
		//Load map
		basic_map_view = (MapView) findViewById(cop4331.cloud9001.bentd.R.id.map);
        basic_map_view.setEventText((TextView) findViewById(cop4331.cloud9001.bentd.R.id.event_textview), (LinearLayout) findViewById(R.id.text_layout));
        basic_map_view.setMapGrid(0);
        basic_map_view.setMode(MapView.READY);
		//Load Stats UI
        text_layout = (LinearLayout) findViewById(R.id.text_layout);
		text_layout.setOnClickListener(global_on_click_listener);
        stats_bar_layout = (RelativeLayout) findViewById(R.id.stats_bar_layout);
        stats_bar_layout.setOnClickListener(global_on_click_listener);
        currency_textview = (TextView) findViewById(R.id.currency_textview);
        currency_textview.setText("0000");
        life_textview = (TextView) findViewById(R.id.life_textview);
        life_textview.setText("100");
        wave_textview = (TextView) findViewById(R.id.wave_textview);
        wave_textview.setText("8/8");
        time_remaining_textview = (TextView) findViewById(R.id.time_remaining_textview);
        time_remaining_textview.setText("02:00");
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
	//Global on click listener
    final OnClickListener global_on_click_listener = new OnClickListener() {
        public void onClick(final View v) {
    		switch(v.getId()){
    			case R.id.pause_btn:
    				pause();
    				break;
    			case R.id.fast_forward_btn:
    				fastForward();
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
    
    protected static void pause(){
    	if(basic_map_view.getMode() == MapView.PAUSED){
    		basic_map_view.setMode(MapView.RUNNING);
        	pause_btn.setBackgroundResource(R.drawable.pause_icon);
        	//game_view.setZOrderOnTop(true);
    	}
    	else if(basic_map_view.getMode() == MapView.READY){
        	//game_view.setZOrderOnTop(false);
    	}
    	else if(basic_map_view.getMode() == MapView.RUNNING){
        	basic_map_view.setMode(MapView.PAUSED);
        	pause_btn.setBackgroundResource(R.drawable.play_icon);
        	//game_view.setZOrderOnTop(false);
        	createPauseMenu();
    	}
    	else if(basic_map_view.getMode() == MapView.FAST_FORWARD){
    		basic_map_view.setMode(MapView.PAUSED);
        	pause_btn.setBackgroundResource(R.drawable.play_icon);
    		forward_btn.setBackgroundResource(R.drawable.fast_forward_icon);
        	//game_view.setZOrderOnTop(true);
        	createPauseMenu();
    		
    	}
    	else if(basic_map_view.getMode() == MapView.DEFEAT){
        	//game_view.setZOrderOnTop(false);
    	}
    	else if(basic_map_view.getMode() == MapView.VICTORY){
        	//game_view.setZOrderOnTop(false);
    	}
    }
    
    protected static void fastForward(){
    	if(basic_map_view.getMode() == MapView.PAUSED){
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
    protected static void createPauseMenu(){
		PauseMenuFragment pause_frag = new PauseMenuFragment();
	    FragmentTransaction fragmentTransaction = fragman.beginTransaction();
	    fragmentTransaction.add(R.id.game_frame,pause_frag, "pause-menu")
	    					.addToBackStack("pause-menu")
	     					.commit();
    }


    protected static void writeSaveData(){
    	try{

			File f = app_context.getFileStreamPath("save_data.txt");
        	PrintWriter pw = new PrintWriter(f);
        	
            pw.println(basic_map_view);

        	/*protected static FragmentManager fragman;
        	protected static Context app_context;
        	protected static MapView basic_map_view;
        	protected static GameView game_view;
        	protected static Button pause_btn;
        	protected static Button forward_btn;
        	protected static TextView currency_textview;
        	protected static TextView life_textview;
        	protected static TextView wave_textview;
        	protected static TextView time_remaining_textview;
        	protected static LinearLayout text_layout;
        	protected static RelativeLayout stats_bar_layout;
        	protected static AlertDialog dialog;*/
        	
        	pw.close();
    	}
    	catch(IOException e){
    		
    	}
    }
	
	protected void loadPreferences(){
		
	}
	
	@Override
	public void onBackPressed(){
		if(fragman.findFragmentByTag("in-game-scoreboard") != null){
			if(fragman.findFragmentByTag("in-game-scoreboard").isVisible()){
				fragman.popBackStack("in-game-scoreboard", FragmentManager.POP_BACK_STACK_INCLUSIVE);
			}
		}
		else if(fragman.findFragmentByTag("pause-menu") != null){
			if(fragman.findFragmentByTag("pause-menu").isVisible()){
				fragman.popBackStack("pause-menu", FragmentManager.POP_BACK_STACK_INCLUSIVE);
				pause();
			}
		}
		else if(GameView.popup_active){
			GameView.popup_window.dismiss();
    		GameView.popup_active = false;
		}
    	else if(basic_map_view.getMode() == MapView.READY){
    		basic_map_view.setMode(MapView.RUNNING);
    	}
		else{
			super.onBackPressed();
        	basic_map_view.setMode(MapView.PAUSED);
        	pause_btn.setBackgroundResource(R.drawable.play_icon);
        	createPauseMenu();
		}
	}
	
	@Override
	public void onPause(){
		super.onPause(); //Super constructor saves views
		System.out.println("onPause");
		writeSaveData();
    	
        //Save fields and timers
    	//out_state.putInt("currency", game_view.getMoney());
    	/*out_state.putInt("score", GameView.score);
    	out_state.putInt("health", GameView.health);
    	out_state.putInt("currentWave", GameView.currentWave);
    	out_state.putInt("maxWaves", GameView.maxWaves);*/
	}
	
	@Override
	public void onResume(){
		super.onResume(); //Super constructor restores views
		System.out.println("onResume");
    	
    	//Now restore saved fields and timers
    	//pause(); //Don't need to know previous mode because will automatically resume as paused
    	//game_view.money = saved_instance_state.getInt("currency");
    	/*GameView.score = saved_instance_state.getInt("score");
    	GameView.health = saved_instance_state.getInt("health");
    	GameView.currentWave = saved_instance_state.getInt("currentWave");
    	GameView.maxWaves = saved_instance_state.getInt("maxWaves");*/
        //Restore towers
        //Restore enemies
	}
	
    @Override
    public void onSaveInstanceState(Bundle out_state) {
    	super.onSaveInstanceState(out_state);
		System.out.println("onSaveInstanceState");
    }
    
    @Override
    public void onRestoreInstanceState(Bundle saved_instance_state){
    	super.onRestoreInstanceState(saved_instance_state);
		System.out.println("onRestoreInstanceState");
    }
}
