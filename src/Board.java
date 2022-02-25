/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.LinkedList;
import java.util.List;
import javafx.scene.paint.Color;

/**
 *
 * @author ASUS
 */
public class Board {

    public Sections[][] blocks = new Sections[3][3];
    public Coordinates nextPlay = new Coordinates(-1, -1);
    public char winner[][] = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};
    private int fills = 0;

    public Board() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                blocks[i][j] = new Sections();
            }
        }
    }

    public Board(Board b) {
        for (int i = 0; i < 3; i++) {
            System.arraycopy(b.winner[i], 0, winner[i], 0, 3);
        }
        this.nextPlay = new Coordinates(b.nextPlay.x, b.nextPlay.y);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.blocks[i][j] = new Sections(b.blocks[i][j]);
            }
        }

        this.fills = b.fills;
    }

    public char getXO(Coordinates move) {
        return blocks[move.x / 3][move.y / 3].grid[move.x % 3][move.y % 3];
    }

    public Color getColor(Coordinates move) {
        if (getWinner(move) == 'X') {
            return Color.CORNFLOWERBLUE;
        } else if (getWinner(move) == 'O') {
            return Color.DARKRED;
        } else if (getWinner(move) == 'n') {
            return Color.DARKGREY;
        } else if (RelatedToSection(nextPlay, move) && getXO(move) == ' ') {
            return Color.GOLD;
        } else {
            return blocks[move.x / 3][move.y / 3].C[move.x % 3][move.y % 3];
        }
    }

    public Coordinates getID(Coordinates move) {
        return new Coordinates(move.x / 3, move.y / 3);
    }

    public char getWinner(Coordinates move) {
        Coordinates temp = getID(move);
        return winner[temp.x][temp.y];
    }

    public List<Move> getNextPossibleMoves(char nextPlayer, int score) {
        List<Move> nextBoards = new LinkedList<>();
        if (nextPlay.x == -1) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (!blocks[i][j].finished) {
                        List<SectionMove> SM = new LinkedList<>();
                        SM = blocks[i][j].getPossibleNextMove2(nextPlayer);

                        for (SectionMove sm : SM) {
                            Move M = new Move();
                            M.tempBoard = new Board(this);
                            //M.tempBoard.blocks[i][j] = new Sections(sm.tempSection);
                            M.tempBoard.makeMove(nextPlayer, new Coordinates(i, j), sm.pos);
                            M.pos.setCoor(sm.pos.x, sm.pos.y);
                            M.ID.setCoor(i, j);
                            if (M.tempBoard.nextPlay.x == -1) {
                                M.Score = score - 5;
                            } else if (winner[M.tempBoard.nextPlay.x][M.tempBoard.nextPlay.y] != ' ') {
                                M.Score = score - 5;
                            } else {
                                int toto = M.tempBoard.blocks[M.tempBoard.nextPlay.x][M.tempBoard.nextPlay.y].evaluate(nextPlayer);
                                M.Score = score + toto;
                                //System.out.println("score:" + M.tempBoard.nextPlay.x + ", " + M.tempBoard.nextPlay.y + ", " + toto);
                            }
                            nextBoards.add(M);
                        }
                    }
                }
            }
        } else {
            List<SectionMove> SM = new LinkedList<>();
            SM = blocks[nextPlay.x][nextPlay.y].getPossibleNextMove2(nextPlayer);

            for (SectionMove sm : SM) {
                Move M = new Move();
                M.tempBoard = new Board(this);
                //M.tempBoard.blocks[nextPlay.x][nextPlay.y] = new Sections(sm.tempSection);
                M.tempBoard.makeMove(nextPlayer, nextPlay, sm.pos);
                M.pos.setCoor(sm.pos.x, sm.pos.y);
                M.ID.setCoor(nextPlay.x, nextPlay.y);
                if (M.tempBoard.nextPlay.x == -1) {
                    M.Score = score - 5;
                } else if (winner[M.tempBoard.nextPlay.x][M.tempBoard.nextPlay.y] != ' ') {
                    M.Score = score - 5;
                } else {
                    //System.out.println("score:" + M.tempBoard.nextPlay.x + ", " + M.tempBoard.nextPlay.y);
                    int toto = M.tempBoard.blocks[M.tempBoard.nextPlay.x][M.tempBoard.nextPlay.y].evaluate(nextPlayer);
                    M.Score = score + toto;
                    //System.out.println("score:" + M.tempBoard.nextPlay.x + ", " + M.tempBoard.nextPlay.y + ", " + toto);
                }
                nextBoards.add(M);
            }
        }

        return nextBoards;
    }

    public int bestPlay(Move B, char P, int alpha, int beta, char goal, int depth) {

        char newGoal = ' ';
        int bestEvaluate = 0;

        if (goal == 'm') {
            newGoal = 'M';
            bestEvaluate = Integer.MAX_VALUE;
        } else {
            newGoal = 'm';
            bestEvaluate = Integer.MIN_VALUE;
        }

        //System.out.println(depth + ", " + P);
        if (depth != 0 && !isFinished()) {
        } else {
            if (isFinished()) {
                return B.tempBoard.evaluate(P);
            }
            //System.out.println("--------------------puzzelgame.Board.bestPlay() " + P);
            int eval = B.tempBoard.evaluate(P) + B.Score;
            eval += B.tempBoard.blocksEvaluate(P);
            return eval;
        }

        for (Move b : B.tempBoard.getNextPossibleMoves(otherPlayer(P), B.Score)) {
            int temp = b.tempBoard.bestPlay(b, otherPlayer(P), alpha, beta, newGoal, depth - 1);

            if (goal == 'M') {
                if (temp > bestEvaluate) {
                    bestEvaluate = temp;
                    alpha = Integer.max(alpha, bestEvaluate);
                    if (beta <= alpha) {
                        break;
                    }
                }
            } else {
                if (temp < bestEvaluate) {
                    bestEvaluate = temp;
                    beta = Integer.min(beta, bestEvaluate);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }

        return bestEvaluate;
    }

    public boolean makeMove(char player, Coordinates ID, Coordinates move) {
        if (CanMakeMove(ID, move)) {
            blocks[ID.x][ID.y].play(player, move.x % 3, move.y % 3);
            if (blocks[ID.x][ID.y].isFinished()) {
                if (blocks[ID.x][ID.y].isWithdrow()) {
                    winner[ID.x][ID.y] = 'n';
                } else {
                    winner[ID.x][ID.y] = player;
                }
                fills += 1;
            }
            DecideNextMove(move);
            return true;
        } else {
            System.out.println("can't make move here");
            return false;
        }
    }

    public boolean CanMakeMove(Coordinates ID, Coordinates move) {
        if (blocks[ID.x][ID.y].finished) {
            return false;
        } else if (blocks[ID.x][ID.y].grid[move.x % 3][move.y % 3] != ' ') {
            return false;
        } else {
            return true;
        }
    }

    public void DecideNextMove(Coordinates move) {

        if (blocks[move.x % 3][move.y % 3].finished) {
            nextPlay.setCoor(-1, -1);
        } else {
            nextPlay.setCoor(move.x % 3, move.y % 3);
        }
    }

    public boolean RelatedToSection(Coordinates ID, Coordinates move) {
        if (ID.x == -1) {
            return true;
        } else if (move.x / 3 == ID.x && move.y / 3 == ID.y) {
            return true;
        } else {
            return false;
        }
    }

    public int blocksEvaluate(char player) {
        int eval = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                eval += blocks[i][j].evaluate(player);
            }
        }
        return eval;
    }

    public int evaluate(char player) {
        int eval = 0;

        if (isWin(player)) {
            return Integer.MAX_VALUE;
        }
        if (isWin(otherPlayer(player))) {
            return Integer.MIN_VALUE;
        }
        //check columns
        for (int i = 0; i < 3; i++) {
            if (winner[0][i] != otherPlayer(player)) {
                if (winner[1][i] != otherPlayer(player)) {
                    if (winner[2][i] != otherPlayer(player)) {
                        eval += 3;
                    }
                }
            }
            if (winner[0][i] != player) {
                if (winner[1][i] != player) {
                    if (winner[2][i] != player) {
                        eval -= 3;
                    }
                }
            }
        }
        //check rows
        for (int i = 0; i < 3; i++) {
            if (winner[i][0] != otherPlayer(player)) {
                if (winner[i][1] != otherPlayer(player)) {
                    if (winner[i][2] != otherPlayer(player)) {
                        eval += 3;
                    }
                }
            }
            if (winner[i][0] != player) {
                if (winner[i][1] != player) {
                    if (winner[i][2] != player) {
                        eval -= 3;
                    }
                }
            }
        }

        //check diagonal 1
        if (winner[0][0] != otherPlayer(player)) {
            if (winner[1][1] != otherPlayer(player)) {
                if (winner[2][2] != otherPlayer(player)) {
                    eval += 3;
                }
            }
        }
        if (winner[0][0] != player) {
            if (winner[1][1] != player) {
                if (winner[2][2] != player) {
                    eval -= 3;
                }
            }
        }

        //check diagonal 2
        if (winner[0][2] != otherPlayer(player)) {
            if (winner[1][1] != otherPlayer(player)) {
                if (winner[2][0] != otherPlayer(player)) {
                    eval += 3;
                }
            }
        }
        if (winner[0][2] != player) {
            if (winner[1][1] != player) {
                if (winner[2][0] != player) {
                    eval -= 3;
                }
            }
        }
        //System.out.println("the eval is : " + eval);
        return eval;
    }

    public boolean isWithdrow() {
        return (fills == 9);
    }

    public boolean isWin(char player) {
        //check columns
        for (int i = 0; i < 3; i++) {
            if (winner[0][i] == player) {
                if (winner[1][i] == player) {
                    if (winner[2][i] == player) {
                        return true;
                    }
                }
            }
        }

        //check rows
        for (int i = 0; i < 3; i++) {
            if (winner[i][0] == player) {
                if (winner[i][1] == player) {
                    if (winner[i][2] == player) {
                        return true;
                    }
                }
            }
        }

        //check diagonal 1
        if (winner[0][0] == player) {
            if (winner[1][1] == player) {
                if (winner[2][2] == player) {
                    return true;
                }
            }
        }
        //check diagonal 2
        if (winner[0][2] == player) {
            if (winner[1][1] == player) {
                if (winner[2][0] == player) {
                    return true;
                }
            }
        }

        return false;
    }

    private char otherPlayer(char player) {
        if (player == 'X') {
            return 'O';
        }
        return 'X';
    }

    public boolean isFinished() {
        char tempPlayer = 'X';
        if (isWin(tempPlayer)) {
            //System.out.println("human wins !!");
            return true;
        }
        tempPlayer = 'O';
        if (isWin(tempPlayer)) {
            //System.out.println("computer wins !!");
            return true;
        }
        if (isWithdrow()) {
            //System.out.println("WithDrow !!");
            return true;
        }
        return false;
    }

    public void draw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        if (blocks[i][k].grid[j][l] == ' ') {
                            System.out.print(".");
                        } else {
                            System.out.print(blocks[i][k].grid[j][l]);
                        }
                    }
                    System.out.print(" ");
                }
                System.out.println();
            }
            System.out.println();
        }
        System.out.println("========================================");
    }
}
