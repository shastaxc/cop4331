package cop4331.cloud9001.bentd;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
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
	protected static ImageView infinity_icon;
	protected static LinearLayout text_layout;
	protected static RelativeLayout stats_bar_layout;
	protected static boolean endless = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_instance);
		app_context = getApplicationContext();
		fragman = getFragmentManager();
		System.out.println("onCreate");
		
		Bundle b = getIntent().getBundleExtra("android.intent.extra.INTENT");
		endless = b.getBoolean("endless");
		
		if(endless){
			//Load map
			basic_map_view = (MapView) findViewById(cop4331.cloud9001.bentd.R.id.map);
	        basic_map_view.setEventText((TextView) findViewById(cop4331.cloud9001.bentd.R.id.event_textview), (LinearLayout) findViewById(R.id.text_layout));
	        basic_map_view.setMapGrid(1);
	        basic_map_view.setMode(MapView.READY);
			//Load Stats UI
	        text_layout = (LinearLayout) findViewById(R.id.text_layout);
			text_layout.setOnClickListener(global_on_click_listener);
	        currency_textview = (TextView) findViewById(R.id.currency_textview);
	        currency_textview.setText("0000");
	        life_textview = (TextView) findViewById(R.id.life_textview);
	        life_textview.setText("100");
	        wave_textview = (TextView) findViewById(R.id.wave_textview);
	        wave_textview.setText("000");
	        time_remaining_textview = (TextView) findViewById(R.id.time_remaining_textview);
	        time_remaining_textview.setText("INF");
	        time_remaining_textview.setVisibility(View.GONE);
	        infinity_icon = (ImageView) findViewById(R.id.infinity_icon);
	        infinity_icon.setVisibility(View.VISIBLE);
			pause_btn = (Button)findViewById(R.id.pause_btn);
			pause_btn.setOnClickListener(global_on_click_listener);
			forward_btn = (Button)findViewById(R.id.fast_forward_btn);
			forward_btn.setOnClickListener(global_on_click_listener);
			
			game_view = (GameView) findViewById(R.id.game);
		}
		else{
			//Load map
			basic_map_view = (MapView) findViewById(R.id.map);
	        basic_map_view.setEventText((TextView) findViewById(R.id.event_textview), (LinearLayout) findViewById(R.id.text_layout));
	        basic_map_view.setMapGrid(0);
	        basic_map_view.setMode(MapView.READY);
			//Load Stats UI
	        text_layout = (LinearLayout) findViewById(R.id.text_layout);
			text_layout.setOnClickListener(global_on_click_listener);
	        currency_textview = (TextView) findViewById(R.id.currency_textview);
	        currency_textview.setText("0000");
	        life_textview = (TextView) findViewById(R.id.life_textview);
	        life_textview.setText("100");
	        wave_textview = (TextView) findViewById(R.id.wave_textview);
	        wave_textview.setText("0/8");
	        time_remaining_textview = (TextView) findViewById(R.id.time_remaining_textview);
	        time_remaining_textview.setText("02:00");
	        time_remaining_textview.setVisibility(View.VISIBLE);
	        infinity_icon = (ImageView) findViewById(R.id.infinity_icon);
	        infinity_icon.setVisibility(View.GONE);
			pause_btn = (Button)findViewById(R.id.pause_btn);
			pause_btn.setOnClickListener(global_on_click_listener);
			forward_btn = (Button)findViewById(R.id.fast_forward_btn);
			forward_btn.setOnClickListener(global_on_click_listener);
			
			game_view = (GameView) findViewById(R.id.game);
		}
	}
	static Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			String text = (String)msg.obj;
			currency_textview.setText(text.substring(0,4));
			life_textview.setText(text.substring(4,7));
			if(endless){
				wave_textview.setText(""+text.substring(7,text.length()));
			}
			else{
				wave_textview.setText(""+text.substring(7,8)+"/"+text.substring(8,9));
				time_remaining_textview.setText(text.substring(9,text.length()));
			}
		}
	};
	static Handler ffPress = new Handler(){
		@Override
		public void handleMessage(Message msg){
			fastForward();
		}
};
		static Handler endHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			String text = (String)msg.obj;
			if(text.compareTo("VICTORY") == 0)
				basic_map_view.setMode(MapView.VICTORY);
			else if(text.compareTo("DEFEAT")==0)
				basic_map_view.setMode(MapView.DEFEAT);
			Log.i("why","crashing");
		}
	};
	protected static String currentWaveToString(int n){
		if(endless){
			if(n >= 1000)
				return "999";
			else if(n>=100)
				return ""+n;
			else if(n>=10)
				return "0"+n;
			else
				return "00"+n;
		}
		else{
			return ""+n;
		}
	}
	protected static String healthToString(int n){
		if(n>=100)
			return ""+n;
		else if(n>10)
			return "0"+n;
		else if(n >= 0)
			return "00"+n;
		else
			return "000";
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
		if(GameInstance.basic_map_view.getMode() == MapView.DEFEAT || 
				GameInstance.basic_map_view.getMode() == MapView.VICTORY || minutes > 99){
			return "00:00";
		}
		if(minutes>10){
			if(seconds>=10)
				return ""+minutes+":"+seconds;
			else
				return ""+minutes+":"+"0"+seconds;
		}
		else if(minutes >= 0){
			if(seconds>=10)
				return "0"+minutes+":"+seconds;
			else
				return "0"+minutes+":"+"0"+seconds;
		}
		else{
			return timeToString(game_view.level.timePerWave);
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
    				else if(basic_map_view.getMode() == MapView.DEFEAT || basic_map_view.getMode() == MapView.VICTORY){
    					finish();
    				}
    				break;
    		}
        }
    };
    
    protected static void pause(){
    	if(basic_map_view.getMode() == MapView.PAUSED){
    		basic_map_view.setMode(MapView.RUNNING);
        	pause_btn.setBackgroundResource(R.drawable.pause_icon);
    	}
    	else if(basic_map_view.getMode() == MapView.READY){
    		// Button will not function in this mode
    	}
    	else if(basic_map_view.getMode() == MapView.RUNNING){
        	basic_map_view.setMode(MapView.PAUSED);
        	pause_btn.setBackgroundResource(R.drawable.play_icon);
        	createPauseMenu();
    	}
    	else if(basic_map_view.getMode() == MapView.FAST_FORWARD){
    		basic_map_view.setMode(MapView.PAUSED);
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
    
    protected static void fastForward(){
    	if(basic_map_view.getMode() == MapView.PAUSED){
    		// Button will not function in this mode
    	}
    	else if(basic_map_view.getMode() == MapView.READY){
    		basic_map_view.setMode(MapView.FAST_FORWARD);
    		forward_btn.setBackgroundResource(R.drawable.play_icon);
    	}
    	else if(basic_map_view.getMode() == MapView.RUNNING){
    		if(GameView.Enemies.size() == 0){
    			//Do not change mode in this case, just advance to next wave
				GameInstance.game_view.startOfWaveInMiliseconds = 0;
        		forward_btn.setBackgroundResource(R.drawable.fast_forward_icon);
    		}
    		else{
        		basic_map_view.setMode(MapView.FAST_FORWARD);
        		forward_btn.setBackgroundResource(R.drawable.play_icon);
    		}
    	}
    	else if(basic_map_view.getMode() == MapView.FAST_FORWARD){ //If fastfoward button pressed and already in ff mode
    		basic_map_view.setMode(MapView.RUNNING);
    		if(GameView.Enemies.size() == 0){
        		forward_btn.setBackgroundResource(R.drawable.spawn_icon); //Change icon to spawn_wave icon
    		}
    		else{
        		forward_btn.setBackgroundResource(R.drawable.fast_forward_icon);
    		}
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
		}
	}
	protected void savePreferences(){
		
	}
	
	protected void loadPreferences(){
		
	}
	
	@Override
	public void onPause(){
		super.onPause(); //Super constructor saves views
		System.out.println("onPause");
    	game_view.gameLoopThread.setRunning(false);
    	basic_map_view.setMode(MapView.PAUSED);
    	pause_btn.setBackgroundResource(R.drawable.play_icon);
    	/*try {
			game_view.gameLoopThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		//writeSaveData();

	}
	
	@Override
	public void onResume(){
		super.onResume(); //Super constructor restores views
		System.out.println("onResume");
		if(basic_map_view != null)
			if(basic_map_view.getMode() != MapView.READY)
				createPauseMenu();
	}
}
