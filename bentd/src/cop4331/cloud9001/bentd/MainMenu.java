package cop4331.cloud9001.bentd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity{
	protected static Context app_context;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		app_context = getApplicationContext();

		Button resume_btn = (Button)findViewById(R.id.resume_btn);
		resume_btn.setOnClickListener(globalOnClickListener);
		
		Button new_game_btn = (Button)findViewById(R.id.new_game_btn);
		new_game_btn.setOnClickListener(globalOnClickListener);
	
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
    			case R.id.new_game_btn:
    				newGameBtnClick();
    				break;
    			case R.id.high_scores_btn:
    				viewHighScores();
    				break;
    		}
        }
    };
    
    private void resumeBtnClick(){
		Intent intent = new Intent(this, cop4331.cloud9001.bentd.GameInstance.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    startActivity(intent);
    }

    private void newGameBtnClick(){
		Intent intent = new Intent(this, cop4331.cloud9001.bentd.GameInstance.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
    
    private static ArrayList<Score> sortHighScores(ArrayList<Score> high_scores, Score your_score){
    	if(high_scores.size() == 0 && your_score != null){
    		high_scores.add(your_score);
    		return high_scores;
    	}
    	if(your_score != null){
    		high_scores.add(your_score);
    	}
    	if(high_scores.size() > 1){
    		for(int i = 0; i < high_scores.size(); i++){
        		for(int j = 0; j < high_scores.size()-1; j++){
        	    	//Sort highest to lowest
        			if(high_scores.get(j).getScore().compareTo(high_scores.get(j+1).getScore()) < 0){
        				Score temp = high_scores.get(j);
        				high_scores.remove(j);
        				high_scores.add(j+1, temp);
        			}
        		}
        	}
    	}
    	if(high_scores.size() < 20){
    		return high_scores;
    	}
        return (ArrayList<Score>)high_scores.subList(0, 19);
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
    
    protected static void writeSaveData(){
    	try{

			File f = app_context.getFileStreamPath("save_data.txt");
        	PrintWriter pw = new PrintWriter(f);
        	
                //pw.println(//printstuff);
        	
        	pw.close();
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