package cop4331.cloud9001.bentd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends Activity{
	protected static Context app_context;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		app_context = getApplicationContext();
		
		TextView title_text = (TextView)findViewById(R.id.title_text);
		Typeface face=Typeface.createFromAsset(getAssets(), "fonts/banzai.ttf");

		title_text.setTypeface(face);

		Button resume_btn = (Button)findViewById(R.id.resume_btn);
		resume_btn.setOnClickListener(globalOnClickListener);
		resume_btn.setVisibility(View.GONE);
		
		Button normal_game_btn = (Button)findViewById(R.id.normal_game_btn);
		normal_game_btn.setOnClickListener(globalOnClickListener);

		Button endless_btn = (Button)findViewById(R.id.endless_btn);
		endless_btn.setOnClickListener(globalOnClickListener);
	
		Button high_scores_btn = (Button)findViewById(R.id.high_scores_btn);
		high_scores_btn.setOnClickListener(globalOnClickListener);
	}
	
	//Global on click listener
    final OnClickListener globalOnClickListener = new OnClickListener() {
        public void onClick(final View v) {
    		switch(v.getId()){
    			case R.id.resume_btn:
    				resumeBtnClick();
    				break;
    			case R.id.normal_game_btn:
    				newGameBtnClick();
    				break;
    			case R.id.endless_btn:
    				endlessBtnClick();
    				break;
    			case R.id.high_scores_btn:
    				viewHighScores();
    				break;
    		}
        }
    };
    
    private void resumeBtnClick(){
		Intent intent = new Intent(MainMenu.this, GameInstance.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    startActivity(intent);
    }

    private void newGameBtnClick(){
    	Bundle b = new Bundle();
    	b.putBoolean("endless",  false);
		Intent intent = new Intent(MainMenu.this, GameInstance.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("android.intent.extra.INTENT", b);
	    startActivity(intent);
    }

    private void endlessBtnClick(){
    	Bundle b = new Bundle();
    	b.putBoolean("endless",  true);
		Intent intent = new Intent(MainMenu.this, GameInstance.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("android.intent.extra.INTENT", b);
	    startActivity(intent);
    }
    
    private void viewHighScores(){
    	ScoreBoardFragment score_frag = new ScoreBoardFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_frame,score_frag)
        					.addToBackStack("main-menu-scoreboard")
        					.commit();
    }
    
	protected static ArrayList<Score> getHighScores(){
		ArrayList<Score> high_scores = new ArrayList<Score>();
    	try{
			File f = app_context.getFileStreamPath("high_scores.txt");
			if(!f.exists()){
				f.createNewFile();
				return high_scores;
			}

	    	FileInputStream fis = app_context.openFileInput("high_scores.txt");
			
			BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(fis));
			String line = buffered_reader.readLine();
			    
			String[] parts = new String[2];
			
			while (line != null) {
			    parts = line.split(" ");
			    line = buffered_reader.readLine();
				high_scores.add(new Score(parts[0], parts[1]));
			}

			buffered_reader.close();
			fis.close();
		}
		catch(IOException e){
			
		}
	    return high_scores;
	}
    
    protected static void writeNewHighScore(Score your_score){
    	try{
    		ArrayList<Score> high_scores = getHighScores();
        	high_scores = sortHighScores(high_scores, your_score);
			File f = app_context.getFileStreamPath("high_scores.txt");

        	PrintWriter pw = new PrintWriter(f);
        	
        	for(int i = 0; i < high_scores.size(); i++){
                pw.println(high_scores.get(i).getPlayer() + " " + high_scores.get(i).getScore());
        	}
        	
        	pw.close();
    	}
    	catch(IOException e){
    		
    	}
    }
    
    protected static ArrayList<Score> sortHighScores(ArrayList<Score> high_scores, Score your_score){
    	if(your_score != null){
    		high_scores.add(your_score);
    	}
    	if(high_scores.size() <= 1){
    		return high_scores;
    	}
    	if(high_scores.size() > 1){
    		int compare1 = 0;
    		int compare2 = 0;
    		for(int i = 0; i < high_scores.size(); i++){
        		for(int j = 0; j < high_scores.size()-1; j++){
        	    	//Sort highest to lowest
        			compare1 = Integer.parseInt(high_scores.get(j).getScore());
        			compare2 = Integer.parseInt(high_scores.get(j+1).getScore());
        			if(compare1 < compare2){
        				Score temp = high_scores.get(j);
        				high_scores.remove(j);
        				high_scores.add(j+1, temp);
        			}
        		}
        	}
    	}
    	if(high_scores.size() <= 20){
    		return high_scores;
    	}
    	else{
    		for(int i = 20; i <= high_scores.size(); i++){
    			high_scores.remove(i);
    		}
    	}
        return high_scores;
    }
    
    protected static void readSaveData(){
    	try{
			File f = app_context.getFileStreamPath("save_data.txt");
			if(!f.exists()){
				f.createNewFile();
			}
			else{
		    	FileInputStream fis = app_context.openFileInput("save_data.txt");
				
				BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(fis));
				String line = buffered_reader.readLine();
				    
				//Do stuff with line

				buffered_reader.close();
				fis.close();
			}
		}
		catch(IOException e){
			
		}
	}

	@Override
	public void onBackPressed(){
		if(getFragmentManager().findFragmentByTag("main-menu-scoreboard") != null){
			if(getFragmentManager().findFragmentByTag("main-menu-scoreboard").isVisible()){
				getFragmentManager().popBackStack("main-menu-scoreboard", FragmentManager.POP_BACK_STACK_INCLUSIVE);
			}
		}
		else{
			super.onBackPressed();
		}
	}
}