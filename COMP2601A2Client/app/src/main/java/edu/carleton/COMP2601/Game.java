package edu.carleton.COMP2601;

import java.util.ArrayList;

/**
 * Created by haamedsultani (100884545) on 2017-01-23.
 */

public class Game
{
    /*
     This array is used to check the state of each image button.
     Values --> -1: EMPTY, 0: O, 1: X
      */
    private int[][] gameButtons = new int[3][3];

    /*
    List of Observers (UI observes the model).
     */
    ArrayList<GameObserver> gameObservers;

    /*
    Model State.
     */
    private int playerTurn;
    private int gameState;

    /*
    CONSTANTS.
     */
    private final static int PLAYER_O = 0;
    private final static int PLAYER_X = 1;
    private final static int EMPTY_CELL = -1;
    private final static int NUMBER_OF_CELLS = 9;

    // Constructor
    public Game(GameObserver observer) {
        for (int i = 0; i < this.gameButtons[0].length; i++) {
            for (int j = 0; j < this.gameButtons[1].length; j++) {
                this.gameButtons[i][j] = EMPTY_CELL;
            }
        }

        playerTurn = PLAYER_X;
        gameState = Constants.GAME_OVER;

        // Add observer to list for updates
        gameObservers = new ArrayList<GameObserver>();
        gameObservers.add(observer);
    }

    public int getPlayerTurn() { return this.playerTurn; }

    // Return the 2D array of tiles
    public int[][] getGameButtons() { return this.gameButtons; }

    public int getState() { return this.gameState; }

    /**
     * Plays a turn in the current game
     * at the specified cell.
     *
     * @param i the row of the cell
     * @param j the oolumn of the cell
     * @return true if the turn was successfully played, false otherwise
     */
    synchronized public boolean playTurn(int i, int j) {
        // Play the turn only if game is running
        if(didGameEnd()) {
            return false;
        }

        // Set the cell to the current player's value (X or O)
        if (i >= 0 && i < this.gameButtons[0].length &&
                j >=0 && j < this.gameButtons[1].length) {
            // Return false if cell is occupied
            if (this.gameButtons[i][j] != EMPTY_CELL) {
                return false;
            }

            this.gameButtons[i][j] = playerTurn;
        }

        // Check if the game is over
        boolean gameEnded = didGameEnd();

        // Switch the player's turn
        switchTurn(gameEnded);

        // Compute cell number
        int cellNumber = getCellNumber(i, j);

        // Update the observers based on game state
        if(gameEnded) {
            // Check if game was won
            if(checkDiagonal() || checkHorizontal() || checkVertical()) {
                gameState = Constants.GAME_OVER;
                this.notifyObservers(Constants.GAME_OVER, playerTurn, cellNumber);
            } else if(didGameTie()) {
                gameState = Constants.GAME_TIED;
                this.notifyObservers(Constants.GAME_TIED, playerTurn, cellNumber);
            }
        } else {
            gameState = Constants.GAME_RUNNING;
            this.notifyObservers(Constants.GAME_RUNNING, playerTurn, cellNumber);
        }

        // Turn played successfully
        return true;

    }

    /**
     * Switches the current player from X to O and vice versa.
     *
     * @param gameEnded state of the game
     */
    public void switchTurn(boolean gameEnded) {
        if(!gameEnded) {
            if(playerTurn == PLAYER_X) {
                playerTurn = PLAYER_O;
            } else {
                playerTurn = PLAYER_X;
            }
        }
    }


    /**
     * Resets the model, game state to RUNNING, and sets
     * up a new game. Notifies observers that game is RUNNING.
     */
    public void createNewGame() {
        for (int i = 0; i < this.gameButtons[0].length; i++) {
            for (int j = 0; j < this.gameButtons[1].length; j++) {
                this.gameButtons[i][j] = EMPTY_CELL;
            }
        }

        // Reset turn to player X
        playerTurn = PLAYER_X;

        // Change Game State to running
        gameState = Constants.GAME_RUNNING;

        // Notify Observers
        notifyObservers(Constants.GAME_RUNNING, playerTurn, Constants.NO_CELL);
    }

    /**
     * Resets the model, game state to GAME OVER, and
     * ends the game. Notifies the observers that GAME IS OVER.
     */
    public void endGame() {
        for (int i = 0; i < this.gameButtons[0].length; i++) {
            for (int j = 0; j < this.gameButtons[1].length; j++) {
                this.gameButtons[i][j] = EMPTY_CELL;
            }
        }

        // Reset turn to player X
        playerTurn = PLAYER_X;

        // Change Game State to Over
        gameState = Constants.GAME_OVER;

        // Notify Observers
        notifyObservers(Constants.GAME_OVER, playerTurn, Constants.NO_CELL);
    }


    /**
     * Checks if the game was won horizontally.
     *
     * @return true if horizontal win, false otherwise
     */
    private boolean checkHorizontal() {
        for (int i = 0; i < 3; i++) {
            if (this.gameButtons[i][0] != EMPTY_CELL) {
                if (this.gameButtons[i][0] == this.gameButtons[i][1] &&
                        this.gameButtons[i][0] == this.gameButtons[i][2]) {
                    System.out.println("**Horizontal win**");

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the game was won vertically.
     *
     * @return true if vertical win, false otherwise
     */
    private boolean checkVertical() {
        for (int i = 0; i < 3; i++) {
            if (this.gameButtons[0][i] != EMPTY_CELL) {
                if (this.gameButtons[0][i] == this.gameButtons[1][i] &&
                        this.gameButtons[0][i] == this.gameButtons[2][i]) {
                    System.out.println("**Vertical win**");

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the game was won diagonally.
     *
     * @return true if diagonal win, false otherwise
     */
    private boolean checkDiagonal() {
        // If diagonal is negative slope: \
        if (this.gameButtons[1][1] != EMPTY_CELL) {
            if (this.gameButtons[0][0] == this.gameButtons[1][1] &&
                    this.gameButtons[0][0] == this.gameButtons[2][2]) {
                System.out.println("**NEGATIVE Diagonal win**");

                return true;
            }

            if (this.gameButtons[0][2] == this.gameButtons[1][1] &&
                    this.gameButtons[0][2] == this.gameButtons[2][0]) {
                System.out.println("**POSITIVE Diagonal win**");

                return true;
            }
        }

        return false;
    }


    /**
     * Checks if the game is over.
     * Runs a check for all win conditions.
     * Runs a check for a tie.
     *
     * @return true if game is over, false otherwise
     */
    public boolean didGameEnd() {
        boolean gameEnded = false;

        // Check for a win condition
        if (checkHorizontal() || checkVertical() || checkDiagonal()) {
            gameEnded = true;
        }
        else {
            if(didGameTie()) {
                gameEnded = true;
            }
        }

        return gameEnded;
    }

    /**
     * Checks if the game has tied.
     *
     * @return true if game tied, false otherwise
     */
    private boolean didGameTie() {
        int counter = 0;

        for(int i = 0; i < gameButtons.length; i++) {
            for (int j = 0; j < gameButtons[i].length; j++) {
                if (gameButtons[i][j] != EMPTY_CELL) {
                    counter++;
                }
            }
        }

        if(counter == NUMBER_OF_CELLS) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates all the observers on the model's state.
     *
     * @param gameState current state of game - RUNNING, OVER, or TIED
     * @param playerTurn current player - X or O
     * @param cellNumber cell number which was last played
     */
    private void notifyObservers(int gameState, int playerTurn, int cellNumber) {
        for (GameObserver observer: gameObservers) {
            observer.update(this.gameButtons, gameState, playerTurn, cellNumber);
        }
    }

    /**
     * Get the number of the cell (0-8)
     * for the corresponding row and column.
     *
     * @param i the row of the cell
     * @param j the column of the cell
     * @return number of cell between range 0-8
     */
    private int getCellNumber(int i, int j) {
        int counter = 0;
        int cellNumber = 0;

        for (int x = 0; x < gameButtons.length; x++) {
            for (int y = 0; y < gameButtons[x].length; y++) {
                if (x == i && y == j) {
                    cellNumber = counter;
                }
                else {
                    counter++;
                }
            }
        }

        return cellNumber;
    }
}