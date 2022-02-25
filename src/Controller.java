/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS
 */
public class Controller {

    int LEVEL;

    public char switchPlayer(char currPlayer) {
        if (currPlayer == 'X') {
            return 'O';
        } else {
            return 'X';
        }
    }

    public void ComputerPlay(Board game, char currPlayer) {
        int temp = 0;
        int bestEvaluate = Integer.MIN_VALUE;
        Move bestmove = new Move();
        for (Move M : game.getNextPossibleMoves(currPlayer, 0)) {
            temp = M.tempBoard.bestPlay(M, 'O', Integer.MIN_VALUE, Integer.MAX_VALUE, 'm', LEVEL);
            System.out.println(temp);
            //System.out.println(B.evaluate(computer));
            M.tempBoard.draw();
            if (temp > bestEvaluate) {
                //System.out.println("my best evaluate " + temp);
                bestEvaluate = temp;
                bestmove = M;

            }
            if (M.tempBoard.isWin('O')) {
                bestmove = M;
                break;
            }
        }
        game.makeMove(currPlayer, bestmove.ID, bestmove.pos);
    }
}
