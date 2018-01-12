package edu.carleton.COMP2601;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by haamedsultani (100884545) on 2017-01-27.
 *
 * Game local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GameUnitTest {
    //Creating an anonymous observer to test total MVC functionality
    GameObserver gameObserver = new GameObserver() {
        @Override
        public void update(int[][] model, int gameState, int playerTurn, int cellNumber) {
            System.out.println("Cell number " + cellNumber + " changed.");
            System.out.println("Game state: " + gameState);
            System.out.println("It is now player " + playerTurn + "'s turn.");
            System.out.println("Tic Tac Toe board: \n" + Arrays.deepToString(model) + "\n");
        }
    };

    //Class used for Tic Tac Toe game logic
    Game game = new Game(gameObserver);



    @Test
    public void horizontalWin_isCorrect() throws Exception {
        System.out.println("\n\nTESTING HORIZONTAL WIN");

        //Resetting the game
        game.createNewGame();

        //Should return 1 which is X's turn
        assertEquals(1, game.getPlayerTurn());

        game.playTurn(0, 0);//X makes a move
        game.playTurn(1, 0);//O makes a move
        game.playTurn(0, 1);//X makes a move
        game.playTurn(2,1);//O makes a move
        game.playTurn(0,2);//X makes a move, should result in a horizontal win for X

        assertEquals(true, game.didGameEnd());//Game has ended due to a win
    }


    @Test
    public void verticalWin_isCorrect() throws Exception {
        System.out.println("\n\nTESTING VERTICAL WIN");

        //Resetting the game
        game.createNewGame();

        //Should return 1 which is X's turn
        assertEquals(1, game.getPlayerTurn());

        game.playTurn(0, 1);//X makes a move
        game.playTurn(0, 0);//O makes a move
        game.playTurn(2, 2);//X makes a move
        game.playTurn(1,0);//O makes a move
        game.playTurn(1,1);//X makes a move
        game.playTurn(2,0);//O makes a move which results in a vertical win

        assertEquals(true, game.didGameEnd());//Game has ended due to a win
    }


    @Test
    public void diagonalWin_isCorrect() throws Exception {
        System.out.println("\n\nTESTING DIAGONAL WIN");

        //Resetting the game
        game.createNewGame();

        //Should return 1 which is X's turn
        assertEquals(1, game.getPlayerTurn());

        game.playTurn(0, 0);//X makes a move
        game.playTurn(0, 1);//O makes a move
        game.playTurn(1, 1);//X makes a move
        game.playTurn(1,0);//O makes a move
        game.playTurn(2,2);//X makes a move and wins with a diagonal

        assertEquals(true, game.didGameEnd());//Game has ended due to a win
    }


    @Test
    public void tieGame_isCorrect() throws Exception {
        System.out.println("\n\nTESTING TIE GAME");

        //Resetting the game
        game.createNewGame();

        //Should return 1 which is X's turn
        assertEquals(1, game.getPlayerTurn());

        game.playTurn(0, 0);//X makes a move
        game.playTurn(0,1);//O makes a move
        game.playTurn(0,2);//X makes a move
        game.playTurn(1,0);//O makes a move
        game.playTurn(1,1);//X makes a move
        game.playTurn(2,0);//O makes a move
        game.playTurn(1,2);//X makes a move
        game.playTurn(2,2);//O makes a move
        game.playTurn(2,1);//X makes a move

        assertEquals(true, game.didGameEnd());//Game has ended due to a tie
    }
}