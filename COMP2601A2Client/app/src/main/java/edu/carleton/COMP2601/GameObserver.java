package edu.carleton.COMP2601;

/**
 * Created by haamedsultani (100884545) on 2017-01-24.
 */

public interface GameObserver {
    public void update(int[][] model, int gameState, int playerTurn, int cellNumber);
}
