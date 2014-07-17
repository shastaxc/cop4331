package cop4331.cloud9001.bentd;

public class Path {
	private int x[];
	private int y[];
	//private boolean visited[];
	
	private boolean completed;
	private int maxPoints;
	private int currWaypoint;
	
	public Path(){
		maxPoints = 7;
		
		x = new int[maxPoints];
		x[0] = 0;
		x[1] = 50;
		x[2] = 50;
		x[3] = 50;
		x[4] = 80;
		x[5] = 120;
		x[6] = 200;
		
		y = new int[maxPoints];
		y[0] = 0;
		y[1] = 20;
		y[2] = 70;
		y[3] = 100;
		y[4] = 100;
		y[5] = 100;
		y[6] = 200;
		//visited = new boolean[maxPoints];
		
		currWaypoint = 0;
		completed = false;
	}
	public int nextX(){
		return x[currWaypoint];
	}
	public int nextY(){
		return y[currWaypoint];
	}
	public boolean nextWaypoint(){
		//visited[currWaypoint]=true;
		currWaypoint++;
		if(currWaypoint >= maxPoints){
			completed = true;
			return true;
		}
		else
			return false;
	}
	public boolean done(){
		return completed;
	}
}
