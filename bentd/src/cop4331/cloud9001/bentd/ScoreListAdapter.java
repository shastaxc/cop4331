package cop4331.cloud9001.bentd;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScoreListAdapter extends BaseAdapter {

	private final Activity activity;
	private ArrayList<Score> items;
	private LayoutInflater inflater = null;
	
	public ScoreListAdapter(Activity a, ArrayList<Score> items){
		this.activity = a;
		this.items = items;
		this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row_view = convertView;
        if (row_view == null){
            row_view = inflater.inflate(R.layout.row_item, null);
        }
        TextView player_text = (TextView) row_view.findViewById(R.id.player_text);
        TextView score_text = (TextView) row_view.findViewById(R.id.score_text);
        player_text.setText(items.get(position).getPlayer());
        score_text.setText(items.get(position).getScore());
        
        return row_view;
    }

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
