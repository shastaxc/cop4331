package cop4331.cloud9001.bentd;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class PauseMenuFragment extends Fragment{
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View root_view = inflater.inflate(R.layout.pause_menu_layout, container, false);
		
		Button resume_from_pause = (Button) root_view.findViewById(R.id.resume_from_pause); //Instantiate button
		Button high_score_from_pause = (Button) root_view.findViewById(R.id.high_score_from_pause); //Instantiate button
		resume_from_pause.setOnClickListener(pause_on_click_listener); //Set onclicklistener
		high_score_from_pause.setOnClickListener(pause_on_click_listener); //Set onclicklistener
		
        return root_view;
    }

    OnClickListener pause_on_click_listener = new OnClickListener() {
        public void onClick(final View v) {
    		switch(v.getId()){
    			case R.id.resume_from_pause:
    	    		GameInstance.pause();
    	    		getFragmentManager().popBackStack("pause-menu", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    				break;
    			case R.id.high_score_from_pause:
    				ScoreBoardFragment score_frag = new ScoreBoardFragment();
    			    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    			    fragmentTransaction.add(R.id.game_frame,score_frag, "in-game-scoreboard")
    			    					.addToBackStack("in-game-scoreboard")
    			     					.commit();
    				break;
    		}
        }
    };
}