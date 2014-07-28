package cop4331.cloud9001.bentd;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ScoreBoardFragment extends Fragment{

	private final ArrayList<Score> displayList = new ArrayList<Score>(20);
	protected static ScoreListAdapter adapterList;
	protected static ListView list;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View root_view = inflater.inflate(R.layout.score_board_layout, container, false);
		
		populateListView(root_view);
		
        return root_view;
    }
	
	private void populateListView(View root_view){
		
		//Build Adapter
		adapterList = new ScoreListAdapter(
				getActivity(), //Context for the activity.
				displayList); //Items to be displayed
		
		//configure listview
		list = (ListView)root_view.findViewById(R.id.score_list);
		list.setAdapter(adapterList);
	}
}
