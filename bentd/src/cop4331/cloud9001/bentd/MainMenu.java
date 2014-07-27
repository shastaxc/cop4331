package cop4331.cloud9001.bentd;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity{
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

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
    	
    }

    private void newGameBtnClick(){
		Intent intent = new Intent(this, cop4331.cloud9001.bentd.GameInstance.class);
	    startActivity(intent);
    }
    
    private void viewHighScores(){
    	ScoreBoardFragment score_frag = new ScoreBoardFragment();
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_frame,score_frag)
        					.addToBackStack("Main Menu Scoreboard")
        					.commit();
    }
}
