package cop4331.cloud9001.bentd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity{

    final SwipeDetector swipeDetector = new SwipeDetector();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		Button resume_btn = (Button)findViewById(R.id.resume_btn);
		resume_btn.setOnClickListener(globalOnClickListener);
		
		Button new_game_btn = (Button)findViewById(R.id.new_game_btn);
		new_game_btn.setOnClickListener(globalOnClickListener);
		
		Button settings_btn = (Button)findViewById(R.id.settings_btn);
		settings_btn.setOnClickListener(globalOnClickListener);
	}
	
	//Global on click listener
    final OnClickListener globalOnClickListener = new OnClickListener() {
        public void onClick(final View v) {
        	// TODO Auto-generated method stub
    		switch(v.getId()){
    			case R.id.resume_btn:
    				resumeBtnClick();
    				break;
    			case R.id.new_game_btn:
    				newGameBtnClick();
    				break;
    			case R.id.settings_btn:
    				settingsBtnClick();
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

    private void settingsBtnClick(){
    	
    }
}
