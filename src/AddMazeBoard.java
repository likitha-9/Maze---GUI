import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AddMazeBoard implements MazeGeneration.Board {

	static final int sizeOfCellWidth = 20, sizeOfCellHeight = 20, boardWidth = 600, boardHeight = 300;
	static final int cellsX = boardWidth / sizeOfCellWidth;
	static final int cellsY = boardHeight / sizeOfCellHeight;
	public static Board[][] grid = new Board[cellsX][cellsY];

	static class Board extends StackPane {

		// each rectangle is defined as a square. A square = 1 cell in the blank canvas
		Rectangle border = new Rectangle(sizeOfCellWidth, sizeOfCellHeight);

		// This constructor is called each time a new cell has to be defined
		public Board(int x, int y) {
			// Add each cell as the pane's children
			getChildren().add(border);
			// Each cell's dimensions are of sizeOfCellWidth and sizeOfCellHeight
			setTranslateX(x * sizeOfCellWidth);
			setTranslateY(y * sizeOfCellHeight);
			// Each cell is initially of a light blue color
			border.setFill(Color.ALICEBLUE);
			border.setStroke(Color.CADETBLUE);
		}

	}

	public Pane createBoard() {
		Pane pane = new Pane();
		pane.setPadding(new Insets(10));
		// Define board's dimensions
		pane.setPrefSize(boardWidth, boardHeight);
		// Fill the board with cells
		for (int i = 0; i < cellsX; i++) {
			for (int j = 0; j < cellsY; j++) {
				grid[i][j] = new Board(i, j);
				pane.getChildren().add(grid[i][j]);
			}
		}
		//Return pane filled with children
		return pane;
	}

}
