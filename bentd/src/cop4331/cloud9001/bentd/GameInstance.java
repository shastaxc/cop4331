package cop4331.cloud9001.bentd;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameInstance extends Activity {
	protected static String game_state;
	private Toast pause_toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_instance);
		
        TextView currency_textview = (TextView) findViewById(R.id.currency_textview);
        currency_textview.setText("0000");
        
        TextView wave_textview = (TextView) findViewById(R.id.wave_textview);
        wave_textview.setText("0/8");
        
        TextView time_remaining_textview = (TextView) findViewById(R.id.time_remaining_textview);
        time_remaining_textview.setText("02:00");

		Button pause_btn = (Button)findViewById(R.id.pause_btn);
		pause_btn.setOnClickListener(globalOnClickListener);

		Button settings_btn = (Button)findViewById(R.id.settings_btn);
		settings_btn.setOnClickListener(globalOnClickListener);
		
        pause_toast = new Toast(getApplicationContext());
		
		game_state = "active";
	}
	
	//Global on click listener
    final OnClickListener globalOnClickListener = new OnClickListener() {
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
    
    //TODO
    private static void pauseBtnClick(){
    	game_state = "paused";
		//=============================================
		//Displays toast with pause text
		
		LayoutInflater inflater = getLayoutInflater();
		//Assign the custom layout to view
		//Parameter 1 - Custom layout XML
		//Parameter 2 - Custom layout ID present in linearlayout tag of XML
		View layout = inflater.inflate(R.layout.toast_custom_layout,
		            (ViewGroup) findViewById(R.id.toast_layout));
		//Return the application context
		TextView text = (TextView) layout.findViewById(R.id.toast_textview);
		text.setText("Paused");
		pause_toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		pause_toast.setDuration(Toast.LENGTH_SHORT);
		//Set the custom layout to Toast
		pause_toast.setView(layout);
		//Display toast
		pause_toast.show();
    }
    
    private static void settingsBtnClick(){
    	
    }
}
