package edu.carleton.COMP2601;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by hrishimukherjee (100888108) on 2017-01-23.
 */

public class GameActivity extends AppCompatActivity implements GameObserver, View.OnClickListener {

    /*
    User Interface Components.
     */
    private ImageButton[][] gameGrid = new ImageButton[3][3];
    private EditText statusBar;
    private Button startButton;

    /*
    Model of the Game.
     */
    private Game model;

    /*
    Computer Player Thread.
     */
    private Thread AI;

    /*
    This client's symbol.
     */
    private int playerSymbol;
    private String opponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Extract Player Symbol
        Bundle extras = getIntent().getExtras();

        playerSymbol = extras.getInt("playerSymbol");
        opponent = extras.getString("opponentName");

        Log.d("GA/onCreate", "Player Symbol: " + playerSymbol);
        Log.d("GA/onCreate", "Opponent Name: " + opponent);

        model = new Game(this);

        initializeUI();
        setListeners();

        // Update the Connection class with this activity
        Connection.getInstance().setGameActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Send Game Over Message
        Connection.getInstance().sendGameOver(Constants.USERNAME,
                opponent, Constants.USERNAME + " quit the game.");
    }

    /**
     * Initializes the UI components.
     */
    private void initializeUI() {
        // Initialize Grid
        gameGrid[0][0] = (ImageButton) findViewById(R.id.button_0);
        gameGrid[0][1] = (ImageButton) findViewById(R.id.button_1);
        gameGrid[0][2] = (ImageButton) findViewById(R.id.button_2);

        gameGrid[1][0] = (ImageButton) findViewById(R.id.button_3);
        gameGrid[1][1] = (ImageButton) findViewById(R.id.button_4);
        gameGrid[1][2] = (ImageButton) findViewById(R.id.button_5);

        gameGrid[2][0] = (ImageButton) findViewById(R.id.button_6);
        gameGrid[2][1] = (ImageButton) findViewById(R.id.button_7);
        gameGrid[2][2] = (ImageButton) findViewById(R.id.button_8);

        // Initialize Status Bar
        statusBar = (EditText) findViewById(R.id.edit_text_status);

        // Initialize Start Button
        startButton = (Button) findViewById(R.id.button_start);

        // Disable buttons
        setEnabledGrid(false);
    }

    /**
     * Sets the listeners for the components.
     */
    public void setListeners() {
        // Game Grid Button Listeners
        for(int i = 0; i < gameGrid.length; i++) {
            for(int j = 0; j < gameGrid[i].length; j++) {
                gameGrid[i][j].setOnClickListener(this);
            }
        }

        // Start Button Listener
        startButton.setOnClickListener(this);
    }

    /**
     * On Click Handler for all
     * views in the Main Activity.
     *
     * @param v the view calling the onClick
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_0: {
                onGridClick(0, 0, v);
                break;
            } case R.id.button_1: {
                onGridClick(0, 1, v);
                break;
            } case R.id.button_2: {
                onGridClick(0, 2, v);
                break;
            } case R.id.button_3: {
                onGridClick(1, 0, v);
                break;
            } case R.id.button_4: {
                onGridClick(1, 1, v);
                break;
            } case R.id.button_5: {
                onGridClick(1, 2, v);
                break;
            } case R.id.button_6: {
                onGridClick(2, 0, v);
                break;
            } case R.id.button_7: {
                onGridClick(2, 1, v);
                break;
            } case R.id.button_8: {
                onGridClick(2, 2, v);
                break;
            } case R.id.button_start: {
                onStartClick(model.getState());
                break;
            }
        }
    }

    public void gameOn(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Update Start Button & Status Bar
                updateStartButtonText(getResources().getString(R.string.button_stop));
                updateStatusBar(message);

                // Enable the Grid
                setEnabledGrid(true);

                // Start a new game
                model.createNewGame();
            }
        });
    }

    public void receiveMoveMessage(int i, int j) {
        model.playTurn(i, j);

        Log.d("GA/receiveMove", "Model State: " + model.getState());
        Log.d("GA/receiveMove", "Model Player Turn: " + model.getPlayerTurn());

        /*
         If it is THIS client's turn and the game is over
         this implies that the previous turn was made by the opponent
         and THIS client has lost the game
          */
        if(model.getState() == Constants.GAME_OVER) {
            if(model.getPlayerTurn() != playerSymbol) {
                // Update opponent
                Log.d("GA/receiveMove", "Sending opponent won the game message...");
                Connection.getInstance().sendGameOver(Constants.USERNAME,
                        opponent, "You won the game.");

                // Update local
                updateStatusBar(opponent + " won the game.");
                updateStartButtonText("START");
            }
        } else if(model.getState() == Constants.GAME_TIED) {
            // Update opponent
            Log.d("GA/receiveMove", "Sending game tied message...");
            Connection.getInstance().sendGameOver(Constants.USERNAME,
                    opponent, "The game was a draw.");

            // Update local
            updateStatusBar("The game was a draw.");
            updateStartButtonText("START");
        }
    }

    public void receiveGameOverMessage(String reason) {

    }

    /**
     * Set of actions to perform
     * on the click of the start button.
     * Actions change based on current
     * state of model.
     *
     * @param modelState the current state of the model
     */
    public void onStartClick(int modelState) {
        if (modelState == Constants.GAME_OVER ||
                modelState == Constants.GAME_TIED) {
            gameOn("");

            // Send game on message
            Connection.getInstance().
                    sendGameOn(Constants.USERNAME, opponent);
        } else if(modelState == Constants.GAME_RUNNING) {
            // End the game
            model.endGame();

            // Update Start Button
            startButton.setText(R.string.button_start);
            statusBar.setText("I ended the game.");

            // Disable all buttons
            setEnabledGrid(false);

            // Send Game Over Message to Opponent
            Connection.getInstance().sendGameOver(Constants.USERNAME,
                    opponent, Constants.USERNAME + " ended the game.");
        }
    }

    /**
     * Set of actions to perform on the click
     * of a button in the grid.
     *
     * @param i the row index of the button
     * @param j the column index of the button
     * @param v the button which was clicked
     */
    public void onGridClick(int i, int j, View v) {
        // Play a turn
        model.playTurn(i, j);

        // Send Move Message
        Connection.getInstance().sendMoveMessage(Constants.USERNAME,
                opponent, i, j);

        // Disable the button to avoid multiple clicks
        v.setEnabled(false);
    }

    /**
     * Updates the view, given
     * the state of the model.
     *
     * @param modelState the state of the grid
     * @param gameState current state of the game - RUNNING, OVER, or TIED
     * @param playerTurn 0 for player O, 1 for player X
     * @param cellNumber cell number of last cell updated
     */
    public void update(final int[][] modelState, final int gameState, final int playerTurn, final int cellNumber) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // Disable buttons when opponents's turn
                if (playerTurn != playerSymbol) {
                    setEnabledGrid(false);
                }
                else {
                    //Enable buttons when your turn
                    setEnabledGrid(true);
                }

                // Update game grid with model values
                for(int i = 0; i < modelState.length; i++) {
                    for(int j = 0; j < modelState[i].length; j++) {
                        updateButton(gameGrid[i][j], modelState[i][j]);
                    }
                }

                /*
                Update view based on game state.
                */
                if(gameState == Constants.GAME_OVER) {
                    // Disable the buttons
                    setEnabledGrid(false);
                    // startButton.setText(R.string.button_start);
                } else if(gameState == Constants.GAME_TIED) {
                    // statusBar.setText(R.string.game_tied);

                    // Disable the buttons
                    setEnabledGrid(false);
                    // startButton.setText(R.string.button_start);
                } else if(gameState == Constants.GAME_RUNNING) {
                    if (cellNumber != Constants.NO_CELL) {
                        if(playerTurn == playerSymbol) {
                            statusBar.setText("Button " + cellNumber +
                                    " clicked by " + opponent);
                        } else {
                            statusBar.setText("Button " + cellNumber +
                                    " clicked by " + Constants.USERNAME);
                        }
                    }
                }
            }
        });
    }

    /**
     * Updates the given button to either X, O, or BLANK,
     * according to the state.
     *
     * @param button the button object to be updated
     * @param state the state of the button
     */
    private void updateButton(ImageButton button, int state) {
        if(state == Constants.CELL_O) {
            button.setImageResource(R.drawable.zero);
        } else if(state == Constants.CELL_X) {
            button.setImageResource(R.drawable.cross);
        } else if(state == Constants.EMPTY_CELL) {
            button.setImageResource(0);
        }
    }

    /**
     * Enables/Disables the game grid.
     *
     * @param enabled true to enable, false for disable
     */
    private void setEnabledGrid(boolean enabled) {
        for(int i = 0; i < gameGrid.length; i++) {
            for(int j = 0; j < gameGrid[i].length; j++) {
                gameGrid[i][j].setEnabled(enabled);
            }
        }
    }

    /**
     * User friendly name for the player.
     *
     * @param playerTurn the integer value of the player
     * @return user friendly player name
     */
    private String getPlayerName(int playerTurn) {
        if(playerTurn == 0) {
            return opponent;
        } else {
            return "I";
        }
    }

    /**
     * Updates the button text.
     *
     * @param text updated text
     */
    public void updateStartButtonText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText(text);
            }
        });
    }

    /**
     * Updates the status bar.
     *
     * @param message the updated message
     */
    public void updateStatusBar(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusBar.setText(message);
            }
        });
    }

}
