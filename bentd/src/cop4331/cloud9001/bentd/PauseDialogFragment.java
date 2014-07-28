package cop4331.cloud9001.bentd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;

public class PauseDialogFragment extends DialogFragment{
    private static final String[] dialog_list= new String[]{
    	"Resume Game",
    	"View High Scores"
    };
    
	public static PauseDialogFragment newInstance(int title) {
		PauseDialogFragment frag = new PauseDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
        	.setTitle(title)
        	.setInverseBackgroundForced(true)
        	.setItems(dialog_list, new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) {
        			switch(which){
        			case 0: //If Resume Game pressed
        	    		GameInstance.basic_map_view.setMode(MapView.RUNNING);
        	    		GameInstance.pause_btn.setBackgroundResource(R.drawable.pause_icon);
        				break;
        			case 1: //If View High Scores pressed
        				ScoreBoardFragment score_frag = new ScoreBoardFragment();
        		        FragmentManager fragmentManager=getFragmentManager();
        		        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        		        fragmentTransaction.add(R.id.game_frame,score_frag)
        		        					.addToBackStack("in-game-scoreboard")
        		        					.commit();
        				break;
        			}
        		}
        	})
        	.create();
    }
    
    @Override
    public void onCancel(DialogInterface dialog){
		GameInstance.basic_map_view.setMode(MapView.RUNNING);
		GameInstance.pause_btn.setBackgroundResource(R.drawable.pause_icon);
    	dialog.dismiss();
    }
}
