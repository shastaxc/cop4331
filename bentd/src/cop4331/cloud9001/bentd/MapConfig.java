package cop4331.cloud9001.bentd;

public class MapConfig {
	/*
  	 * Indicates tile-by-tile which drawable to use in each location of the map
  	 * Leave transparent = 0
  	 * path: T=1, B=2, L=3, R=4, TL=5, TR=6, BL=7, BR=8, no_edge=9
  	 */
	public static int[][] map0 = new int[][]{
    		{0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //               Base
    		{0,  0,  0,  0,  0,  0,  0,  3,  4,  0}, //               Wall
    		{1,  1,  6,  0,  0,  0,  0,  3,  4,  0}, // ____           | |
    		{2,  9,  4,  0,  0,  0,  0,  3,  4,  0}, // __  |          | |
    		{0,  3,  9,  1,  1,  1,  1,  9,  4,  0}, //   | |__________| |
    		{0,  7,  2,  2,  2,  2,  2,  2,  8,  0}, //   |______________|
    		{0,  0,  0,  0,  0,  0,  0,  0,  0,  0}  //
	};
	public static int[][] getMap(int map_number){
		return map0;
	}
}
