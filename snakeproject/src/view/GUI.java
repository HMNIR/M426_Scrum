package view;

import static model.Direction.EAST;
import static model.Direction.NORTH;
import static model.Direction.SOUTH;
import static model.Direction.WEST;
import static view.CellStatus.BOARD;
import static view.CellStatus.SNAKE;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Snake;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class GUI extends Application {

    private Button btnPlay;
    private Button btnReset;

    private HBox buttonBox;
    private GridPane gamePane;

    private final int gridSize = 30;

    private Timeline timeline = new Timeline();

    private Snake snake;

    @Override
    public void start(Stage primaryStage) throws Exception {

	primaryStage.setTitle("Snake");
	snake = new Snake(gridSize);

	FlowPane root = new FlowPane(10, 10);
	root.setAlignment(Pos.BOTTOM_CENTER);

	gamePane = createGamePane();

	primaryStage.setScene(new Scene(root));

	btnPlay = new Button("Play");
	btnReset = new Button("Reset");

	btnPlay.setMinWidth(60);
	btnReset.setMinWidth(60);

	buttonBox = new HBox(3.0);
	buttonBox.getChildren().addAll(btnPlay, btnReset);

	btnPlay.setOnAction(event -> {
	    startSnakeGame();
	});

	btnReset.setOnAction(event -> {
	    timeline.pause();
	});

	root.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    @Override
	    public void handle(KeyEvent event) {
		if (event.getCode() == KeyCode.RIGHT) {
		    snake.setNewDirection(EAST);
		}
		if (event.getCode() == KeyCode.LEFT) {
		    snake.setNewDirection(WEST);
		}
		if (event.getCode() == KeyCode.UP) {
		    snake.setNewDirection(NORTH);
		}
		if (event.getCode() == KeyCode.DOWN) {
		    snake.setNewDirection(SOUTH);
		}
	    }
	});

	root.getChildren().addAll(gamePane, buttonBox);
	primaryStage.show();
    }

    private void startSnakeGame() {
	timeline = new Timeline(new KeyFrame(Duration.ZERO, new EventHandler() {
	    @Override
	    public void handle(Event event) {
		snake.move();
		if (snake.snakeRunOutOfField()) {
		    // TODO implement game over pane
		    Platform.exit();
		    System.exit(0);
		}
		repaintPane();
	    }
	}), new KeyFrame(Duration.millis(600)));

	timeline.setCycleCount(Timeline.INDEFINITE);
	timeline.play();

    }

    private int calcIndex(int row, int col) {
	return row * gridSize + col;
    }

    private void repaintPane() {
	for (int row = 0; row < gridSize; row++) {
	    for (int col = 0; col < gridSize; col++) {
		if (snake.isSnakePosition(row, col)) {
		    ((CellButton) gamePane.getChildren().get(calcIndex(row, col))).setStatus(SNAKE);
		} else {
		    ((CellButton) gamePane.getChildren().get(calcIndex(row, col))).setStatus(BOARD);
		}
	    }
	}
    }

    private GridPane createGamePane() {
	GridPane pane = new GridPane();
	for (int row = 0; row < gridSize; row++) {
	    for (int col = 0; col < gridSize; col++) {
		pane.add(new CellButton(), row, col);
	    }
	}
	return pane;
    }

    private void printGamePane() {
	for (int row = 0; row < gridSize; row++) {
	    for (int col = 0; col < gridSize; col++) {
		if (((CellButton) gamePane.getChildren().get(calcIndex(row, col))).getStatus().equals(SNAKE)) {
		    System.out.print("s");
		} else {
		    System.out.print("o");
		}
	    }
	    System.out.println();
	}
	System.out.println("end");
    }
}
