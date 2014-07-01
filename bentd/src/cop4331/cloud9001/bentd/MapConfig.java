package cop4331.cloud9001.bentd;

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
    		{0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //               Base
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
}
