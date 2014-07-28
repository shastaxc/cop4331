package cop4331.cloud9001.bentd;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
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
	//private Handler mHandler = new Handler();
    //private boolean running = true;
	private updater gameUpdater;
	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.activity_game_instance);
		app_context = getApplicationContext();
		fragman = getFragmentManager();

		//Load map
		basic_map_view = (MapView) findViewById(cop4331.cloud9001.bentd.R.id.map);
        basic_map_view.setEventText((TextView) findViewById(cop4331.cloud9001.bentd.R.id.event_textview), (LinearLayout) findViewById(R.id.text_layout));
        basic_map_view.setMapGrid(0);
        basic_map_view.setMode(MapView.READY);
		
		//Load Stats UI
        text_layout = (LinearLayout) findViewById(R.id.text_layout);
		text_layout.setOnClickListener(global_on_click_listener);
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
			currency_textview.setText(text);
		}
	};
	/*private Runnable runnable = new Runnable(){
		public void run(){
			//int currency = game_view.money;
			while(true){
				currency_textview.setText(""+game_view.money);
				try {
					wait(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	};*/
	private class updater extends Thread{
	    private boolean running = false;
	    private long lastUpdate = 0;
	    public void setRunning(boolean run) {
            running = run;
	    }
      	@SuppressLint("WrongCall") 
      	@Override
      	public void run() {
      		//while (running) {
      			if(System.currentTimeMillis() - lastUpdate > 10000){
      				lastUpdate = System.currentTimeMillis();
      				GameInstance.currency_textview.setText(GameView.money);
      			}
      		//}
      		
   		}
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
	private static String timeToString(long timeInMili){
		long minutes = timeInMili/60000;
		long seconds = (timeInMili%60000)/1000;
		if(minutes>10){
			if(seconds>10)
				return ""+minutes+":"+seconds;
			else
				return ""+minutes+":"+"0"+seconds;
		}
		else{
			if(seconds>10)
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
    
    protected static ArrayList<Score> getHighScores(){
		ArrayList<Score> high_scores = new ArrayList<Score>(20);
		try{
			FileInputStream in = app_context.openFileInput("highscores.txt");
		    InputStreamReader inputStreamReader = new InputStreamReader(in);
		    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		    String line;
		    
		    String[] parts = new String[40];
		    while ((line = bufferedReader.readLine()) != null) {
		        parts = line.split(" ");
		    }
		    for(int i = 1; i <= parts.length; i+=2){
		    	high_scores.add(new Score(parts[i], parts[i-1]));
		    }
		}
		catch(IOException e){
			//
		}
	    return high_scores;
	}
    
    protected static void rewriteHighScores(int your_score){
    	
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
    public void onSaveInstanceState(Bundle out_state) {
        //Save fields and timers
    	//Example: 
    	out_state.putInt("score", GameView.money);
    	
    	super.onSaveInstanceState(out_state); //Super constructor saves views
    }
    
    @Override
    public void onRestoreInstanceState(Bundle saved_instance_state){
    	super.onRestoreInstanceState(saved_instance_state); //Super constructor restores views
    	
    	//Now restore saved fields and timers
    	GameView.money = saved_instance_state.getInt("score");
    }
}
