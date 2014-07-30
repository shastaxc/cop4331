package cop4331.cloud9001.bentd;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ScoreBoardFragment extends Fragment{

	private ArrayList<Score> display_list = new ArrayList<Score>(20);
	protected ScoreListAdapter adapter_list;
	protected ListView list;
	TextView current_score;
	TextView current_score_label;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View root_view = inflater.inflate(R.layout.score_board_layout, container, false);
		
		current_score = (TextView) root_view.findViewById(R.id.current_score);
		current_score_label = (TextView) root_view.findViewById(R.id.current_score_label);


		//MainMenu.writeNewHighScore(new Score("YOU", "9001"));

		display_list = MainMenu.getHighScores();
		display_list = MainMenu.sortHighScores(display_list, null);

		/*display_list.add(new Score("NNN", "3000"));
		display_list.add(new Score("WVA", "5816"));
		display_list.add(new Score("GRI", "1873"));
		display_list.add(new Score("VES", "8486"));
		display_list.add(new Score("WEG", "4375"));
		display_list.add(new Score("UIY", "7831867"));
		display_list.add(new Score("JFJ", "438783777"));
		display_list.add(new Score("VJB", "786868"));*/
		
		updateCurrentScore();
		populateListView(root_view);
		
        return root_view;
    }
	
	private void updateCurrentScore(){
		if(GameInstance.basic_map_view != null && GameInstance.game_view != null){ //If scoreboard was called from pause menu
			if(GameInstance.basic_map_view.getMode() == MapView.PAUSED){
				current_score.setText(Integer.toString(GameInstance.game_view.score));
				current_score_label.setVisibility(View.VISIBLE); //Hide text "current score"
				current_score.setVisibility(View.VISIBLE); //Hide current score
			}
		}
		else{ //If scoreboard was called from main menu
			current_score_label.setVisibility(View.INVISIBLE); //Hide text "current score"
			current_score.setVisibility(View.INVISIBLE); //Hide current score
		}
	}
	
	private void populateListView(View root_view){
		
		//Build Adapter
		adapter_list = new ScoreListAdapter(
				getActivity(), //Context for the activity.
				display_list); //Items to be displayed
		
		//configure listview
		list = (ListView)root_view.findViewById(R.id.score_list);
		list.setAdapter(adapter_list);
	}
}
