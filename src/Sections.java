import java.util.LinkedList;
import java.util.List;
import javafx.scene.paint.Color;

public class Sections {

    public char[][] grid = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};
    private int fills;
    public boolean finished = false;
    public Color[][] C = new Color[3][3];

    public Sections() {
        fills = 0;
        setAllColors();
    }

    public Sections(Sections board) {
        for (int i = 0; i < 3; i++) {
            System.arraycopy(board.grid[i], 0, grid[i], 0, 3);
        }
        this.fills = board.fills;
    }

    public void setAllColors()
    {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                C[i][j] = Color.GREY;
            }
        }
    }

    public List<SectionMove> getPossibleNextMove2(char nextPlayer) {
        List<SectionMove> nextMove = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == ' ') {
                    SectionMove SM = new SectionMove();
                    SM.tempSection = new Sections(this);
                    SM.pos.setCoor(i, j);
                    nextMove.add(SM);
                }
            }
        }
        return nextMove;
    }
    
    public void play(char player, int x, int y) {
        if (grid[x][y] == ' ') {
            grid[x][y] = player;
            fills++;
        }
    }

    public int evaluate(char player) {
        int eval = 0;
        
        if (isWin(player)) {
            return 10;
        }
        if (isWin(otherPlayer(player))) {
            return -10;
        }
        //check columns
        for (int i = 0; i < 3; i++) {
            if (grid[0][i] != otherPlayer(player)) {
                if (grid[1][i] != otherPlayer(player)) {
                    if (grid[2][i] != otherPlayer(player)) {
                        eval += 1;
                    }
                }
            }
            if (grid[0][i] != player) {
                if (grid[1][i] != player) {
                    if (grid[2][i] != player) {
                        eval -= 1;
                    }
                }
            }
        }
        //check rows
        for (int i = 0; i < 3; i++) {
            if (grid[i][0] != otherPlayer(player)) {
                if (grid[i][1] != otherPlayer(player)) {
                    if (grid[i][2] != otherPlayer(player)) {
                        eval += 1;
                    }
                }
            }
            if (grid[i][0] != player) {
                if (grid[i][1] != player) {
                    if (grid[i][2] != player) {
                        eval -= 1;
                    }
                }
            }
        }

        //check diagonal 1
        if (grid[0][0] != otherPlayer(player)) {
            if (grid[1][1] != otherPlayer(player)) {
                if (grid[2][2] != otherPlayer(player)) {
                    eval += 1;
                }
            }
        }
        if (grid[0][0] != player) {
            if (grid[1][1] != player) {
                if (grid[2][2] != player) {
                    eval -= 1;
                }
            }
        }

        //check diagonal 2
        if (grid[0][2] != otherPlayer(player)) {
            if (grid[1][1] != otherPlayer(player)) {
                if (grid[2][0] != otherPlayer(player)) {
                    eval += 1;
                }
            }
        }
        if (grid[0][2] != player) {
            if (grid[1][1] != player) {
                if (grid[2][0] != player) {
                    eval -= 1;
                }
            }
        }

        return eval;
    }

    public boolean isWithdrow() {
        return (fills == 9);
    }

    public boolean isWin(char player) {
        //check columns
        for (int i = 0; i < 3; i++) {
            if (grid[0][i] == player) {
                if (grid[1][i] == player) {
                    if (grid[2][i] == player) {
                        return true;
                    }
                }
            }
        }

        //check rows
        for (int i = 0; i < 3; i++) {
            if (grid[i][0] == player) {
                if (grid[i][1] == player) {
                    if (grid[i][2] == player) {
                        return true;
                    }
                }
            }
        }

        //check diagonal 1
        if (grid[0][0] == player) {
            if (grid[1][1] == player) {
                if (grid[2][2] == player) {
                    return true;
                }
            }
        }
        //check diagonal 2
        if (grid[0][2] == player) {
            if (grid[1][1] == player) {
                if (grid[2][0] == player) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isFinished() {
        char tempPlayer = 'X';
        if (isWin(tempPlayer)) {
            //System.out.println("human wins !!");
            finished = true;
            return true;
        }
        tempPlayer = otherPlayer(tempPlayer);
        if (isWin(tempPlayer)) {
            //System.out.println("computer wins !!");
            finished = true;
            return true;
        }
        if (isWithdrow()) {
            //System.out.println("WithDrow !!");
            finished = true;
            return true;
        }
        return false;
    }

    private char otherPlayer(char player) {
        if (player == 'X') {
            return 'O';
        }
        return 'X';
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(grid[i][j]);
                sb.append(" | ");
            }
            sb.delete(sb.length() - 2, sb.length() - 1);
            sb.append('\n');
        }
        return sb.toString();
    }

}
