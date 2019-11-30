import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public interface MazeGeneration {

	interface Menu {
		VBox addVBox();

		VBox insets();
	}

	interface IterativeDivision {
		void mazeAlgorithm();

		boolean solvability();
	}

	interface Board {
	
		Pane createBoard();
	}

}
