package cop4331.cloud9001.bentd;

import java.util.ArrayList;

import android.util.Log;

public class Path {
	private Integer x[];
	private Integer y[];
	
	private boolean completed;
	private int maxPoints;
	private int currWaypoint;
	
	public Path(){
		maxPoints = 7;
		
		x = new Integer[maxPoints];
		x[0] = 0;
		x[1] = 50;
		x[2] = 50;
		x[3] = 50;
		x[4] = 80;
		x[5] = 120;
		x[6] = 200;
		
		y = new Integer[maxPoints];
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
	public Path(Path path){
		maxPoints = path.x.length;
		x = new Integer[maxPoints];
		y = new Integer[maxPoints];
		
		for(int i=0;i<maxPoints;i++){
			x[i]=path.x[i];
			y[i]=path.y[i];
		}
		completed = false;
		currWaypoint = 0;
	}
	public Path(int[][] map, GameView gv) {
		maxPoints = 0;
		ArrayList<Integer> xPts = new ArrayList<Integer>();
		ArrayList<Integer> yPts = new ArrayList<Integer>();
		int gridH = gv.getHeight()/map.length;
		int gridW = gv.getWidth()/map[0].length;
		
		for(int i=0;i<map.length;i++){
			if(map[i][0] > 0){
				yPts.add(i*gridH);
				xPts.add(0);
				int j=1;
				while(true){
					switch(map[i][j]){
					case 1:
						yPts.add(i*gridH);
						xPts.add(j*gridW);
						j++;
						break;
					case 6:
						yPts.add(i*gridH);
						xPts.add(j*gridW);
						i++;
						break;
					case 4:
						yPts.add(i*gridH);
						xPts.add(j*gridW);
						i++;
						break;
					case 10:
						yPts.add(i*gridH);
						xPts.add(j*gridW);
						j++;
						break;
					case 9:
						yPts.add(i*gridH);
						xPts.add(j*gridW);
						i--;
						break;
					case 3:
						yPts.add(i*gridH);
						xPts.add(j*gridW);
						i--;
						break;
					}
					if(i<0)
						break;
				}
				i=map.length;
			}
		}
		x = xPts.toArray(new Integer[0]);
		y = yPts.toArray(new Integer[0]);
		maxPoints = xPts.size();
		currWaypoint = 0;
		completed = false;
		
	}
	public int oldX(){
		return x[currWaypoint-1];
	}
	public int oldY(){
		return y[currWaypoint-1];
	}
	public int nextX(){
		return x[currWaypoint];
	}
	public int nextY(){
		return y[currWaypoint];
	}
	public int currentWaypoint(){
		return currWaypoint;
	}
	public static int direction(Path p, int i1, int i2){
		if(i1>=p.maxPoints || i2 >=p.maxPoints)
			return -1;
		if(p.x[i1] == p.x[i2]){//up or down
			if(p.y[i1] > p.y[i2])
				return 0;
			else
				return 2;
		}
		else{
			if(p.x[i1] < p.x[i2])
				return 1;
			else
				return 3;
		}
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
	
	public String toString(){
		String str = "";
		for(int i=0;i<x.length;i++){
			str = str +"("+x[i]+","+y[i]+") ";
		}
		return str;
	}
}
