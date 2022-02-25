import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.KeyEvent;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author ASUS
 */
public class GUI extends Application {

    Stage window;
    int h = 11, w = 11;
    Board game = new Board();
    char currPlayer = 'X';
    int LEVEL = 0;
    Controller controller = new Controller();

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = new Stage();

        window.setTitle("This the game window");

        askInfo();
        MakeGameGrid();

        window.show();

    }

    public void askInfo() {
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(5, 5, 5, 5));
        gp.setVgap(5);
        gp.setHgap(10);
        gp.setAlignment(Pos.CENTER);

        Label hightLab = new Label("Enter LEVEL:");
        GridPane.setConstraints(hightLab, 0, 0);

        ComboBox comboBox = new ComboBox();
        comboBox.setPromptText("EASY");
        comboBox.setValue("EASY");
        comboBox.getItems().addAll(
                "EASY",
                "MEDIUM",
                "HARD"
        );
        GridPane.setConstraints(comboBox, 1, 0);
        
        
        Button bot = new Button("Submit");
        GridPane.setConstraints(bot, 1, 2);

        gp.getChildren().addAll(hightLab, comboBox, bot);

        bot.setOnAction(e -> {
            switch ((String) comboBox.getValue()) {
                case "EASY":
                    controller.LEVEL = 0;
                    break;

                case "MEDIUM":
                    controller.LEVEL = 2;
                    break;
                    
                case "HARD":
                    controller.LEVEL = 6;
                    break;
            }
            window.close();
            return;
        });

        Scene scene1 = new Scene(gp, 300, 250);
        window.setScene(scene1);
        window.showAndWait();
    }

    transient Rectangle[][] rect;
    transient Text[][] names;
    transient Text player;

    public void MakeGameGrid() {
        rect = new Rectangle[h][w];
        names = new Text[h][w];

        player = new Text(String.valueOf(currPlayer));
        player.setFill(Color.BLUE);
        player.setFont(Font.font(null, FontWeight.SEMI_BOLD, 50 / 1.7));

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);

        root.setVgap(2);
        root.setHgap(2);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                rect[i][j] = new Rectangle(33, 33);
                rect[i][j].setFill(Color.GOLD);

                if (i == 3 || i == 7 || j == 3 || j == 7) {
                    rect[i][j].setX(15);
                    rect[i][j].setY(15);
                    rect[i][j].setFill(Color.BLACK);
                }

                if (i > 7) {
                    rect[i][j].setX(i - 2);
                } else if (i > 3) {
                    rect[i][j].setX(i - 1);
                } else {
                    rect[i][j].setX(i);
                }

                if (j > 7) {
                    rect[i][j].setY(j - 2);
                } else if (j > 3) {
                    rect[i][j].setY(j - 1);
                } else {
                    rect[i][j].setY(j);
                }

                names[i][j] = new Text("");
                names[i][j].setFill(Color.BLACK);
                names[i][j].setFont(Font.font(null, FontWeight.SEMI_BOLD, 33 / 1.7));
                names[i][j].setTranslateX(33 / 2 - 33 / 8);

                root.add(rect[i][j], j, i);
                root.add(names[i][j], j, i);
            }
        }
        setOnSomething();

        BorderPane mainRoot = new BorderPane();
        mainRoot.setCenter(root);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(player);

        mainRoot.setTop(vbox);
        Scene scene = new Scene(mainRoot, 600, 600);

        scene.setOnMousePressed((MouseEvent e) -> {
            if ("PRIMARY".equals(e.getButton().toString())) {

            } else {
                List<Board> l = new LinkedList<>();
                //l = game.getBoards(currPlayer);
                //for(Board ll : l)
                //ll.draw();
            }
        });
        //G.checkAvailableMoves();
        window.setScene(scene);
        window.show();
    }

    void update() {
        int tempX;
        int tempY;
        Coordinates temp = new Coordinates(0, 0);

        if (currPlayer == 'X') {
            player.setFill(Color.BLUE);
            player.setText("X");
        } else {
            player.setFill(Color.RED);
            player.setText("O");
        }
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                //rect[i][j] = new Rectangle(33, 33);
                tempX = (int) rect[i][j].getX();
                tempY = (int) rect[i][j].getY();
                temp.setCoor(tempX, tempY);

                if (i != 3 && i != 7 && j != 3 && j != 7) {
                    if (game.getWinner(temp) == ' ') {
                        if (game.nextPlay.x == -1) {
                            if (game.getXO(temp) == ' ') {
                                rect[i][j].setFill(Color.GOLD);
                            } else {
                                rect[i][j].setFill(game.getColor(temp));
                            }
                        } else {
                            if (game.RelatedToSection(game.nextPlay, temp) && game.getXO(temp) == ' ') {
                                rect[i][j].setFill(Color.GOLD);
                            } else {
                                rect[i][j].setFill(game.getColor(temp));
                            }
                        }
                    } else if (game.getWinner(temp) == 'X') {
                        rect[i][j].setFill(Color.CORNFLOWERBLUE);
                    } else if (game.getWinner(temp) == 'O') {
                        rect[i][j].setFill(Color.DARKRED);
                    } else {
                        rect[i][j].setFill(Color.DARKGRAY);
                    }
                    names[i][j].setText(String.valueOf(game.getXO(temp)));
                }
            }
        }
    }

    void makeThePlay(int x, int y) {
        Coordinates move = new Coordinates(x, y);
        Coordinates ID = game.getID(move);
        boolean gameEnded = false;
        if (game.nextPlay.x == -1 || game.RelatedToSection(game.nextPlay, move)) {
            if (game.makeMove(currPlayer, ID, move)) {
                if (game.isFinished()) {
                    update();
                    confirmWin.display(currPlayer);
                    window.close();
                    gameEnded = true;
                }
                currPlayer = controller.switchPlayer(currPlayer);
                update();
                if(gameEnded) return;
                //currPlayer = controller.switchPlayer(currPlayer);
                controller.ComputerPlay(game, currPlayer);
                if (game.isFinished()) {
                    update();
                    confirmWin.display(currPlayer);
                    window.close();
                }
                currPlayer = controller.switchPlayer(currPlayer);
                update();
            }
        } else {
            System.out.println("invalid move");
        }
    }

    void setOnSomething() {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                final int h = i, hh = j;
                final Coordinates coor = new Coordinates((int) rect[i][j].getX(), (int) rect[i][j].getY());
                if (h != 3 && h != 7 && hh != 3 && hh != 7) {

                    rect[i][j].setOnMouseClicked(e -> {
                        //System.out.println(rect[h][hh].getX() + ", " + rect[h][hh].getY());
                        makeThePlay((int) rect[h][hh].getX(), (int) rect[h][hh].getY());
                    });
                    names[i][j].setOnMouseClicked(e -> {
                        //System.out.println(rect[h][hh].getX() + ", " + rect[h][hh].getY());
                        makeThePlay((int) rect[h][hh].getX(), (int) rect[h][hh].getY());
                    });
                    rect[i][j].setOnMouseEntered(e -> {
                        rect[h][hh].setFill(Color.LIGHTGREY);
                    });
                    rect[i][j].setOnMouseExited(e -> {
                        rect[h][hh].setFill(game.getColor(coor));
                    });
                    names[i][j].setOnMouseEntered(e -> {
                        rect[h][hh].setFill(Color.LIGHTGREY);
                    });
                    names[i][j].setOnMouseExited(e -> {
                        rect[h][hh].setFill(game.getColor(coor));
                    });
                }
            }
        }
    }

}
