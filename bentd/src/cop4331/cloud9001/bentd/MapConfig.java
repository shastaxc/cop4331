package cop4331.cloud9001.bentd;

public class MapConfig {
	/*
  	 * private static final int ALL_EDGE = 0; //Index to reference drawable
    private static final int TOP_EDGE = 1; //Index to reference drawable
    private static final int BOTTOM_EDGE = 2; //Index to reference drawable
    private static final int LEFT_EDGE = 3; //Index to reference drawable
    private static final int RIGHT_EDGE = 4; //Index to reference drawable
    private static final int TOP_LEFT_CORNER = 5; //Index to reference drawable
    private static final int TOP_RIGHT_CORNER = 6; //Index to reference drawable
    private static final int BOTTOM_LEFT_CORNER = 7; //Index to reference drawable
    private static final int BOTTOM_RIGHT_CORNER = 8; //Index to reference drawable
    private static final int TOP_LEFT_INSIDE_CORNER = 9; //Index to reference drawable
    private static final int TOP_RIGHT_INSIDE_CORNER = 10; //Index to reference drawable
    private static final int BOTTOM_LEFT_INSIDE_CORNER = 11; //Index to reference drawable
    private static final int BOTTOM_RIGHT_INSIDE_CORNER = 12; //Index to reference drawable
    private static final int NO_EDGE = 13; //Index to reference drawable
    private static final int TOWER_SITE = 14; //Index to reference drawable
  	 */
	private static int[][] map0 = new int[][]{
    		{ 0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //               Base
    		{14, 14, 14, 14,  0,  0, 14,  3,  4, 14}, //               Wall
    		{ 1,  1,  6, 14,  0,  0, 14,  3,  4, 14}, // ____           | |
    		{ 2, 11,  4, 14, 14, 14, 14,  3,  4, 14}, // __  |          | |
    		{14,  3, 10,  1,  1,  1,  1,  9,  4, 14}, //   | |__________| |
    		{14,  7,  2,  2,  2,  2,  2,  2,  8, 14}, //   |______________|
    		{14, 14, 14, 14, 14, 14, 14, 14, 14, 14}  //
	};
	public static int[][] getMap(int map_number){
		return map0;
	}
}
