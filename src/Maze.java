import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/* 
 * AUTHOR: SIVA LIKITHA VALLURU 
 * MAZE PROJECT - DUE DATE: NOVEMBER 20, 2018
 * 
 * In the FIRST short project, JApplets were used. 
 * But I have realized by now that Swing and AWT are quite outdated and were no longer being developed.
 * JavaFX is the new trend.
 * 
 * Even in the INITIAL PROPOSAL, JApplets were provided. But, with your permission, they were discarded and JavaFX was used. 
 * This entire project is implemented using JavaFX. 
 *   
 */

/*
 * 													NOTE: USE CAMEL-NOTATION!!!!!!!!!
 * 													NOTE: USE CAMEL-NOTATION!!!!!!!!!
 * 													NOTE: USE CAMEL-NOTATION!!!!!!!!!
 */

public class Maze extends Application implements EventHandler<ActionEvent>, MazeGeneration.Menu {

	/*
	 * NOTE: To gain a better grip of coding and utilize Java's features, most of
	 * the ActionEvents will be implemented using Lambda Expressions (more compact &
	 * easier).
	 */
	Button instructions, inset1, inset2, inset3, inset4, generateMaze, restartMaze, exitApp;

	/*
	 * Each inset will be colored with a different color. In case the colors have to
	 * be changed in the future, instead of changing each instance, a variable is
	 * defined. So, in case of future color changes, just one instance can be
	 * changed.
	 * 
	 * If no inset is placed, then backgroundColor will be defined (initialized for
	 * each cell from the beginning).
	 */
	static Color backgroundColor = Color.ALICEBLUE, inset1Color = Color.RED, inset2Color = Color.BLUEVIOLET,
			inset3Color = Color.HOTPINK, inset4Color = Color.LIME;

	static int count = 0;

	@Override
	public void handle(ActionEvent event) {
	}

	static Stage mainStage;

	// Create instances of other classes
	static AddMazeBoard board = new AddMazeBoard();
	static GenerateMaze generate = new GenerateMaze();

	// determine if maze has been generated
	static boolean generateFlag = false; // only if this is true will the user be able to play

	// current position of user
	static int currentPositionRow = 0, currentPositionColumn = 0;

	// previous visited node
	static int visitedRow = 0, visitedColumn = 0;
	static Color visitedColor = Color.DARKRED;

	// destination that the user must reach
	static int endRow = generate.cellsX - 1, endColumn = generate.cellsY - 1;

	static BorderPane border = new BorderPane();

	public static void main(String[] args) {
		launch(args); // Start JavaFX application.
	}

	/*
	 * Terminology: in JavaFX, the 'window' is called the Stage. And the 'stuff
	 * inside the window (content)' is called the Scene.
	 */

	@Override
	public void start(Stage mainStage) throws Exception {

		mainStage.setTitle("MAZEEEEE!");
		border.setRight(addVBox());
		border.setLeft(board.createBoard());
		border.setPadding(new Insets(10));
		Scene mainScene = new Scene(border, 2000, 690);

		/*
		 * There are FOUR insets that the user can choose from. He can place the insets
		 * wherever he wants to. He can place however many he wants to. BUT, an error
		 * WILL occur if the user places the insets in a way that it overlaps ANOTHER
		 * inset OR if the user places the inset where there is not sufficient space
		 * available.
		 * 
		 * The four event handlers down below draw the inset onto the empty canvas and
		 * thus help user visualize what and where he's placing into his desired maze.
		 * 
		 */

		// CANDY CANE
		inset1.setOnMouseClicked(e -> {
			count++;
			System.out.println("Inset1");
			mainScene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					boolean flag = true;
					System.out.println((int) e.getX() / 13 + " " + (int) e.getY() / 13);
					try {
						// CHECK IF THIS INSERTED INSET OVERLAPS ANOTHER INSET!!
						for (int i = 0; i < 11; i++)
							for (int j = 0; j < 6; j++)
								try {
									if (board.grid[((int) e.getX() / 13) + i][((int) e.getY() / 13) - j].border
											.getFill().equals(backgroundColor))
										continue;
									else {
										flag = false;
										break;
									}
								} catch (Exception e1) {
								}
						for (int i = 0; i < 5; i++)
							for (int j = 0; j < 13; j++)
								try {
									if (board.grid[((int) e.getX() / 13) + i + 7][((int) e.getY() / 13) + j + 1].border
											.getFill().equals(backgroundColor))
										continue;
									else {
										flag = false;
										break;
									}
								} catch (Exception e1) {
								}

						// IF THERE IS NO OVERLAP
						if (flag) {
							for (int i = 0; i < 5; i++) {
								// horizontal line from starting point
								board.grid[((int) e.getX() / 13) + i][((int) e.getY() / 13)].border
										.setFill(inset1Color);
								// vertical line across from starting point
								board.grid[((int) e.getX() / 13)][((int) e.getY() / 13) - i].border
										.setFill(inset1Color);
								// the block in the row of starting point and vertical down
								for (int j = 0; j < 14; j++)
									board.grid[((int) e.getX() / 13) + i + 7][((int) e.getY() / 13) + j].border
											.setFill(inset1Color);
								// the block from the row above starting point and vertical up
								for (int j = 0; j < 6; j++)
									board.grid[((int) e.getX() / 13) + i + 7][((int) e.getY() / 13) + j - 6].border
											.setFill(inset1Color);
								// 5x5 square at the top of candy cane
								for (int j = 0; j < 5; j++) {
									board.grid[((int) e.getX() / 13) + i + 2][((int) e.getY() / 13) + j - 6].border
											.setFill(inset1Color);
									// remaining line
									board.grid[((int) e.getX() / 13) + 1][((int) e.getY() / 13) + j - 5].border
											.setFill(inset1Color);
								}

							}
							for (int i = 0; i < 2; i++) {
								// middle 2 squares joining the big block and left side
								board.grid[((int) e.getX() / 13) + 4][((int) e.getY() / 13) - i].border
										.setFill(inset1Color);
								// connecting the other 2 squares above the previous line of code
								board.grid[((int) e.getX() / 13) + 5 + i][((int) e.getY() / 13) - 1].border
										.setFill(inset1Color);
								// last 2 squares
								board.grid[((int) e.getX() / 13) + 2 + i][((int) e.getY() / 13) - 1].border
										.setFill(inset1Color);
							}

							// crop off the ends to make the shape a little curvy

							// original square
							board.grid[((int) e.getX() / 13)][((int) e.getY() / 13)].border.setFill(backgroundColor);

							// top right corner
							board.grid[((int) e.getX() / 13) + 9][((int) e.getY() / 13) - 6].border
									.setFill(backgroundColor);
							board.grid[((int) e.getX() / 13) + 11][((int) e.getY() / 13) - 4].border
									.setFill(backgroundColor);
							for (int i = 0; i < 2; i++) {
								for (int j = 0; j < 2; j++)
									board.grid[((int) e.getX() / 13) + 10 + i][((int) e.getY() / 13) - 6 + j].border
											.setFill(backgroundColor);

							}

							// ends of the cane
							board.grid[((int) e.getX() / 13) + 11][((int) e.getY() / 13) + 13].border
									.setFill(backgroundColor);
							board.grid[((int) e.getX() / 13) + 7][((int) e.getY() / 13) + 13].border
									.setFill(backgroundColor);
						} else {
							Stage errorStage = new Stage();
							errorStage.initOwner(mainStage);
							errorStage.setTitle("ERROR!");

							VBox errorDialogBox = new VBox(20);
							errorDialogBox.getChildren().addAll(new Text("Error: Inset overlaps another!"),
									new Text("Choose another open area!"));
							Scene errorScene = new Scene(errorDialogBox, 500, 75);
							errorStage.setScene(errorScene);
							errorStage.show();
						}
					}
					// prevent user from inserting in random (little) places.
					catch (ArrayIndexOutOfBoundsException bounds) {

						/*
						 * ArrayOutOfBoundsException ===> User most likely clicked at the extreme ends
						 * of the maze board.
						 */

						for (int i = 0; i < 11; i++)
							for (int j = 0; j < 6; j++)
								try {
									board.grid[((int) e.getX() / 13) + i][((int) e.getY() / 13) - j].border
											.setFill(backgroundColor);
								} catch (Exception e1) {
								}
						for (int i = 0; i < 5; i++)
							for (int j = 0; j < 13; j++)
								try {
									board.grid[((int) e.getX() / 13) + i + 7][((int) e.getY() / 13) + j + 1].border
											.setFill(backgroundColor);
								} catch (Exception e1) {
								}

						Stage errorStage = new Stage();
						errorStage.initOwner(mainStage);
						errorStage.setTitle("ERROR!");

						VBox errorDialogBox = new VBox(20);
						errorDialogBox.getChildren().addAll(
								new Text("Error: There's not enough space to insert the inset!"),
								new Text("Choose another open area!"));
						Scene errorScene = new Scene(errorDialogBox, 500, 75);
						errorStage.setScene(errorScene);
						errorStage.show();
					}
					mainScene.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
				}
			});
		});

		// CHRISTMAS STOCKING
		inset2.setOnMouseClicked(e -> {
			count++;
			System.out.println("Inset2");
			mainScene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					boolean flag = true;
					System.out.println((int) e.getX() / 13 + " " + (int) e.getY() / 13);
					try {
						// CHECK IF THIS INSERTED INSET OVERLAPS ANOTHER INSET!!
						// stocking's base
						for (int i = 0; i < 29; i++)
							for (int j = 0; j < 11; j++)
								try {
									if (board.grid[((int) e.getX() / 13) + i - 14][((int) e.getY() / 13) - j].border
											.getFill().equals(backgroundColor))
										continue;
									else {
										flag = false;
										break;
									}
								} catch (Exception e1) {
								}
						// stocking's head
						for (int i = 0; i < 15; i++)
							for (int j = 0; j < 13; j++)
								try {
									if (board.grid[((int) e.getX() / 13) + i - 14][((int) e.getY() / 13) + j].border
											.getFill().equals(backgroundColor))
										continue;
									else {
										flag = false;
										break;
									}
								} catch (Exception e1) {
								}

						// IF THERE IS NO OVERLAP
						if (flag) {
							// stocking's base
							for (int i = 0; i < 29; i++) {
								for (int j = 0; j < 11; j++) {
									board.grid[((int) e.getX() / 13) + i - 14][((int) e.getY() / 13) + j].border
											.setFill(inset2Color);
								}
							}
							// stocking's head (with respect to original point)
							for (int i = 0; i < 15; i++) {
								for (int j = 0; j < 13; j++)
									board.grid[((int) e.getX() / 13) - i][((int) e.getY() / 13) + j - 13].border
											.setFill(inset2Color);
							}
							// stocking's fur
							for (int i = 0; i < 19; i++) {
								for (int j = 0; j < 2; j++)
									board.grid[((int) e.getX() / 13) + i - 16][((int) e.getY() / 13) + j - 12].border
											.setFill(inset2Color);
							}
							for (int i = 0; i < 17; i++) {
								board.grid[((int) e.getX() / 13) + i - 15][((int) e.getY() / 13) - 13].border
										.setFill(inset2Color);
								board.grid[((int) e.getX() / 13) + i - 15][((int) e.getY() / 13) - 10].border
										.setFill(inset2Color);
							}

							// crop off the edges to make the snowman look a little curvy and realistic
							// top right (near the feet)
							for (int i = 5; i > 0; i--) {
								for (int j = i; j > 0; j--)
									board.grid[((int) e.getX() / 13) + i + 9][((int) e.getY() / 13) + j - 1].border
											.setFill(backgroundColor);

							}
							// bottom right (near the feet)
							board.grid[((int) e.getX() / 13) + 14][((int) e.getY() / 13) + 8].border
									.setFill(backgroundColor);
							board.grid[((int) e.getX() / 13) + 12][((int) e.getY() / 13) + 10].border
									.setFill(backgroundColor);
							for (int i = 0; i < 2; i++) {
								for (int j = 0; j < 2; j++)
									board.grid[((int) e.getX() / 13) + i + 13][((int) e.getY() / 13) + j + 9].border
											.setFill(backgroundColor);
							}

							// bottom left
							board.grid[((int) e.getX() / 13) - 12][((int) e.getY() / 13) + 10].border
									.setFill(backgroundColor);
							board.grid[((int) e.getX() / 13) - 14][((int) e.getY() / 13) + 8].border
									.setFill(backgroundColor);
							for (int i = 0; i < 2; i++) {
								for (int j = 0; j < 2; j++)
									board.grid[((int) e.getX() / 13) + i - 14][((int) e.getY() / 13) + j + 9].border
											.setFill(backgroundColor);
							}

							// single dot connecting the rectangle and bottom base
							board.grid[((int) e.getX() / 13) + 1][((int) e.getY() / 13) - 1].border
									.setFill(inset2Color);

						} else {
							Stage errorStage = new Stage();
							errorStage.initOwner(mainStage);
							errorStage.setTitle("ERROR!");

							VBox errorDialogBox = new VBox(20);
							errorDialogBox.getChildren().addAll(new Text("Error: Inset overlaps another!"),
									new Text("Choose another open area!"));
							Scene errorScene = new Scene(errorDialogBox, 500, 75);
							errorStage.setScene(errorScene);
							errorStage.show();
						}
					}
					// prevent user from inserting in random (little) places.
					catch (ArrayIndexOutOfBoundsException bounds) {

						/*
						 * ArrayOutOfBoundsException ===> User most likely clicked at the extreme ends
						 * of the maze board.
						 */
						for (int i = 0; i < 29; i++) {
							for (int j = 0; j < 11; j++) {
								try {
									board.grid[((int) e.getX() / 13) + i - 14][((int) e.getY() / 13) + j].border
											.setFill(backgroundColor);
								} catch (Exception e1) {
								}
							}
						}
						// stocking's head (with respect to original point)
						for (int i = 0; i < 15; i++) {
							for (int j = 0; j < 13; j++)
								try {
									board.grid[((int) e.getX() / 13) + i][((int) e.getY() / 13) + j - 13].border
											.setFill(backgroundColor);
								} catch (Exception e1) {
								}
						}
						// stocking's fur
						for (int i = 0; i < 19; i++) {
							for (int j = 0; j < 2; j++)
								try {
									board.grid[((int) e.getX() / 13) + i - 16][((int) e.getY() / 13) + j - 12].border
											.setFill(backgroundColor);
								} catch (Exception e1) {
								}
						}

						Stage errorStage = new Stage();
						errorStage.initOwner(mainStage);
						errorStage.setTitle("ERROR!");

						VBox errorDialogBox = new VBox(20);
						errorDialogBox.getChildren().addAll(
								new Text("Error: There's not enough space to insert the inset!"),
								new Text("Choose another open area!"));
						Scene errorScene = new Scene(errorDialogBox, 500, 75);
						errorStage.setScene(errorScene);
						errorStage.show();
					}
					mainScene.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
				}
			});
		});

		// SNOWMAN
		inset3.setOnMouseClicked(e -> {
			count++;
			System.out.println("Inset3");
			mainScene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					boolean flag = true;
					System.out.println((int) e.getX() / 13 + " " + (int) e.getY() / 13);
					try {
						// CHECK IF THIS INSERTED INSET OVERLAPS ANOTHER INSET!!
						// snowman's head - does it overlap with another??
						for (int i = 0; i < 7; i++)
							for (int j = 0; j < 7; j++)
								try {
									if (board.grid[((int) e.getX() / 13) + i - 3][((int) e.getY() / 13) - j].border
											.getFill().equals(backgroundColor))
										continue;
									else {
										flag = false;
										break;
									}
								} catch (Exception e1) {
								}
						// snowman's body - does it overlap with another????
						for (int i = 0; i < 15; i++)
							for (int j = 0; j < 15; j++)
								try {
									if (board.grid[((int) e.getX() / 13) + i - 7][((int) e.getY() / 13) + j + 7].border
											.getFill().equals(backgroundColor))
										continue;
									else {
										flag = false;
										break;
									}
								} catch (Exception e1) {
								}

						// IF THERE IS NO OVERLAP
						if (flag) {
							// snowman's head
							for (int i = 0; i < 7; i++) {
								for (int j = 0; j < 7; j++) {
									board.grid[((int) e.getX() / 13) + i - 3][((int) e.getY() / 13) + j].border
											.setFill(inset3Color);
								}
							}
							// snowman's body
							for (int i = 0; i < 15; i++) {
								for (int j = 0; j < 13; j++)
									board.grid[((int) e.getX() / 13) + +i - 7][((int) e.getY() / 13) + j + 7].border
											.setFill(inset3Color);
							}

							// crop off the edges to make the snowman look a little curvy and realistic
							// SNOWMAN HEAD
							// top right corner
							board.grid[((int) e.getX() / 13) + 3][((int) e.getY() / 13)].border
									.setFill(backgroundColor);
							board.grid[((int) e.getX() / 13) + 3][((int) e.getY() / 13) + 1].border
									.setFill(backgroundColor);
							board.grid[((int) e.getX() / 13) + 2][((int) e.getY() / 13)].border
									.setFill(backgroundColor);

							// top left corner
							board.grid[((int) e.getX() / 13) - 3][((int) e.getY() / 13)].border
									.setFill(backgroundColor);
							board.grid[((int) e.getX() / 13) - 3][((int) e.getY() / 13) + 1].border
									.setFill(backgroundColor);
							board.grid[((int) e.getX() / 13) - 2][((int) e.getY() / 13)].border
									.setFill(backgroundColor);

							// bottom left corner
							board.grid[((int) e.getX() / 13) - 3][((int) e.getY() / 13) + 6].border
									.setFill(backgroundColor);

							// top right corner
							board.grid[((int) e.getX() / 13) + 3][((int) e.getY() / 13) + 6].border
									.setFill(backgroundColor);

							// SNOWMAN'S BODY
							// top right corner
							board.grid[((int) e.getX() / 13) + 5][((int) e.getY() / 13) + 7].border
									.setFill(backgroundColor);
							board.grid[((int) e.getX() / 13) + 7][((int) e.getY() / 13) + 9].border
									.setFill(backgroundColor);
							for (int i = 0; i < 2; i++) {
								for (int j = 0; j < 2; j++)
									board.grid[((int) e.getX() / 13) + i + 6][((int) e.getY() / 13) + j + 7].border
											.setFill(backgroundColor);
							}

							// top left corner
							board.grid[((int) e.getX() / 13) - 5][((int) e.getY() / 13) + 7].border
									.setFill(backgroundColor);
							board.grid[((int) e.getX() / 13) - 7][((int) e.getY() / 13) + 9].border
									.setFill(backgroundColor);
							for (int i = 0; i < 2; i++) {
								for (int j = 0; j < 2; j++)
									board.grid[((int) e.getX() / 13) + i - 7][((int) e.getY() / 13) + j + 7].border
											.setFill(backgroundColor);
							}

							// bottom left corner
							board.grid[((int) e.getX() / 13) - 5][((int) e.getY() / 13) + 19].border
									.setFill(backgroundColor);
							board.grid[((int) e.getX() / 13) - 7][((int) e.getY() / 13) + 17].border
									.setFill(backgroundColor);
							for (int i = 0; i < 2; i++) {
								for (int j = 0; j < 2; j++)
									board.grid[((int) e.getX() / 13) + i - 7][((int) e.getY() / 13) + j + 18].border
											.setFill(backgroundColor);
							}

							// bottom right corner
							board.grid[((int) e.getX() / 13) + 5][((int) e.getY() / 13) + 19].border
									.setFill(backgroundColor);
							board.grid[((int) e.getX() / 13) + 7][((int) e.getY() / 13) + 17].border
									.setFill(backgroundColor);
							for (int i = 0; i < 2; i++) {
								for (int j = 0; j < 2; j++)
									board.grid[((int) e.getX() / 13) + i + 6][((int) e.getY() / 13) + j + 18].border
											.setFill(backgroundColor);
							}
						} else {
							Stage errorStage = new Stage();
							errorStage.initOwner(mainStage);
							errorStage.setTitle("ERROR!");

							VBox errorDialogBox = new VBox(20);
							errorDialogBox.getChildren().addAll(new Text("Error: Inset overlaps another!"),
									new Text("Choose another open area!"));
							Scene errorScene = new Scene(errorDialogBox, 500, 75);
							errorStage.setScene(errorScene);
							errorStage.show();
						}
					}
					// prevent user from inserting in random (little) places.
					catch (ArrayIndexOutOfBoundsException bounds) {

						/*
						 * ArrayOutOfBoundsException ===> User most likely clicked at the extreme ends
						 * of the maze board.
						 */

						// recolor the little head
						for (int i = 0; i < 3; i++)
							for (int j = 0; j < 4; j++)
								try {
									board.grid[((int) e.getX() / 13) + i - 1][((int) e.getY() / 13) - j + 16].border
											.setFill(backgroundColor);
								} catch (Exception e1) {
								}

						// recolor the body
						for (int i = 0; i < 15; i++)
							for (int j = 0; j < 16; j++)
								try {
									board.grid[((int) e.getX() / 13) + i - 7][((int) e.getY() / 13) + j].border
											.setFill(backgroundColor);
								} catch (Exception e1) {
								}

						Stage errorStage = new Stage();
						errorStage.initOwner(mainStage);
						errorStage.setTitle("ERROR!");

						VBox errorDialogBox = new VBox(20);
						errorDialogBox.getChildren().addAll(
								new Text("Error: There's not enough space to insert the inset!"),
								new Text("Choose another open area!"));
						Scene errorScene = new Scene(errorDialogBox, 500, 75);
						errorStage.setScene(errorScene);
						errorStage.show();
					}
					mainScene.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
				}
			});
		});

		// CHRISTMAS TREE
		inset4.setOnMouseClicked(e -> {
			count++;
			System.out.println("Inset4");
			mainScene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					boolean flag = true;
					System.out.println((int) e.getX() / 13 + " " + (int) e.getY() / 13);
					try {
						// CHECK IF THIS INSERTED INSET OVERLAPS ANOTHER INSET!!
						// brown block
						for (int i = 0; i < 3; i++)
							for (int j = 0; j < 4; j++)
								try {
									if (board.grid[((int) e.getX() / 13) + i - 1][((int) e.getY() / 13) - j + 16].border
											.getFill().equals(backgroundColor))
										continue;
									else {
										flag = false;
										break;
									}
								} catch (Exception e1) {
								}
						// green block
						for (int i = 0; i < 15; i++)
							for (int j = 0; j < 15; j++)
								try {
									if (board.grid[((int) e.getX() / 13) + i - 7][((int) e.getY() / 13) + j].border
											.getFill().equals(backgroundColor))
										continue;
									else {
										flag = false;
										break;
									}
								} catch (Exception e1) {
								}

						// IF THERE IS NO OVERLAP
						if (flag) {
							// green area of the tree
							for (int i = 0; i < 16; i++) {
								for (int j = 0; j < i; j++) {
									// color left side of tree
									board.grid[((int) e.getX() / 13) - j / 2][((int) e.getY() / 13) + i].border
											.setFill(inset4Color);
									// color right side of tree
									board.grid[((int) e.getX() / 13) + j / 2][((int) e.getY() / 13) + i].border
											.setFill(inset4Color);
								}
							}
							// brown area
							for (int i = 0; i < 3; i++) {
								for (int j = 0; j < 4; j++)
									board.grid[((int) e.getX() / 13) + -1 + i][((int) e.getY() / 13) + 16 + j].border
											.setFill(inset4Color);
							}

						} else {
							Stage errorStage = new Stage();
							errorStage.initOwner(mainStage);
							errorStage.setTitle("ERROR!");

							VBox errorDialogBox = new VBox(20);
							errorDialogBox.getChildren().addAll(new Text("Error: Inset overlaps another!"),
									new Text("Choose another open area!"));
							Scene errorScene = new Scene(errorDialogBox, 500, 75);
							errorStage.setScene(errorScene);
							errorStage.show();
						}
					}
					// prevent user from inserting in random (little) places.
					catch (ArrayIndexOutOfBoundsException bounds) {

						/*
						 * ArrayOutOfBoundsException ===> User most likely clicked at the extreme ends
						 * of the maze board.
						 */

						// recolor the incomplete brown block
						for (int i = 0; i < 3; i++)
							for (int j = 0; j < 4; j++)
								try {
									board.grid[((int) e.getX() / 13) + i - 1][((int) e.getY() / 13) - j + 16].border
											.setFill(backgroundColor);
								} catch (Exception e1) {
								}

						// recolor incomplete green block
						for (int i = 0; i < 15; i++)
							for (int j = 0; j < 16; j++)
								try {
									board.grid[((int) e.getX() / 13) + i - 7][((int) e.getY() / 13) + j].border
											.setFill(backgroundColor);
								} catch (Exception e1) {
								}

						Stage errorStage = new Stage();
						errorStage.initOwner(mainStage);
						errorStage.setTitle("ERROR!");

						VBox errorDialogBox = new VBox(20);
						errorDialogBox.getChildren().addAll(
								new Text("Error: There's not enough space to insert the inset!"),
								new Text("Choose another open area!"));
						Scene errorScene = new Scene(errorDialogBox, 500, 75);
						errorStage.setScene(errorScene);
						errorStage.show();
					}
					mainScene.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
				}
			});
		});

		generateMaze.setOnMouseClicked(e -> {
			generateFlag = true; // enables key event handler that determines what key was pressed
			currentPositionRow = 0;
			currentPositionColumn = 0;

			border.setStyle("-fx-background-color: SKYBLUE;"); // color of the stage
			border.setLeft(generate.createBoard()); // adding object of GenerateMaze to BorderPane

			/*
			 * Get the insets that the user placed within the earlier maze. Get the state of
			 * the canvas. Call 'generate' which is an object of GenerateMaze.java.
			 */
			generate.initializeCheckedCells();
			generate.mazeAlgorithm();
			generate.solvability();
			generate.insetsIntoWalls();
			generate.mazeInInsets();
			generate.startsEnds();

		});

		mainScene.setOnKeyPressed(e -> {
			// Interact using arrow keys or W, A, S, D keys

			// user goes up
			if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W) {
				System.out.println("Go up!");
				try {
					// assuming the up direction doesn't involve a wall && inset boundary
					if (!generate.grid[currentPositionRow][currentPositionColumn - 1].cell.getFill()
							.equals(generate.wallColor)
							&& !generate.grid[currentPositionRow][currentPositionColumn - 1].cell.getFill()
									.equals(generate.insetColor)) {

						Color color = (Color) generate.grid[currentPositionRow][currentPositionColumn - 1].cell
								.getFill();
						// make the position and turn the user's position upwards
						generate.grid[currentPositionRow][currentPositionColumn - 1].cell.setFill(Color.DARKBLUE);
						generate.grid[currentPositionRow][currentPositionColumn - 1].cell.setStroke(Color.CYAN);
						generate.grid[currentPositionRow][currentPositionColumn - 1].cell.setStrokeWidth(2);

						// position of the column will become 1 less
						currentPositionColumn--;

						// color the previous node back to its original state
						generate.grid[currentPositionRow][currentPositionColumn + 1].cell.setFill(visitedColor);
						generate.grid[currentPositionRow][currentPositionColumn + 1].cell.setStroke(visitedColor);

						visitedColor = color;
					}

				} catch (ArrayIndexOutOfBoundsException e1) {
					// ignore
					System.out.println("UP ACTION IGNORED");
				}
			}

			// user goes down
			else if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) {
				System.out.println("Go down!");
				try {
					// assuming the down direction doesn't involve a wall && inset boundary
					if (!generate.grid[currentPositionRow][currentPositionColumn + 1].cell.getFill()
							.equals(generate.wallColor)
							&& !generate.grid[currentPositionRow][currentPositionColumn + 1].cell.getFill()
									.equals(generate.insetColor)) {

						Color color = (Color) generate.grid[currentPositionRow][currentPositionColumn + 1].cell
								.getFill();
						// make the position and turn the user's position downwards
						generate.grid[currentPositionRow][currentPositionColumn + 1].cell.setFill(Color.DARKBLUE);
						generate.grid[currentPositionRow][currentPositionColumn + 1].cell.setStroke(Color.CYAN);
						generate.grid[currentPositionRow][currentPositionColumn + 1].cell.setStrokeWidth(2);

						// position of the column will become 1 more
						currentPositionColumn++;

						// color the previous node back to its original state
						generate.grid[currentPositionRow][currentPositionColumn - 1].cell.setFill(visitedColor);
						generate.grid[currentPositionRow][currentPositionColumn - 1].cell.setStroke(visitedColor);

						visitedColor = color;
					}
				} catch (ArrayIndexOutOfBoundsException e1) {
					// ignore
					System.out.println(e1);
					System.out.println("DOWN ACTION IGNORED");
				}
			}

			// user goes left
			else if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) {
				System.out.println("Go left!");
				try {
					// assuming the left direction doesn't involve a wall && inset boundary
					if (!generate.grid[currentPositionRow - 1][currentPositionColumn].cell.getFill()
							.equals(generate.wallColor)
							&& !generate.grid[currentPositionRow - 1][currentPositionColumn].cell.getFill()
									.equals(generate.insetColor)) {

						Color color = (Color) generate.grid[currentPositionRow - 1][currentPositionColumn].cell
								.getFill();

						// make the position and turn the user's position left
						generate.grid[currentPositionRow - 1][currentPositionColumn].cell.setFill(Color.DARKBLUE);
						generate.grid[currentPositionRow - 1][currentPositionColumn].cell.setStroke(Color.CYAN);
						generate.grid[currentPositionRow - 1][currentPositionColumn].cell.setStrokeWidth(2);

						// position of the row will become 1 less
						currentPositionRow--;

						// color the previous node back to its original state
						generate.grid[currentPositionRow + 1][currentPositionColumn].cell.setFill(visitedColor);
						generate.grid[currentPositionRow + 1][currentPositionColumn].cell.setStroke(visitedColor);

						visitedColor = color;
					}
				} catch (ArrayIndexOutOfBoundsException e1) {
					// ignore
					System.out.println(e1);
					System.out.println("LEFT ACTION IGNORED");
				}
			}

			// user goes right
			else if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
				System.out.println("Go right!");
				try {
					// assuming the right direction doesn't involve a wall && inset boundary
					if (!generate.grid[currentPositionRow + 1][currentPositionColumn].cell.getFill()
							.equals(generate.wallColor)
							&& !generate.grid[currentPositionRow + 1][currentPositionColumn].cell.getFill()
									.equals(generate.insetColor)) {

						Color color = (Color) generate.grid[currentPositionRow + 1][currentPositionColumn].cell
								.getFill();

						// make the position and turn the user's position right
						generate.grid[currentPositionRow + 1][currentPositionColumn].cell.setFill(Color.DARKBLUE);
						generate.grid[currentPositionRow + 1][currentPositionColumn].cell.setStroke(Color.CYAN);
						generate.grid[currentPositionRow + 1][currentPositionColumn].cell.setStrokeWidth(2);

						// position of the row will become 1 more
						currentPositionRow++;

						// color the previous node back to its original state
						generate.grid[currentPositionRow - 1][currentPositionColumn].cell.setFill(visitedColor);
						generate.grid[currentPositionRow - 1][currentPositionColumn].cell.setStroke(visitedColor);

						visitedColor = color;
					}

				} catch (ArrayIndexOutOfBoundsException e1) {
					// ignore
					System.out.println(e1);
					System.out.println("RIGHT ACTION IGNORED");
				}
			}

			else {
				// ...
				// ignore any other actions
				// ...
			}

			// check if the current position matches the ending of the maze
			if (currentPositionRow == generate.cellsX - 1 && currentPositionColumn == generate.cellsY - 1) {
				Stage dialogStage = new Stage();
				dialogStage.initOwner(mainStage);
				dialogStage.setTitle("CONGRATULATIONS!");

				VBox dialogBox = new VBox(20);
				dialogBox.getChildren().addAll(new Text("CONGRATULATIONS!\nYou completed the maze!"),
						new Text("Generate/create a new maze!"));
				Scene errorScene = new Scene(dialogBox, 500, 75);
				dialogStage.setScene(errorScene);
				dialogStage.show();
			}

		});
		mainStage.setScene(mainScene);
		mainStage.show();
	}

	public VBox addVBox() {

		/*
		 * Create a layout to hold buttons, options, etc. for user.
		 * https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
		 */

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(5);

		// Title of VBox
		Text title = new Text("MENU OPTIONS");
		title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
		title.setFill(Color.HOTPINK);
		vbox.getChildren().add(title);

		/*
		 * IMAGE CREDITS: Images associated with the respective buttons are taken from
		 * Google Images.
		 */

		// Display Instructions
		instructions = new Button();
		Image image6 = new Image("graphics/instructions.png", 210, 80, false, false);
		instructions.setGraphic(new ImageView(image6));
		instructions.setMaxSize(250, 120);
		instructions.setOnAction(new EventHandler<ActionEvent>() {

			// handle() method is invoked
			// This is a generic method - it's invoked upon an action in the GUI.
			/*
			 * This is an anonymous inner class --- you DON'T have to use event.getSource();
			 */

			@Override
			public void handle(ActionEvent event) {
				Stage instructions = new Stage();
				instructions.initOwner(mainStage);
				VBox instructionsPopUp = new VBox(20); // Upon clicking on instructions, a new pop up window will open.
				instructionsPopUp.setPadding(new Insets(10));

				Text title2 = new Text("INSTRUCTIONS");
				title2.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
				title2.setFill(Color.HOTPINK);

				Text explainDrawing = new Text("Choose from the following options to generate your desired maze.");
				Text explainInsets = new Text(
						"Inserting insets - Insert mini-mazes of your choice in a place of your choice.");
				Text explainGenerate = new Text(
						"Generate maze - clicking on this button will create a randomized maze, with your path and insets considered.");
				Text explainCreate = new Text("Create new maze - restart the application once more.");
				Text explainExit = new Text("Exit application - exit the maze.");

				instructionsPopUp.getChildren().addAll(title2, explainDrawing, explainInsets, explainGenerate,
						explainCreate, explainExit);

				Scene dialogScene = new Scene(instructionsPopUp, 600, 250);
				instructions.setScene(dialogScene);
				instructions.show();
			}

		});
		vbox.getChildren().add(instructions);

		// Insert insets into the maze.
		HBox hbox1 = new HBox(), hbox2 = new HBox();
		VBox vboxOfHBoxes = new VBox();

		inset1 = new Button();
		Image inset1Image = new Image("graphics/inset1.png", 100, 90, false, false);
		inset1.setGraphic(new ImageView(inset1Image));
		inset1.setMaxSize(250, 120);

		inset2 = new Button();
		Image inset2Image = new Image("graphics/inset2.png", 100, 90, false, false);
		inset2.setGraphic(new ImageView(inset2Image));
		inset2.setMaxSize(250, 120);

		inset3 = new Button();
		Image inset3Image = new Image("graphics/inset3.png", 100, 90, false, false);
		inset3.setGraphic(new ImageView(inset3Image));
		inset3.setMaxSize(250, 120);

		inset4 = new Button();
		Image inset4Image = new Image("graphics/inset4.png", 100, 90, false, false);
		inset4.setGraphic(new ImageView(inset4Image));
		inset4.setMaxSize(250, 120);

		hbox1.getChildren().addAll(inset1, inset2);
		hbox2.getChildren().addAll(inset3, inset4);
		vboxOfHBoxes.getChildren().addAll(hbox1, hbox2);
		vbox.getChildren().add(vboxOfHBoxes);
		/*
		 * Button insertInsets = new Button(); insertInsets.setText("Insert Insets");
		 * Image image2 = new Image("graphics/2.png", 100, 200, false, false);
		 * insertInsets.setGraphic(new ImageView(image2)); insertInsets.setMaxSize(250,
		 * 120); vbox.getChildren().add(insertInsets); insertInsets.setOnMouseEntered(e
		 * -> { vbox.getChildren().remove(insertInsets); });
		 * insertInsets.setOnMouseMoved(e -> { insertInsets.setVisible(false);
		 * vbox.getChildren().remove(insertInsets); vbox.getChildren().add(insets); });
		 * insertInsets.setOnMouseExited(e -> { insertInsets.setVisible(true);
		 * vbox.getChildren().add(insertInsets); vbox.getChildren().remove(insets); });
		 */

		// Generate the maze.
		generateMaze = new Button();
		generateMaze.setText("Generate Maze");
		Image image3 = new Image("graphics/3.png", 100, 100, false, false);
		generateMaze.setGraphic(new ImageView(image3));
		generateMaze.setMaxSize(250, 120);
		vbox.getChildren().add(generateMaze);

		// Re-do the entire process again.
		restartMaze = new Button();
		restartMaze.setText("Create New Maze");
		Image image4 = new Image("graphics/4.png", 100, 100, false, false);
		restartMaze.setGraphic(new ImageView(image4));
		restartMaze.setMaxSize(250, 120);
		restartMaze.setOnAction(e -> {
			// board.createContent();
			border.setLeft(board.createBoard());
		});
		vbox.getChildren().add(restartMaze);

		// Exit the maze window.
		exitApp = new Button();
		exitApp.setText("Exit Application");
		Image image5 = new Image("graphics/5.png", 100, 100, false, false);
		exitApp.setGraphic(new ImageView(image5));
		exitApp.setMaxSize(250, 170);
		exitApp.setOnAction(e -> System.exit(0));
		vbox.getChildren().add(exitApp);

		return vbox;
	}

	public VBox insets() {
		HBox hbox1 = new HBox(), hbox2 = new HBox();
		VBox vbox = new VBox();

		inset1 = new Button();
		Image inset1Image = new Image("graphics/inset1.png", 100, 80, false, false);
		inset1.setGraphic(new ImageView(inset1Image));
		inset1.setMaxSize(250, 120);

		inset2 = new Button();
		Image inset2Image = new Image("graphics/inset2.png", 100, 80, false, false);
		inset2.setGraphic(new ImageView(inset2Image));
		inset2.setMaxSize(250, 120);

		hbox1.getChildren().addAll(inset1, inset2);

		inset3 = new Button();
		Image inset3Image = new Image("graphics/inset1.png", 100, 80, false, false);
		inset3.setGraphic(new ImageView(inset3Image));
		inset3.setMaxSize(250, 120);

		inset4 = new Button();
		Image inset4Image = new Image("graphics/inset2.png", 100, 80, false, false);
		inset4.setGraphic(new ImageView(inset4Image));
		inset4.setMaxSize(250, 120);

		hbox1.getChildren().addAll(inset1, inset2);
		hbox2.getChildren().addAll(inset3, inset4);
		vbox.getChildren().addAll(hbox1, hbox2);
		return vbox;

	}

}
