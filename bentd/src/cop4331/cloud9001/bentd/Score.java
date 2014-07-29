package cop4331.cloud9001.bentd;

public class Score{
	private String player;
	private String score;
	
	public Score(String p, String s){
		this.player = p;
		this.score = s;
	}
	
	public String getPlayer(){
		return this.player;
	}
	public String getScore(){
		return this.score;
	}
}
