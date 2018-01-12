package edu.carleton.COMP2601;

/**
 * Created by Hrishi Mukherjee (100888108) on 2017-02-15.
 */

public class Constants {

    /*
    NETWORK CONSTANTS
     */
    public static final String SERVER_ADDRESS    = "192.168.2.129";
    public static final int    SERVER_PORT       = 7004;

    public static final String USERNAME          = "hermoine.granger";

    public static final int DISPLAY_SPINNER      = 1;
    public static final int HIDE_SPINNER         = 0;

    /*
    GAME STATES.
     */
    public static final int GAME_OVER    = 0;
    public static final int GAME_RUNNING = 1;
    public static final int GAME_TIED    = 2;

    /*
    PLAYER TURNS.
     */
    public static final int PLAYER_O     = 0;
    public static final int PLAYER_X     = 1;

    /*
    CELL NUMBERS.
     */
    public static final int NO_CELL      = -1;

    /*
    THE GRID.
     */
    public static final int EMPTY_CELL   = -1;
    public static final int CELL_O       = 0;
    public static final int CELL_X       = 1;

    private Constants() {
        throw new RuntimeException();
    }

}
