package edu.carleton.COMP2601;

import java.util.Random;

/**
 * Created by haamedsultani (100884545) on 2017-01-26.
 */

public class AIThread implements Runnable
{
    Game model;

    public AIThread(Game game)
    {
        this.model = game;
    }

    public void run() {
        // Sleep for 2 seconds
        try
        {
            while (!model.didGameEnd()) {
                Thread.sleep(2000);
                int i;
                int j;

                Random r = new Random();
                do {
                    i = r.nextInt(3);
                    j = r.nextInt(3);
                } while(!model.playTurn(i, j) && !model.didGameEnd());
            }
        } catch(InterruptedException e) {
            System.out.println("AI has been reset.");
        }
    }
}
