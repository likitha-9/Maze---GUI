import java.util.Random;
import java.util.Stack;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GenerateMaze implements MazeGeneration.IterativeDivision, MazeGeneration.Board {

	static private int mazeWidth = 1090, mazeHeight = 670, cellWidth = 13, cellHeight = 13;
	static int cellsX = mazeWidth / cellWidth, cellsY = mazeHeight / cellHeight;
	public Cells[][] grid = new Cells[cellsX][cellsY];

	static Color wallColor = Color.DARKGREY, pathColor = Color.DARKRED, insetColor = Color.DARKBLUE;

	static int[][] checkedCells = new int[cellsX][cellsY];

	// each cell is represented by a square
	static class Cells extends StackPane {
		Rectangle cell = new Rectangle(cellWidth, cellHeight);

		public Cells(int x, int y) {
			getChildren().add(cell);
			setTranslateX(x * cellWidth);
			setTranslateY(y * cellHeight);

			cell.setFill(insetColor);
			if (x % 2 == 0 && y % 2 == 0) // create alternate cells (not visible to the user)
				setColor1();
			else
				setColor2();
		}

		public void setColor1() {
			cell.setFill(pathColor);
		}

		public void setColor2() {
			cell.setFill(wallColor);
		}

	}

	// create grid
	public Pane createBoard() {
		// Maze architectural layout
		Pane pane = new Pane();
		pane.setPadding(new Insets(10));
		pane.setPrefSize(mazeWidth, mazeHeight);
		// Store the cells in a grid
		for (int i = 0; i < cellsX; i++) {
			for (int j = 0; j < cellsY; j++) {
				grid[i][j] = new Cells(i, j);
				pane.getChildren().add(grid[i][j]);
			}
		}
		return pane;
	}

	void initializeCheckedCells() {
		/*
		 * Each cell has to be checked (whether a path exists from it or not) Initially,
		 * each cell is assumed to be unchecked.
		 */
		for (int i = 0; i < cellsX; i++)
			for (int j = 0; j < cellsY; j++)
				checkedCells[i][j] = 0;
	}

	// iterative division
	public void mazeAlgorithm() {

		/*
		 * In the proposal, I wanted to create the maze using recursive division
		 * algorithm. But because of the time lags that will possibly be faced
		 * throughout the program, it was determined that a recursive method would
		 * probably harm the program's efficiency. Therefore, an iterative approach was
		 * taken.
		 */
		for (int i = 0; i < cellsX; i++) {
			for (int j = 0; j < cellsY; j++) {
				try {
					if (checkedCells[i][j] == 0) {

						// check for every cell
						if (grid[i - 1][j].cell.getFill().equals(wallColor)
								&& grid[i + 1][j].cell.getFill().equals(wallColor)
								&& grid[i][j - 1].cell.getFill().equals(wallColor)
								&& grid[i][j + 1].cell.getFill().equals(wallColor)) {
							// call method if condition is satisfied
							// (Condition: if a room is surrounded by walls)
							insertPathways(i, j);
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {

					/*
					 * Each case in the catch {} block calls a new method, specific to the type of
					 * error. The type of error depends on where in the maze the exception occurred.
					 */

					// top left corner
					if (i == 0 && j == 0) {
						if (grid[i + 1][j].cell.getFill().equals(wallColor) // right cell
								&& grid[i][j + 1].cell.getFill().equals(wallColor))// bottom
							insertPathwaysForCorners(i, j, false, true, false, true);
					}

					// bottom left corner
					else if (i == 0 && j == (cellsY - 1)) {
						if (grid[i][j - 1].cell.getFill().equals(wallColor)// top cell
								&& grid[i + 1][j].cell.getFill().equals(wallColor))// right
							insertPathwaysForCorners(i, j, false, true, true, false);
					}

					// top right corner
					else if (i == (cellsX - 1) && j == 0) {
						if (grid[i - 1][j].cell.getFill().equals(wallColor)// left cell
								&& grid[i][j + 1].cell.getFill().equals(wallColor))// bottom
							insertPathwaysForCorners(i, j, true, false, false, true);
					}

					// bottom right corner
					else if (i == (cellsX - 1) && j == (cellsY - 1)) {
						if (grid[i][j - 1].cell.getFill().equals(wallColor) // top cell
								&& grid[i - 1][j].cell.getFill().equals(wallColor))// left
							insertPathwaysForCorners(i, j, true, false, true, false);
					}

					// top row
					else if (i > 0 && i < cellsX && j == 0) {
						if (grid[i - 1][j].cell.getFill().equals(wallColor) // left cell
								&& grid[i + 1][j].cell.getFill().equals(wallColor) // right
								&& grid[i][j + 1].cell.getFill().equals(wallColor)) // bottom
							insertPathwaysForUpperRow(i, j);
					}

					// bottom row
					else if (i > 0 && i < cellsX && j == (cellsY - 1)) {
						if (grid[i - 1][j].cell.getFill().equals(wallColor) // left cell
								&& grid[i + 1][j].cell.getFill().equals(wallColor) // right
								&& grid[i][j - 1].cell.getFill().equals(wallColor)) // top
							insertPathwaysForBottomRow(i, j);
					}

					// left column
					else if (i == 0 && j > 0 && j < cellsY) {
						if (grid[i][j - 1].cell.getFill().equals(wallColor) // top cell
								&& grid[i][j + 1].cell.getFill().equals(wallColor) // bottom
								&& grid[i + 1][j].cell.getFill().equals(wallColor)) // right
							insertPathwaysForLeftColumn(i, j);
					}

					// right column
					else if (i == (cellsX - 1) && j > 0 && j < cellsY) {
						if (grid[i][j - 1].cell.getFill().equals(wallColor) // top cell
								&& grid[i][j + 1].cell.getFill().equals(wallColor) // bottom
								&& grid[i - 1][j].cell.getFill().equals(wallColor)) // left
							insertPathwaysForRightColumn(i, j);
					}

					else
						;

				}
				checkedCells[i][j] = 1;
			}

		}

		// check for infinite loops, especially concerning those that have a single wall
		// in between.
		for (int i = 0; i < cellsX; i++) {
			for (int j = 0; j < cellsY; j++) {
				try {
					if (grid[i][j].cell.getFill().equals(wallColor)) // wall
					{
						// infinite loop around the wall
						// check only the neighbors (not diagonals)
						if (grid[i - 1][j].cell.getFill().equals(pathColor)
								&& grid[i + 1][j].cell.getFill().equals(pathColor)
								&& grid[i][j - 1].cell.getFill().equals(pathColor)
								&& grid[i][j + 1].cell.getFill().equals(pathColor))
							preventInfiniteLoops(i, j);
					}
				} catch (ArrayIndexOutOfBoundsException e1) {
					// ignore
				}
			}
		}
		if (solvability()) // display the maze;
			return;
		else
			for (int i = 0; i < cellsX; i++)
				for (int j = 0; j < cellsX; j++) {
					checkedCells[i][j] = 0;
					mazeAlgorithm();
				}

	}

	void insertPathways(int i, int j) {
		/*
		 * This method isn't called because of an error case. Instead, those cells which
		 * have 4 neighbors are tackled in this method.
		 * 
		 * One of the 4 neighbours will be selected for a pathway connection
		 */

		boolean leftFlag = new Random().nextBoolean(), topFlag = new Random().nextBoolean();

		if (leftFlag) // left neighbour will be selected
			grid[i - 1][j].cell.setFill(pathColor);
		else if (!leftFlag) // right neighbour will be selected
			grid[i + 1][j].cell.setFill(pathColor);

		if (topFlag) // top neighbor will be selected
			grid[i][j - 1].cell.setFill(pathColor);
		else if (!topFlag) // bottom neighbor will be selected
			grid[i][j + 1].cell.setFill(pathColor);
	}

	/*
	 * The next 6 methods are for dealing with specific error cases that may come up
	 * in mazeAlgorithm()
	 */
	void insertPathwaysForCorners(int i, int j, boolean left, boolean right, boolean top, boolean bottom) {

		/*
		 * Deal with 4 corners first. This means that only TWO flags would've been true
		 * at the same time.
		 */

		// initiate directional flags
		boolean horizontalFlag = true, verticalFlag = true;
		if (left)
			horizontalFlag = true; // one of the LEFT two corners
		else if (right)
			horizontalFlag = false; // one of the RIGHT two corners

		if (top)
			verticalFlag = true; // one of the TOP two corners
		else if (bottom)
			verticalFlag = false; // one of the BOTTOM two corners

		/*
		 * Now, choose one of the two flags to draw a path from the initial cell
		 */

		int direction = new Random().nextInt(2);

		/*
		 * if direction=0 => horizontalFlag; else=> verticalFlag
		 */

		if (direction == 0) {
			if (horizontalFlag)
				grid[i - 1][j].cell.setFill(pathColor);// left
			else
				grid[i + 1][j].cell.setFill(pathColor);// right
		}

		else {
			if (verticalFlag)
				grid[i][j - 1].cell.setFill(pathColor); // top
			else
				grid[i][j + 1].cell.setFill(pathColor);// bottom
		}
	}

	/*
	 * Deal with the other borders
	 * 
	 * Issue raised: 1. Determining in which row/column the exception is raised
	 * 
	 * For convenience, all the information is stored in a single variable and
	 * referenced later
	 * 
	 */
	void insertPathwaysForUpperRow(int i, int j) {

		// possible values for direction = 0, 1, 2
		int direction = new Random().nextInt(3); // integers from [0,3)

		/*
		 * If direction=0 ==> choose LEFT; If direction=1 ==> choose BOTTOM; If
		 * direction=2 ==> choose RIGHT;
		 */

		if (direction == 0)
			grid[i - 1][j].cell.setFill(pathColor);
		else if (direction == 1)
			grid[i][j + 1].cell.setFill(pathColor);
		else
			grid[i + 1][j].cell.setFill(pathColor);
	}

	void insertPathwaysForBottomRow(int i, int j) {
		// possible values for direction = 0, 1, 2
		int direction = new Random().nextInt(3); // integers from [0,3)

		/*
		 * If direction=0 ==> choose LEFT; If direction=1 ==> choose TOP; If direction=2
		 * ==> choose RIGHT;
		 */

		if (direction == 0)
			grid[i - 1][j].cell.setFill(pathColor);
		else if (direction == 1)
			grid[i][j - 1].cell.setFill(pathColor);
		else
			grid[i + 1][j].cell.setFill(pathColor);
	}

	void insertPathwaysForLeftColumn(int i, int j) {
		// possible values for direction = 0, 1, 2
		int direction = new Random().nextInt(3); // integers from [0,3)

		/*
		 * If direction=0 ==> choose TOP; If direction=1 ==> choose RIGHT; If
		 * direction=2 ==> choose BOTTOM;
		 */

		if (direction == 0)
			grid[i][j - 1].cell.setFill(pathColor);
		else if (direction == 1)
			grid[i + 1][j].cell.setFill(pathColor);
		else
			grid[i][j + 1].cell.setFill(pathColor);
	}

	void insertPathwaysForRightColumn(int i, int j) {
		// possible values for direction = 0, 1, 2
		int direction = new Random().nextInt(3); // integers from [0,3)

		/*
		 * If direction=0 ==> choose TOP; If direction=1 ==> choose LEFT; If direction=2
		 * ==> choose BOTTOM;
		 */

		if (direction == 0)
			grid[i][j - 1].cell.setFill(pathColor);
		else if (direction == 1)
			grid[i - 1][j].cell.setFill(pathColor);
		else
			grid[i][j + 1].cell.setFill(pathColor);
	}

	// Prevent multiple solutions and too many islands.
	// A few islands is okay, but not too many.
	void preventInfiniteLoops(int i, int j) {
		boolean leftFlag = new Random().nextBoolean(), topFlag = new Random().nextBoolean();

		if (leftFlag)
			grid[i - 1][j].cell.setFill(wallColor);
		else if (!leftFlag)
			grid[i + 1][j].cell.setFill(wallColor);

		if (topFlag)
			grid[i][j - 1].cell.setFill(wallColor);
		else if (!topFlag)
			grid[i][j + 1].cell.setFill(wallColor);
	}

	// Draw a distinct line around each inset.
	void insetsIntoWalls() {

		for (int i = 0; i < cellsX; i++) {
			for (int j = 0; j < cellsY; j++) {
				try {
					if (!AddMazeBoard.grid[i][j].border.getFill().equals(Maze.backgroundColor)) {
						if (AddMazeBoard.grid[i - 1][j].border.getFill().equals(Maze.backgroundColor)
								|| AddMazeBoard.grid[i + 1][j].border.getFill().equals(Maze.backgroundColor)
								|| AddMazeBoard.grid[i][j - 1].border.getFill().equals(Maze.backgroundColor)
								|| AddMazeBoard.grid[i][j + 1].border.getFill().equals(Maze.backgroundColor)) {
							grid[i][j].cell.setFill(insetColor);
							System.out.println("eq");
						}
					}
				} catch (Exception e) {
					// ignore
					// user will not be allowed to place inset in an impossible situation, so this
					// catch block will only deal with array index out of bounds exception.
				}

			}
		}
	}

	// Display openings within each inset as part of solution
	void mazeInInsets() {
		// display openings

		for (int i = 0; i < cellsX; i++) {
			for (int j = 0; j < cellsY; j++) {
				if (grid[i][j].cell.getFill().equals(insetColor)) {
					int opening = new Random().nextInt(8);
					if (opening == 4) {
						System.out.println(i + " " + j);
						
						//entrance
						if (grid[i - 1][j].cell.getFill().equals(pathColor)
								|| grid[i + 1][j].cell.getFill().equals(pathColor)) {
							grid[i][j].cell.setFill(pathColor);
						}

						//exit
						if (grid[i][j - 1].cell.getFill().equals(pathColor)
								|| grid[i][j + 1].cell.getFill().equals(pathColor)) {
							grid[i][j].cell.setFill(pathColor);
						}
					}
				}
			}
		}

	}

	// check whether the maze is solvable/not
	public boolean solvability() {
		Stack<Cells> stack = new Stack<Cells>();

		boolean flag = false;
		for (int i = 0; i < cellsX; i++) {
			for (int j = 0; j < cellsY; j++) {
				stack.push(grid[i][j]);
				// push items onto the stack as long as there is a way to move
				// 4 ways are checked: up, down, left, right
				if (grid[i - 1][j].cell.getFill().equals((pathColor))) {
					stack.push(grid[i - 1][j]);
					i = i - 1;
					continue;
				} else if (grid[i + 1][j].cell.getFill().equals((pathColor))) {
					stack.push(grid[i + 1][j]);
					i = i + 1;
					continue;
				} else if (grid[i][j - 1].cell.getFill().equals((pathColor))) {
					stack.push(grid[i][j - 1]);
					j = j - 1;
					continue;
				} else if (grid[i][j + 1].cell.getFill().equals((pathColor))) {
					stack.push(grid[i][j + 1]);
					j = j + 1;
					continue;
				}
				// pop item from stack when there is a dead end
				stack.pop();
				// set flag=true if the ending of maze is present on stack AND if all the insets
				// entrances & exits are present as well
				if (stack.contains(grid[cellsX - 1][cellsY - 1])) {
					int count = 0;
					for (int m = 0; m < stack.size(); m++) {
						for (int n = 0; n < cellsY; n++) {
							{
								if (!AddMazeBoard.grid[m][n].border.getFill().equals(Maze.backgroundColor))
									if (stack.contains(AddMazeBoard.grid[m][n]))
										count++;
							}
						}
					}
					if (count == Maze.count / 2)
						return true;
				}

			}
		}
		// if flag=false => stack doesn't have a solution with the destination
		// if so, return to mazeAlgorithm() again and run this method one more time

		return false;
	}

	// define the starting and ending positions of the user
	// starting - top left corner, ending - bottom right corner
	void startsEnds() {
		// starting position
		grid[0][0].cell.setFill(Color.DARKBLUE);
		grid[0][0].cell.setStroke(Color.CYAN);
		grid[0][0].cell.setStrokeWidth(2);
		// ending position - this is the goal where user must reach
		grid[cellsX - 1][cellsY - 1].cell.setFill(Color.DARKBLUE);
		grid[cellsX - 1][cellsY - 1].cell.setStroke(Color.CYAN);
		grid[cellsX - 1][cellsY - 1].cell.setStrokeWidth(2);
	}
}
