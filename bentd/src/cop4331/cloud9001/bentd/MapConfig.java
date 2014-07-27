package cop4331.cloud9001.bentd;

import java.util.ArrayList;

import android.util.Log;

public class MapConfig {
	/*
  	 * Indicates tile-by-tile which drawable to use in each location of the map
  	 * Leave transparent = 0
     * TOP_EDGE = 1; //Index to reference drawable
     * BOTTOM_EDGE = 2; //Index to reference drawable
     * LEFT_EDGE = 3; //Index to reference drawable
     * RIGHT_EDGE = 4; //Index to reference drawable
     * TOP_LEFT_CORNER = 5; //Index to reference drawable
     * TOP_RIGHT_CORNER = 6; //Index to reference drawable
     * BOTTOM_LEFT_CORNER = 7; //Index to reference drawable
     * BOTTOM_RIGHT_CORNER = 8; //Index to reference drawable
     * TOP_LEFT_INSIDE_CORNER = 9; //Index to reference drawable
     * TOP_RIGHT_INSIDE_CORNER = 10; //Index to reference drawable
     * BOTTOM_LEFT_INSIDE_CORNER = 11; //Index to reference drawable
     * BOTTOM_RIGHT_INSIDE_CORNER = 12; //Index to reference drawable
     * NO_EDGE = 13; //Index to reference drawable
  	 */
	private static int[][] map0 = new int[][]{
    		{0,  0,  0,  0,  0,  0,  0,  3,  4,  0}, //               Base
    		{0,  0,  0,  0,  0,  0,  0,  3,  4,  0}, //               Wall
    		{1,  1,  6,  0,  0,  0,  0,  3,  4,  0}, // ____           | |
    		{2, 11,  4,  0,  0,  0,  0,  3,  4,  0}, // __  |          | |
    		{0,  3, 10,  1,  1,  1,  1,  9,  4,  0}, //   | |__________| |
    		{0,  7,  2,  2,  2,  2,  2,  2,  8,  0}, //   |______________|
    		{0,  0,  0,  0,  0,  0,  0,  0,  0,  0}  //
	};
	
	public static int[][] getMap(int map_number){
		return map0;
	}
	/*
	 * LEVEL VARIABLES
	 */
	protected int startingMoney;
	protected int maxWaves;
	protected int health;
	protected Path enemyPath;
	protected int[] EnemiesPerWave;
	protected long timePerWave;
	
	public MapConfig(int[][] fieldOfBattle, int level){
		switch(level){
		case 0: //FIRST LEVEL
			startingMoney = 100;
			maxWaves = 8;
			timePerWave = 120000;
			health = 100;
			EnemiesPerWave = new int[maxWaves];
			for(int i=0;i<maxWaves;i++)
				EnemiesPerWave[i] = i+2;
				
			//enemyPath = new Path(map0, gv);
			//Log.i("MapConfig_24",enemyPath.toString());
			
			
			//Pre gen won't work i have no clue why
			//EnemiesPerWave = new ArrayList<ArrayList<Enemy>>();
			/*for(int i=0;i<maxWaves;i++){
				ArrayList<Enemy> combatants = new ArrayList<Enemy>();
				for(int j=0;j<1;j++){
					combatants.add(new Enemy(gv,fieldOfBattle,0));
				}
				EnemiesPerWave.add(combatants);
			}*/
			break;
			default:
				break;
		}
	}
	public MapConfig(int level){
		switch(level){
		case 0: //FIRST LEVEL
			startingMoney = 100;
			maxWaves = 8;
			timePerWave = 120000;
			health = 100;
			EnemiesPerWave = new int[maxWaves];
			for(int i=0;i<maxWaves;i++)
				EnemiesPerWave[i] = i+2;
				
			//enemyPath = new Path(map0, gv);
			//Log.i("MapConfig_24",enemyPath.toString());
			
			
			//Pre gen won't work i have no clue why
			//EnemiesPerWave = new ArrayList<ArrayList<Enemy>>();
			/*for(int i=0;i<maxWaves;i++){
				ArrayList<Enemy> combatants = new ArrayList<Enemy>();
				for(int j=0;j<1;j++){
					combatants.add(new Enemy(gv,fieldOfBattle,0));
				}
				EnemiesPerWave.add(combatants);
			}*/
			break;
			default:
				break;
		}
	}
	
}
